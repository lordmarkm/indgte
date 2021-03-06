package com.baldwin.indgte.webapp.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import com.baldwin.indgte.persistence.model.Notification;
import com.baldwin.indgte.webapp.controller.JSON;

@Component
public class Comet {
	static Logger log = LoggerFactory.getLogger(Comet.class);
	
	//<Principal name, live() response>
	private ConcurrentMap<String, DeferredResult<JSON>> waiting = new ConcurrentHashMap<>();

	public ConcurrentMap<String, DeferredResult<JSON>> getWaiting() {
		return waiting;
	}

	public void fireNotif(Notification notif) {
		if(null == notif || null == notif.getNotified()) {
			log.debug("Nobody needs to be notified. Strange. {}", notif);
			return;
		}
		
		String notifyName = notif.getNotified().getUsername();
		DeferredResult<JSON> response = waiting.remove(notifyName);
		
		if(null == response) {
			log.debug("Null response retrieved for {}, user is likely offline.", notifyName);
			return;
		} else if(response.isSetOrExpired()) {
			log.debug("Response is set or expired. They'll get their notif on the next live() cycle");
			return;
		}
		
		List<Notification> notifications = new ArrayList<>();
		notifications.add(notif);
		
		JSON asyncResponse = JSON.ok();
		asyncResponse.put("notifications", notifications);
		
		response.setResult(asyncResponse);
	}
}
