package com.baldwin.indgte.webapp.controller.impl;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.baldwin.indgte.persistence.model.ChatMessage;
import com.baldwin.indgte.webapp.controller.ChatController;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.dto.Chatter;
import com.baldwin.indgte.webapp.misc.ChatRepository;

@Component
public class ChatControllerImpl implements ChatController {

	static Logger log = Logger.getLogger(ChatControllerImpl.class);
	
	@Autowired
	private ChatRepository chat;
	
	@Override
	@SuppressWarnings("unchecked")
	public @ResponseBody DeferredResult<JSON> getChatters(Principal principal, @RequestParam("chatters[]") String[] chatters) {
		DeferredResult<JSON> response = new DeferredResult<>();
		
			Object[] changes = chat.getChatters(principal.getName(), chatters);
			
			Collection<Chatter> goneOnline = (Collection<Chatter>) changes[0];
			Collection<Chatter> goneOffline = (Collection<Chatter>) changes[1];
			List<String> channels = (List<String>) changes[2];
			
			if(goneOnline.size() > 0 || goneOffline.size() > 0 || channels.size() > 0) {
				JSON jsonResponse = JSON.ok()
						.put("goneOnline", goneOnline)
						.put("goneOffline", goneOffline)
						.put("channels", channels);
				response.setResult(jsonResponse);
			} else {
				chat.storePresenceRequest(response);
			}
			
			return response;
	}

	@Override
	public @ResponseBody JSON send(Principal principal, @RequestParam String channel, @RequestParam String message) {
		try {
			chat.send(principal.getName(), channel, message);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Exception posting message", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody DeferredResult<List<ChatMessage>> getMessages(Principal principal, @RequestParam("channels[]") String[] channels, @RequestParam long lastReceivedId) {
		DeferredResult<List<ChatMessage>> result = new DeferredResult<>();
		
		List<ChatMessage> messages = chat.getMessages(principal.getName(), channels, lastReceivedId);
		if(messages.size() > 0) {
			result.setResult(messages);
		} else {
			chat.storeResult(principal.getName(), result);
		}
		
		return result;
	}

	@Override
	public @ResponseBody JSON getChannelMessages(Principal principal, @PathVariable String channel, @PathVariable int howmany) {
		try { 
			return JSON.ok().put("messages", chat.getChannelMessages(channel, howmany));
		} catch (Exception e) {
			log.error("Error getting messages for channel " + channel, e);
			return JSON.status500(e);
		}
	}

}
