package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.clean;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.service.PostsService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.pers‪istence.dao.PostDao;
import com.baldwin.indgte.webapp.controller.InteractiveController;
import com.baldwin.indgte.webapp.controller.JSON;

@Component
public class InteractiveControllerImpl implements InteractiveController {

	static Logger log = LoggerFactory.getLogger(InteractiveControllerImpl.class);
	
	@Autowired
	private PostsService posts;
	
	@Autowired UserService users;
	
	@Autowired
	private PostDao postDao;
	
	@Override
	public @ResponseBody JSON subposts(Principal principal, @RequestParam int start, @RequestParam int howmany) {
		try {
			return JSON.ok().put("posts", posts.getSubposts(principal.getName(), start, howmany));
		} catch (Exception e) {
			log.error("Exception getting subposts", e);
			return JSON.status500(e);
		}
	}
	
	@Override
	public @ResponseBody JSON lastPosts(long posterId, PostType type, int start, int howmany) {
		return JSON.ok().put("posts", postDao.getById(posterId, type, start, howmany));
	}

	@Override
	public @ResponseBody JSON post(long posterId, PostType type, String title, String text) {
		title = clean(title);
		text = clean(text);
		Post post = postDao.newPost(posterId, type, title, text);
		return JSON.ok().put("post", post);
	}

	@Override
	public @ResponseBody JSON businessPost(long posterId, String title, String text) {
		return post(posterId, PostType.business, title, text);
	}

	@Override
	public @ResponseBody JSON subscribe(Principal principal, @PathVariable PostType type, @PathVariable Long id) {
		try {
			posts.subscribe(principal.getName(), type, id);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Error", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON unsubscribe(Principal principal, @PathVariable PostType type, @PathVariable Long id) {
		try {
			posts.unsubscribe(principal.getName(), type, id);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Error", e);
			return JSON.status500(e);
		}
	}

}
