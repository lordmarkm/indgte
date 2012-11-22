package com.baldwin.indgte.webapp.misc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

@Service
public class ChatRepository implements ApplicationListener<InteractiveAuthenticationSuccessEvent>, LogoutSuccessHandler {
	static Logger log = LoggerFactory.getLogger(ChatRepository.class);
	
	@Autowired
	private ChatDao dao;
	
	private CopyOnWriteArraySet<String> sessions = new CopyOnWriteArraySet<>();
	
	//<channel name, Set<participant name>> - channel must start with #
	private ConcurrentMap<String, Set<String>> channels = new ConcurrentHashMap<>();
	
	//<username, DeferredResult>
	private ConcurrentMap<String, DeferredResult<List<ChatMessage>>> waiting = new ConcurrentHashMap<>();
	
	public Set<String> getAllPrincipals() {
		return sessions;
	}
	
	private List<String> extractUsernamesFromChannel(String channel) {
		return Arrays.asList(channel.split("\\|"));
	}
	
	public void send(String channel, String message) {
		ChatMessage chatMessage = dao.newMessage(channel, message);

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
			sessions.add((String) p);
		}
	}
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		Object p = authentication.getPrincipal();
		log.debug("Unregistering principal {}", p);
		
		if(p instanceof String) {
			sessions.remove(p);
			for(Set<String> participants : channels.values()) {
				participants.remove(p);
			}
		}
		
		response.sendRedirect("/indgte-webapp/login.jsp?loggedout=true");
	}

	public List<ChatMessage> getMessages(String username, String[] subscriptions, long lastReceivedId) {
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
}
