package com.baldwin.indgte.webapp.controller.impl;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.baldwin.indgte.persistence.model.ChatMessage;
import com.baldwin.indgte.persistence.model.Notification;
import com.baldwin.indgte.persistence.service.NotificationsService;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.controller.LiveController;
import com.baldwin.indgte.webapp.dto.Chatter;
import com.baldwin.indgte.webapp.misc.ChatRepository;

@Component
public class LiveControllerImpl implements LiveController {

	static Logger log = LoggerFactory.getLogger(LiveControllerImpl.class);
	
	@Autowired
	private ChatRepository chat;
	
	@Autowired
	private NotificationsService notifs;
	
	@Override
	@SuppressWarnings("unchecked")
	public @ResponseBody DeferredResult<JSON> live (	Principal principal, 
										@RequestParam boolean initial,
										@RequestParam("chatters[]") String[] chatters, 
										@RequestParam("channels[]") String[] channels, 
										@RequestParam long lastReceivedId,
										@RequestParam long lastNotifId//,
										//@RequestParam("rejectedNotifs[]") Long[] rejectedNotifs
										) 
	{
//		notifs.clearNotifications(principal.getName(), rejectedNotifs);

		//make the response
		DeferredResult<JSON> response = new DeferredResult<>();
		JSON json = JSON.ok();
		boolean respond = false;

		//old getChatters(...)
		Object[] changes = chat.getChatters(principal.getName(), chatters, initial);
		Collection<Chatter> goneOnline = (Collection<Chatter>) changes[0];
		Collection<Chatter> goneOffline = (Collection<Chatter>) changes[1];
		Collection<Chatter> userSubs = (Collection<Chatter>) changes[2];
		List<String> yourchannels = (List<String>) changes[3];
		if(goneOnline.size() > 0 || goneOffline.size() > 0) {
			respond = true;
			json.put("goneOnline", goneOnline)
				.put("goneOffline", goneOffline)
				.put("usersubs", userSubs)
				.put("channels", yourchannels);
			
			log.debug("Returning because getOnline or goneOffline size > 0");
		}
		
		//old getMessages(...)
		List<ChatMessage> messages = chat.getMessages(principal.getName(), channels, lastReceivedId);
		if(messages.size() > 0) {
			respond = true;
			json.put("messages", messages);
			
			log.debug("Returning because messages size > 0");
		} 
		
		//notifications
		if(lastNotifId != -1) { //on pages where notifs are not displayed (probably no pages are like this but hey), this will be -1
			Collection<Notification> notifications = notifs.getUnread(principal.getName(), lastNotifId);
			if(notifications.size() > 0) {
				respond = true;
				json.put("notifications", notifications);
				
				log.debug("Returning because notifications size > 0");
			}
		}
		
		//respond if needed
		if(respond) {
			response.setResult(json);
		} else {
			chat.storeLiveResponse(principal.getName(), response);
		}
		
		return response;
	}
	
	
}
