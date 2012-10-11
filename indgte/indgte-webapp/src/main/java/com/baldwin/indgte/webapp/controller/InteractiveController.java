package com.baldwin.indgte.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	@RequestMapping(value = "/posts/", method = RequestMethod.GET)
	public JSON lastPosts(long posterId, PostType type, int start, int howmany);
	
	@RequestMapping(value = "/posts/", method = RequestMethod.POST)
	public JSON post(long posterId, PostType type, String title, String text);

	@RequestMapping(value = "/posts/business/", method = RequestMethod.POST)
	public JSON businessPost(long posterId, String title, String text);
}
