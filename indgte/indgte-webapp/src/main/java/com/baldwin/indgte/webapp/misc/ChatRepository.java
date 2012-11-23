package com.baldwin.indgte.webapp.misc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.baldwin.indgte.persistence.model.ChatMessage;
import com.baldwin.indgte.pers‪istence.dao.ChatDao;
import com.baldwin.indgte.pers‪istence.dao.InteractiveDao;
import com.baldwin.indgte.pers‪istence.dao.UserDao;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.dto.Chatter;

@Service
public class ChatRepository implements ApplicationListener<InteractiveAuthenticationSuccessEvent>, LogoutSuccessHandler {
	static Logger log = LoggerFactory.getLogger(ChatRepository.class);
	
	@Autowired
	private ChatDao dao;
	
	@Autowired
	private UserDao users;
	
	@Autowired
	private InteractiveDao interact;
	
	private List<DeferredResult<JSON>> presenceRequests = Collections.synchronizedList(new ArrayList<DeferredResult<JSON>>());

	private Set<Chatter> presence = Collections.synchronizedSet(new HashSet<Chatter>());
	
	//<channel name, Set<participant name>> - channel must start with #
	private ConcurrentMap<String, Set<String>> channels = new ConcurrentHashMap<>();
	
	//<username, DeferredResult>
	private ConcurrentMap<String, DeferredResult<List<ChatMessage>>> waiting = new ConcurrentHashMap<>();
	
	private List<String> extractUsernamesFromChannel(String channel) {
		return Arrays.asList(channel.split("\\|"));
	}
	
	public void send(String sender, String channel, String message) {
		log.debug("Posting message [{}] to channel [{}]", message, channel);
		
		ChatMessage chatMessage = dao.newMessage(sender, channel, message);

		Collection<String> receivers = channel.startsWith("#") ? channels.get(channel) : extractUsernamesFromChannel(channel);
		
		List<ChatMessage> result = new ArrayList<ChatMessage>();
		result.add(chatMessage);
		for(String username : receivers) {
			DeferredResult<List<ChatMessage>> response = waiting.remove(username);
			if(null == response || response.isSetOrExpired()) {
				continue;
			}
			response.setResult(result);
		}
	}
	
	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		Object p = event.getAuthentication().getPrincipal();
		log.debug("Registering principal {}", p);
		
		if(p instanceof String) {
			String username = (String)p;
			
			Chatter newChatter = new Chatter(username, users.getImageUrl(username));
			
			presence.add(newChatter);
			
			List<Chatter> lonechatter = new ArrayList<Chatter>();
			lonechatter.add(newChatter);
			JSON asyncResponse = JSON.ok().put("goneOnline", lonechatter);
			synchronized(presenceRequests) {
				for(Iterator<DeferredResult<JSON>> i = presenceRequests.iterator(); i.hasNext();) {
					DeferredResult<JSON> presenceRequest = i.next();
					if(presenceRequest.isSetOrExpired()) {
						i.remove();
						continue;
					}
					presenceRequest.setResult(asyncResponse);
				}
			}
		}
	}
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		Object p = authentication.getPrincipal();
		log.debug("Unregistering principal {}", p);
		
		if(p instanceof String) {
			String username = (String)p;
			
			synchronized(presence) {
				for(Iterator<Chatter> i = presence.iterator(); i.hasNext();) {
					if(i.next().getUsername().equals(username)) i.remove();
				}
			}
			
			List<Chatter> quitter = new ArrayList<>();
			quitter.add(convertToChatter(username));
			JSON asyncResponse = JSON.ok().put("goneOffline", quitter);
			synchronized(presenceRequests) {
				for(Iterator<DeferredResult<JSON>> i = presenceRequests.iterator(); i.hasNext();) {
					DeferredResult<JSON> presenceRequest = i.next();
					if(presenceRequest.isSetOrExpired()) {
						i.remove();
						continue;
					}
					presenceRequest.setResult(asyncResponse);
				}
			}
			
			for(Set<String> participants : channels.values()) {
				participants.remove(p);
			}
		}
		
		response.sendRedirect("/indgte-webapp/login.jsp?loggedout=true");
	}

	public List<ChatMessage> getMessages(String username, String[] subscriptions, long lastReceivedId) {
		log.debug("Finding messages to user [{}] on channels [{}] who last received id {}", username, Arrays.asList(subscriptions), lastReceivedId);
		
		//if user is not subscribed to the channel, add him
		//if channel does not exist, create it
		for(String channel : subscriptions) {
			Set<String> participants = channels.get(channel);
			if(null == participants) {
				Set<String> participant = new CopyOnWriteArraySet<>();
				participant.add(username);
				channels.put(channel, participant);
				continue;
			}
			participants.add(username);
		}
		
		return dao.getMessages(subscriptions, lastReceivedId);
	}

	public void storeResult(String name, DeferredResult<List<ChatMessage>> result) {
		waiting.put(name, result);
	}

	public Collection<ChatMessage> getChannelMessages(String channel, int howmany) {
		return dao.getChannelMessages(channel, howmany);
	}

	/**
	 * @return Collection[] {goneOnline, goneOffline, subs channels}
	 */
	private final String CHANNEL_PREFIX = "#";
	private final String HOME_CHANNEL = "#dumaguete";
	@SuppressWarnings("unchecked")
	public Collection<Object>[] getChatters(String username, String[] chatters) {
		//if chatters[0] = 'none', it's an initial request and we should send his business subscriptions'
		//channels along
		List<String> channels = new ArrayList<>();
		if(chatters.length == 1 && DgteConstants.CHANNEL_NONE.equals(chatters[0])) {
			List<String> subsDomains = interact.getBusinessSubscriptionDomains(username);
			for(String domain : subsDomains) {
				channels.add(CHANNEL_PREFIX + domain);
			}
			channels.add(HOME_CHANNEL);
		}
		
		Set<Chatter> goneOnline = new HashSet<>(presence);
		List<String> goneOffline = new ArrayList<String>(Arrays.asList(chatters));
		for(Iterator<String> j = goneOffline.iterator(); j.hasNext();) {
			String name = j.next();
			if(name.startsWith(CHANNEL_PREFIX)) {
				//it's a channel
				j.remove();
				continue;
			}
			for(Iterator<Chatter> i = goneOnline.iterator(); i.hasNext();) {
				if(i.next().getUsername().equals(name)) {
					i.remove();
					j.remove();
				}
			}
		}
		
		return new Collection[]{goneOnline, convertToChatters(goneOffline), channels};
	}

	public void storePresenceRequest(DeferredResult<JSON> response) {
		presenceRequests.add(response);
	}
	
	private List<Chatter> convertToChatters(List<String> usernames) {
		List<Chatter> chatters = new ArrayList<>(usernames.size());
		if(usernames.size() == 1 && DgteConstants.USERNAME_NONE.equals(usernames.get(0))) return chatters;
		for(String username : usernames) {
			Chatter chatter = convertToChatter(username);
			chatters.add(chatter);
		}
		return chatters;
	}
	
	private Chatter convertToChatter(String username) {
		return new Chatter(username, users.getImageUrl(username));
	}
}
