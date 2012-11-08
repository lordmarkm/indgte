package com.baldwin.indgte.persistence.service;

import static com.baldwin.indgte.persistence.constants.Initializable.reviewqueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.constants.ReviewType;
import com.baldwin.indgte.persistence.constants.WishType;
import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.BusinessReview;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.model.Review;
import com.baldwin.indgte.persistence.model.TopTenCandidate;
import com.baldwin.indgte.persistence.model.TopTenList;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.persistence.model.UserReview;
import com.baldwin.indgte.pers‪istence.dao.InteractiveDao;
import com.baldwin.indgte.pers‪istence.dao.TableConstants;
import com.baldwin.indgte.pers‪istence.dao.UserDao;

@Service
public class InteractiveService {
	static Logger log = LoggerFactory.getLogger(InteractiveService.class);
	
	@Autowired
	private InteractiveDao dao;

	@Autowired
	private UserDao users;
	
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
	
	public BusinessReview getBusinessReview(String name, long targetId) {
		return dao.getBusinessReview(name, targetId);
	}
	
	public UserReview getUserReview(String name, long targetId) {
		return dao.getUserReview(name, targetId);
	}

	public Review businessReview(String name, long businessId, int score, String justification) {
		return dao.businessReview(name, businessId, score, justification);
	}

	public Review userReview(String name, long targetId, int score, String clean) {
		return dao.userReview(name, targetId, score, clean);
	}
	
	public Collection<? extends Review> getReviews(long targetId, ReviewType type) {
		switch(type) {
		case business:
			return dao.getBusinessReviews(targetId);
		case user:
			return dao.getUserReviews(targetId);
		default:
			throw new IllegalStateException("Unknown type: " + type);
		}
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

	public void addToWishlist(String name, WishType type, long id) {
		dao.addToWishlist(name, type, id);
	}
	
	public List<Summary> getReviewRequests(String username) {
		UserExtension user = users.getExtended(username, reviewqueue);
		List<Summary> reviewRequests = new ArrayList<Summary>();
		for(BusinessProfile business : user.getForReview()) {
			reviewRequests.add(business.summarize());
		}
		return reviewRequests;
	}

	public void noReview(String name, long businessId) {
		dao.noReview(name, businessId);
	}

	public void neverReview(String name, long businessId) {
		dao.neverReview(name, businessId);
	}

	public Object[] getReviewStats(long targetId, ReviewType type) {
		switch(type) {
		case business:
			return dao.getBusinessReviewStats(targetId, type);
		case user:
			return dao.getUserReviewStats(targetId, type);
		default:
			throw new IllegalStateException("Unknown type: " + type);
		}
	}

	public long getBusinessTopTenListId(long groupId) {
		return dao.getBusinessTopTenListId(groupId);
	}
}
