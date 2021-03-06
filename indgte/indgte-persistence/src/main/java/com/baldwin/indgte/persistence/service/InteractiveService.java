package com.baldwin.indgte.persistence.service;

import static com.baldwin.indgte.persistence.constants.Initializable.reviewqueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.constants.AttachmentType;
import com.baldwin.indgte.persistence.constants.Background;
import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.constants.ReviewType;
import com.baldwin.indgte.persistence.constants.Theme;
import com.baldwin.indgte.persistence.constants.WishType;
import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.BusinessReview;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.model.Review;
import com.baldwin.indgte.persistence.model.TopTenCandidate;
import com.baldwin.indgte.persistence.model.TopTenList;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.persistence.model.UserReview;
import com.baldwin.indgte.pers‪istence.dao.BusinessDao;
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
	
	@Autowired
	private BusinessDao businesses;
	
	@Value("${toptens.candidate.preview}")
	private int toptensCandidatePreview;
	
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
		return dao.getSubposts(username, start, end, null);
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
		return dao.getToptens(start, howmany, TableConstants.TOPTEN_DATECREATED, toptensCandidatePreview);
	}

	public Collection<TopTenList> getPopularToptens(int start, int howmany) {
		return dao.getToptens(start, howmany, TableConstants.TOPTEN_TOTALVOTES, toptensCandidatePreview);
	}

	public Collection<TopTenList> getUserToptens(String name) {
		return dao.getUserToptens(name, toptensCandidatePreview);
	}

	public TopTenList getTopten(long toptenId) {
		return dao.getTopTenList(toptenId);
	}

	public TopTenCandidate createTopTenCandidate(String name, long topTenId, String title) {
		return dao.createTopTenCandidate(name, topTenId, title);
	}
	
	public TopTenCandidate topTenVote(String name, long topTenId) {
		return dao.toptenVote(name, topTenId);
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

	public void saveTopTenList(String name, TopTenList list) {
		dao.saveTopTenList(name, list);
	}

	public void attachImageToCandidate(String name, long candidateId,	Imgur imgur) {
		dao.attachImageToCandidate(name, candidateId, imgur);
	}

	public Imgur addDescriptionToCandidate(long candidateId, String description) {
		return dao.addDescriptionToCandidate(candidateId, description);
	}

	public String addDescriptionToList(long listId, String description) {
		return dao.addDescriptionToList(listId, description);
	}

	public void addEntityToList(String name, long listId, AttachmentType type,	long attachmentId) {
		dao.addEntityToList(name, listId, type, attachmentId);
	}

	public Collection<TopTenList> getAllLists() {
		return dao.getAllLists();
	}

	public void changetheme(String name, Theme newtheme) {
		dao.changetheme(name, newtheme);
	}
	
	public void changebg(String name, Background newBg) {
		dao.changebg(name, newBg);
	}

	public Post newPost(long posterId, PostType type, String title, String text) {
		return dao.newPost(posterId, type, title, text);
	}

	public Post getPost(long postId) {
		return dao.getPost(postId);
	}

	public Review getReview(ReviewType type, long reviewId, boolean getReactors) {
		return dao.getReview(type, reviewId, getReactors);
	}

	public Review reviewReact(String name, ReviewType type, String mode, long reviewId) {
		return dao.reviewReact(name, type, mode, reviewId);
	}

	public int countsubs(PostType type, Long id) {
		return dao.subscount(type, id);
	}

	public Collection<Post> getPosts(int start, int howmany) {
		return dao.getPosts(start, howmany, null);
	}

	public void initializeAttachment(TopTenCandidate candidate) {
		dao.initializeAttachment(candidate);
	}

	public Collection<Post> getPosts(String username, int start, int howmany, String tagFilter, String sort) {
		Collection<Post> results;
		switch(sort) {
		case "subs":
			results = dao.getSubposts(username, start, howmany, tagFilter);
			break;
		case "newest":
			results = dao.getPosts(start, howmany, tagFilter);
			break;
		case "popularity":
			results = dao.getPostsByPopularity(start, howmany, tagFilter);
			break;
		default:
			throw new IllegalArgumentException("Illegal sort order: " + sort);
		}
		
		return results;
	}

	public boolean isPostOwner(Post post, UserExtension user) {
		switch(post.getType()) {
		case user:
			return post.getPosterId() == user.getId();
		case business:
			BusinessProfile business = businesses.get(post.getPosterId());
			return business.getOwner().equals(user);
		default:
			throw new IllegalStateException("Unsupported post type: " + post.getType());
		}
	}

	public Post getRandomFeaturedPost() {
		Post featured = dao.getRandomFeaturedPost();
		log.debug("Returning featured post: {}", featured);
		return featured;
	}

	public void deletepost(boolean moderator, String name, long id) {
		UserExtension user = users.getExtended(name);
		Post post = dao.getPost(id);
		
		boolean owner = isPostOwner(post, user);
		
		if(owner) {
			log.info("{} has deleted post with id {} ({})", name, id, post.getTitle());
		} else if(moderator) {
			log.info("[moderator] {} is deleting post with id {} ({})", name, id, post.getTitle());
		}
		
		if(moderator || owner) {
			dao.deletepost(id);
		} else {
			throw new IllegalArgumentException("You do not own this post.");
		}
	}

	public void deleteReview(boolean moderator, String name, ReviewType type, long reviewId) throws IllegalAccessException {
		Review review = dao.getReview(type, reviewId, false);
		String reviewer = review.getReviewerSummary().getIdentifier();
		String reviewee = review.getRevieweeSummary().getIdentifier();
		if(reviewer.equals(name)) {
			log.info("{} is deleting his review of {}", name, review.getRevieweeSummary().getIdentifier());
			dao.deleteReview(type, reviewId);
		} else if(moderator) {
			log.info("[moderator] {} is deleting {}'s review of {}", name, reviewer, reviewee);
			dao.deleteReview(type, reviewId);
		} else {
			throw new IllegalAccessException(name + " does not have permission to delete this review.");
		}
	}

	public List<Post> getBusinessGroupPosts(long groupId, int start, int howmany, String filterTag) {
		return dao.getBusinessGroupPosts(groupId, start, howmany, filterTag);
	}

	public Post getBusinessGroupFeaturedPost(long groupId) {
		return dao.getBusinessGroupFeaturedPost(groupId);
	}
}