package com.baldwin.indgte.webapp.controller.impl;

import java.security.Principal;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.baldwin.indgte.persistence.model.ChatMessage;
import com.baldwin.indgte.webapp.controller.ChatController;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.misc.ChatRepository;

@Component
public class ChatControllerImpl implements ChatController {

	static Logger log = Logger.getLogger(ChatControllerImpl.class);
	
	@Autowired
	private ChatRepository chat;
	
	@Override
	public @ResponseBody JSON getChatters(Principal principal) {
		try {
			return JSON.ok().put("chatters", chat.getAllPrincipals());
		} catch (Exception e) {
			log.error("Exception getting authenticated users", e);
			return JSON.status500(e);
		}
	}

	@Override
	public JSON send(Principal principal, @RequestParam String channel, @RequestParam String message) {
		try {
			chat.send(channel, message);
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

}
