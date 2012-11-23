package com.baldwin.indgte.webapp.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import com.baldwin.indgte.persistence.model.ChatMessage;

@Controller
@RequestMapping("/c/")
public interface ChatController {
	
	@RequestMapping(method = RequestMethod.GET)
	public DeferredResult<JSON> getChatters(Principal principal, String[] chatters);
	
	@RequestMapping(value="/messages/", method = RequestMethod.POST)
	public JSON send(Principal principal, String channel, String message);
	
	/**
	 * lastReceived = largest (and thus most recent) message id received from server
	 */
	@RequestMapping(value="/messages/", method = RequestMethod.GET)
	public DeferredResult<List<ChatMessage>> getMessages(Principal principal, String[] channels, long lastReceivedId);
	
	@RequestMapping(value="/messages/{channel}/{howmany}.json", method = RequestMethod.POST) //Post prevents caching
	public JSON getChannelMessages(Principal principal, String channel, int howmany);
}
