package com.baldwin.indgte.persistence.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.model.BusinessReview;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.model.TopTenCandidate;
import com.baldwin.indgte.persistence.model.TopTenList;
import com.baldwin.indgte.pers‪istence.dao.InteractiveDao;
import com.baldwin.indgte.pers‪istence.dao.TableConstants;

@Service
public class InteractiveService {
	static Logger log = LoggerFactory.getLogger(InteractiveService.class);
	
	@Autowired
	private InteractiveDao dao;

	public void subscribe(String username, PostType type, Long id) {
		switch(type) {
		case user:
			dao.subscribeToUser(username, id);
			break;
		case business:
			dao.subscribeToBusiness(username, id);
			break;
		default:
			log.warn("Unsupported subscription type: {}", type);
		}
	}

	public void unsubscribe(String username, PostType type, Long id) {
		switch(type) {
		case user:
			dao.unsubscribeFromUser(username, id);
			break;
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
	public boolean isSubscribed(String name, long targetId, PostType type) {
		return dao.isSubscribed(name, targetId, type);
	}
	
	public void saveOrUpdate(Post post) {
		dao.saveOrUpdate(post);
	}

	public BusinessReview getReview(String name, long businessId) {
		return dao.getReview(name, businessId);
	}

	public BusinessReview review(String name, long businessId, int score, String justification) {
		return dao.review(name, businessId, score, justification);
	}

	public Collection<BusinessReview> getReviews(long businessId) {
		return dao.getReviews(businessId);
	}

	/*
	 * Top Tens
	 */
	
	public Collection<TopTenList> getRecentToptens(int start, int howmany) {
		return dao.getToptens(start, howmany, TableConstants.TOPTEN_DATECREATED);
	}

	public Collection<TopTenList> getPopularToptens(int start, int howmany) {
		return dao.getToptens(start, howmany, TableConstants.TOPTEN_TOTALVOTES);
	}

	public Collection<TopTenList> getUserToptens(String name) {
		return dao.getUserToptens(name);
	}

	public TopTenList getTopten(long toptenId) {
		return dao.getTopTenList(toptenId);
	}

	public TopTenList createTopTenList(String name, String title) {
		return dao.createTopTenList(name, title);
	}

	public TopTenCandidate createTopTenCandidate(String name, long topTenId, String title) {
		return dao.createTopTenCandidate(name, topTenId, title);
	}
	
	public void topTenVote(String name, long topTenId) {
		dao.toptenVote(name, topTenId);
	}
}
