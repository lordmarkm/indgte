package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.baldwin.indgte.persistence.constants.PostType;

/**
 * For things like likes, posts and other stuff
 * I can't think of a better name than InteractiveController, not because
 * there is no better name, but because I suck
 * @author mbmartinez
 */

@Controller
@RequestMapping("/i/")
public interface InteractiveController {
	/**
	 * Get most recent posts of entities principal is subscribed to
	 */
	@RequestMapping(value = "/subposts.json", method = RequestMethod.GET)
	public JSON subposts(Principal principal, int start, int howmany);
	
	/**
	 * Get most recent posts of a single entity
	 */
	@RequestMapping(value = "/posts/", method = RequestMethod.GET)
	public JSON lastPosts(long posterId, PostType type, int start, int howmany);
	
	/**
	 * Post new status - supports attachments
	 * 
	 * image:
	 *  hash - imgur hash 
	 *  
	 * video:
	 * 	embed - embed code, 3rd party provided (explain this in the docs)
	 */
	@RequestMapping(value = "/newstatus.json", method = RequestMethod.POST)
	public JSON newstatus(Principal principal, WebRequest request);
	
	/**
	 * New entity-specific posts? These don't support attachments and will soon be deprecated
	 */
	@RequestMapping(value = "/posts/", method = RequestMethod.POST)
	public JSON post(long posterId, PostType type, String title, String text);

	@RequestMapping(value = "/posts/business/", method = RequestMethod.POST)
	public JSON businessPost(long posterId, String title, String text);
	
	/**
	 * Subscribe/unsubscribe
	 */
	@RequestMapping(value = "/subscribe/{type}/{id}.json", method = RequestMethod.POST)
	public JSON subscribe(Principal principal, PostType type, Long id);
	
	@RequestMapping(value = "/unsubscribe/{type}/{id}.json", method = RequestMethod.POST)
	public JSON unsubscribe(Principal principal, PostType type, Long id);
}
