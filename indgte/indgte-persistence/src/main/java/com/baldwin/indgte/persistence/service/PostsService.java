package com.baldwin.indgte.persistence.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.pers‪istence.dao.PostDao;

@Service
public class PostsService {
	static Logger log = LoggerFactory.getLogger(PostsService.class);
	
	@Autowired
	private PostDao dao;

	public void subscribe(String username, PostType type, Long id) {
		switch(type) {
		case business:
			dao.subscribeToBusiness(username, id);
			break;
		default:
			log.warn("Unsupported subscription type: {}", type);
		}
	}

	public void unsubscribe(String username, PostType type, Long id) {
		switch(type) {
		case business:
			dao.unsubscribeFromBusiness(username, id);
			break;
		default:
			log.warn("Unsupported subscription type: {}", type);
		}
	}
	
	public Collection<Post> getSubposts(String username, int start, int end) {
		return dao.getSubposts(username, start, end);
	}

	public boolean isSubscribed(String username, long businessId) {
		return dao.isSubscribed(username, businessId);
	}

	public void saveOrUpdate(Post post) {
		dao.saveOrUpdate(post);
	}
}
