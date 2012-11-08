package com.baldwin.indgte.pers‪istence.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.constants.ReviewType;
import com.baldwin.indgte.persistence.constants.WishType;
import com.baldwin.indgte.persistence.model.BusinessReview;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.model.TopTenCandidate;
import com.baldwin.indgte.persistence.model.TopTenList;
import com.baldwin.indgte.persistence.model.UserReview;

public interface InteractiveDao {

	/**
	 * Will only ever find posts by businesses
	 * @deprecated use getById instead
	 */
	@Deprecated
	public Set<Post> getByDomain(String domain, int start, int howmany);

	public Collection<Post> getById(long posterId, PostType type, int start, int howmany);

	public Post newPost(long posterId, PostType type, String title, String text);

	public void subscribeToBusiness(String username, Long id);

	public List<Post> getSubposts(String username, int start, int end);

	public void unsubscribeFromBusiness(String username, Long id);

	public void saveOrUpdate(Post post);

	public void subscribeToUser(String username, Long id);

	public void unsubscribeFromUser(String username, Long id);

	public boolean isSubscribed(String name, long targetId, PostType type);

	public BusinessReview getBusinessReview(String name, long businessId);

	public UserReview getUserReview(String name, long userId);
	
	public BusinessReview businessReview(String name, long businessId, int score,	String justification);

	public UserReview userReview(String name, long targetId, int score, String clean);
	
	public Collection<BusinessReview> getBusinessReviews(long businessId);

	public Collection<UserReview> getUserReviews(long targetId);
	
	public Collection<TopTenList> getToptens(int start, int howmany, String orderColumn);

	public Collection<TopTenList> getUserToptens(String name);

	public TopTenList getTopTenList(long toptenId);

	public TopTenList createTopTenList(String name, String title);

	public void toptenVote(String name, long topTenId);

	public TopTenCandidate createTopTenCandidate(String name, long topTenId, String title);

	public boolean addToWishlist(String name, WishType type, long id);

	public void noReview(String name, long businessId);

	public void neverReview(String name, long businessId);

	public Object[] getBusinessReviewStats(long targetId, ReviewType type);

	public Object[] getUserReviewStats(long targetId, ReviewType type);

	public long getBusinessTopTenListId(long groupId);
}
