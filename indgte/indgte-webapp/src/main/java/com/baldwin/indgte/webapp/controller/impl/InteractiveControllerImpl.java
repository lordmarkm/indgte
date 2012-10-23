package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.clean;

import java.security.Principal;
import java.util.Date;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.Summary.SummaryType;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.persistence.service.PostsService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.pers‪istence.dao.PostDao;
import com.baldwin.indgte.webapp.controller.InteractiveController;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.misc.DgteTagWhitelist;

@Component
public class InteractiveControllerImpl implements InteractiveController {

	static Logger log = LoggerFactory.getLogger(InteractiveControllerImpl.class);
	
	@Autowired
	private PostsService posts;
	
	@Autowired 
	private UserService users;
	
	@Autowired
	private BusinessService businesses;
	
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
	public @ResponseBody JSON newstatus(Principal principal, WebRequest request) {
		log.debug("Poster id: [{}]", request.getParameter("posterId"));
		log.debug("Poster type: [{}]", request.getParameter("posterType"));
		log.debug("Text: [{}]", request.getParameter("text"));
		
		log.debug("Attachment? {}", request.getParameter("attachmentType"));
		
		Summary poster;
		PostType postType = PostType.valueOf(request.getParameter("posterType"));
		switch(postType) {
		case user:
			User user = users.getFacebook(principal.getName());
			poster = user.summarize();
			break;
		case business:
			BusinessProfile business = businesses.get(Long.parseLong(request.getParameter("posterId")));
			poster = business.summarize();
			break;
		default:
			throw new IllegalArgumentException("Illegal post type " + postType);
		}
		
		Summary attachment;
		SummaryType attachmentType = SummaryType.valueOf(request.getParameter("attachmentType"));
		switch(attachmentType) {
		case image:
			attachment = new Summary(attachmentType, null, null, null, null, request.getParameter("hash"));
			break;
		case video:
			String embed = request.getParameter("embed");
			embed = Jsoup.clean(embed, DgteTagWhitelist.videos());
			attachment = new Summary(attachmentType, null, null, null, embed, null);
			break;
		case none:
			attachment = null;
			break;
		default:
			throw new IllegalArgumentException("Illegal attachment type " + attachmentType);
		}
		
		Post post = new Post(poster, attachment);
		
		String title = clean(request.getParameter("title"));
		String text = clean(request.getParameter("text"));
		
		post.setType(postType);
		post.setPostTime(new Date());
		post.setTitle(title);
		post.setText(text);
		
		posts.saveOrUpdate(post);
		log.debug("Post: {} text: {}", post);
		
		return JSON.ok().put("post", post);
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
