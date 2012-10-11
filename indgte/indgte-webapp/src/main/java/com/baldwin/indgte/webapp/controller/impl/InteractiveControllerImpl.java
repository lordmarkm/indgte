package com.baldwin.indgte.webapp.controller.impl;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.pers‪istence.dao.PostDao;
import com.baldwin.indgte.webapp.controller.InteractiveController;
import com.baldwin.indgte.webapp.controller.JSON;

@Component
public class InteractiveControllerImpl implements InteractiveController {

	@Autowired
	private PostDao postDao;
	
	@Override
	public @ResponseBody JSON lastPosts(long posterId, PostType type, int start, int howmany) {
		return JSON.ok().put("posts", postDao.getById(posterId, type, start, howmany));
	}

	@Override
	public @ResponseBody JSON post(long posterId, PostType type, String title, String text) {
		title = StringEscapeUtils.escapeHtml(title).replace("\n", "<br>");
		text = StringEscapeUtils.escapeHtml(text).replace("\n", "<br>");
		Post post = postDao.newPost(posterId, type, title, text);
		return JSON.ok().put("post", post);
	}

	@Override
	public @ResponseBody JSON businessPost(long posterId, String title, String text) {
		return post(posterId, PostType.business, title, text);
	}
}
