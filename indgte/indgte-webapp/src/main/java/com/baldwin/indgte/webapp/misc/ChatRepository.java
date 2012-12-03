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
import com.baldwin.indgte.persistence.model.MessageNotification;
import com.baldwin.indgte.persistence.model.Notification;
import com.baldwin.indgte.persistence.model.User;
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

	@Autowired
	private Comet comet;
	
	private Set<Chatter> presence = Collections.synchronizedSet(new HashSet<Chatter>());
	private ConcurrentMap<String, Set<String>> channels = new ConcurrentHashMap<>();
	//<username, DeferredResult>

	private List<String> extractUsernamesFromChannel(String channel) {
		return Arrays.asList(channel.split("\\|"));
	}

	public void send(String sender, String channel, String message) {
		log.debug("Posting message [{}] to channel [{}]", message, channel);

		//[ChatMessage, MessageNotification]
		Object[] msgAndNotif = dao.newMessage(sender, channel, message);

		//new chatmessage
		ChatMessage chatMessage = (ChatMessage) msgAndNotif[0];
		List<ChatMessage> messages = new ArrayList<>();
		messages.add(chatMessage);
		
		//new notif
		MessageNotification notification = (MessageNotification) msgAndNotif[1];
		List<Notification> notifications = null;
		if(null != notification) {
			notifications = new ArrayList<>();
			notifications.add(notification);
		}

		Collection<String> receivers = channel.startsWith("#") ? channels.get(channel) : extractUsernamesFromChannel(channel);
		for(String username : receivers) {
			DeferredResult<JSON> response = comet.getWaiting().remove(username);
			if(null == response || response.isSetOrExpired()) {
				continue;
			}
			
			JSON asyncResponse = JSON.ok().put("messages", messages);
			
			//don't send the notif to the sender
			if(null != notifications && !username.equals(sender)) {
				asyncResponse.put("notifications", notifications);
			}
			
			response.setResult(asyncResponse);
		}
	}

	private void newChatter(String username) {
		Chatter newChatter = new Chatter(username, users.getImageUrl(username));
		presence.add(newChatter);

		List<Chatter> lonechatter = new ArrayList<Chatter>();
		lonechatter.add(newChatter);
		JSON asyncResponse = JSON.ok().put("goneOnline", lonechatter);
		for(Iterator<DeferredResult<JSON>> i = comet.getWaiting().values().iterator(); i.hasNext();) {
			DeferredResult<JSON> presenceRequest = i.next();
			if(presenceRequest.isSetOrExpired()) {
				i.remove();
				continue;
			}
			presenceRequest.setResult(asyncResponse);
		}
	}

	private void removeChatter(String username) {
		synchronized(presence) {
			for(Iterator<Chatter> i = presence.iterator(); i.hasNext();) {
				if(i.next().getUsername().equals(username)) i.remove();
			}
		}

		List<Chatter> quitter = new ArrayList<>();
		quitter.add(convertToChatter(username));
		JSON asyncResponse = JSON.ok().put("goneOffline", quitter);
		for(Iterator<DeferredResult<JSON>> i = comet.getWaiting().values().iterator(); i.hasNext();) {
			DeferredResult<JSON> presenceRequest = i.next();
			if(presenceRequest.isSetOrExpired()) {
				i.remove();
				continue;
			}
			presenceRequest.setResult(asyncResponse);
		}

		for(Iterator<Set<String>> i = channels.values().iterator(); i.hasNext();) {
			Set<String> participants = i.next();
			participants.remove(username);
			
			if(participants.size() == 0) {
				log.debug("0 participants left in channel {}. Removing...");
				i.remove();
			}
		}
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
			} else {
				participants.add(username);
			}
		}

		return dao.getMessages(subscriptions, lastReceivedId);
	}

	public Collection<ChatMessage> getChannelMessages(String username, String channel, int howmany) {
		Set<String> participants = channels.get(channel);
		if(null == participants) {
			Set<String> participant = new CopyOnWriteArraySet<>();
			participant.add(username);
			channels.put(channel, participant);
			log.debug("New channel {} created. Channels: {}", channel, channels);
		} else {
			participants.add(username);
		}

		return dao.getChannelMessages(channel, howmany);
	}

	/**
	 * @return Collection[] {goneOnline, goneOffline, user subs, subs channels}
	 */
	private final String CHANNEL_PREFIX = "#";
	private final String HOME_CHANNEL = "#dumaguete";
	@SuppressWarnings("unchecked")
	public Collection<Object>[] getChatters(String username, String[] chatters, boolean initial) {
		Set<Chatter> goneOnline = new HashSet<>(presence);
		List<String> goneOffline = new ArrayList<String>(Arrays.asList(chatters));

		//channels along
		//also his user subs
		List<Chatter> userSubs = new ArrayList<>();
		List<String> channels = new ArrayList<>();
		if(initial) {
			List<String> subsDomains = interact.getBusinessSubscriptionDomains(username);
			for(String domain : subsDomains) {
				channels.add(CHANNEL_PREFIX + domain);
			}
			channels.add(HOME_CHANNEL);

			List<User> users = interact.getUserSubscripionSummaries(username);
			for(User user : users) {
				Chatter sub = new Chatter(user.getUsername(), user.getImageUrl());
				userSubs.add(sub);
			}

			return new Collection[]{goneOnline, convertToChatters(goneOffline), userSubs, channels};
		}

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
		List<Chatter> goneOfflineChatters = convertToChatters(goneOffline);

		return new Collection[]{goneOnline, goneOfflineChatters, userSubs, channels};
	}

	public void storeLiveResponse(String respondee, DeferredResult<JSON> response) {
		comet.getWaiting().put(respondee, response);
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

	public void toggleAppearOffline(String name, boolean appearOffline) {
		if(appearOffline) removeChatter(name);
		else newChatter(name);
	}

	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		Object p = event.getAuthentication().getPrincipal();
		log.debug("Registering principal {}", p);

		if(p instanceof String) {
			String username = (String)p;
			newChatter(username);
		}
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		Object p = authentication.getPrincipal();
		log.debug("Unregistering principal {}", p);

		if(p instanceof String) {
			String username = (String)p;
			removeChatter(username);
		}

		response.sendRedirect("/indgte-webapp/login.jsp?loggedout=true");
	}
}
