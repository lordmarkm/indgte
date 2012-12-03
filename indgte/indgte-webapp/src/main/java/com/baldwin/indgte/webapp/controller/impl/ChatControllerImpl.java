package com.baldwin.indgte.webapp.controller.impl;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baldwin.indgte.webapp.controller.ChatController;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.misc.ChatRepository;

@Component
public class ChatControllerImpl implements ChatController {

	static Logger log = LoggerFactory.getLogger(ChatControllerImpl.class);
	
	@Autowired
	private ChatRepository chat;
	
//	@Override
//	@SuppressWarnings("unchecked")
//	public @ResponseBody DeferredResult<JSON> getChatters(Principal principal, @RequestParam("chatters[]") String[] chatters, @RequestParam boolean initial) {
//		DeferredResult<JSON> response = new DeferredResult<>();
//		
//			Object[] changes = chat.getChatters(principal.getName(), chatters, initial);
//			
//			Collection<Chatter> goneOnline = (Collection<Chatter>) changes[0];
//			Collection<Chatter> goneOffline = (Collection<Chatter>) changes[1];
//			Collection<Chatter> userSubs = (Collection<Chatter>) changes[2];
//			List<String> channels = (List<String>) changes[3];
//			
//			if(goneOnline.size() > 0 || goneOffline.size() > 0) {
//				JSON jsonResponse = JSON.ok()
//						.put("goneOnline", goneOnline)
//						.put("goneOffline", goneOffline)
//						.put("usersubs", userSubs)
//						.put("channels", channels);
//				response.setResult(jsonResponse);
//			} else {
//				chat.storePresenceResponse(response);
//			}
//			
//			return response;
//	}

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

//	@Override
//	public @ResponseBody DeferredResult<List<ChatMessage>> getMessages(Principal principal, @RequestParam("channels[]") String[] channels, @RequestParam long lastReceivedId) {
//		DeferredResult<List<ChatMessage>> result = new DeferredResult<>();
//		
//		List<ChatMessage> messages = chat.getMessages(principal.getName(), channels, lastReceivedId);
//		if(messages.size() > 0) {
//			result.setResult(messages);
//		} else {
//			chat.storeMessagesResponse(principal.getName(), result);
//		}
//		
//		return result;
//	}

	@Override
	public @ResponseBody JSON getChannelMessages(Principal principal, @PathVariable String channel, @PathVariable int howmany) {
		try { 
			return JSON.ok().put("messages", chat.getChannelMessages(principal.getName(), channel, howmany));
		} catch (Exception e) {
			log.error("Error getting messages for channel " + channel, e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON toggleAppearOffline(Principal principal, @PathVariable boolean appearOffline) {
		try {
			log.debug("{} wants to appear {}", principal.getName(), appearOffline ? "offline" : "online");
			chat.toggleAppearOffline(principal.getName(), appearOffline);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Exception toggling appear offline", e);
			return JSON.status500(e);
		}
	}
}
