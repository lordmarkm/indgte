package com.baldwin.indgte.pers‪istence.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.dto.Fame;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.BusinessReview;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.model.Product;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.persistence.model.UserReview;

/**
 * <p>A Dao for assigning and computing User "Fame"
 * <ol type="I">
 *  <li>Businesses and Products
 *   <ol>
 *    <li>Somebody comments on your Business, category, or product (entity) +1
 *    <li>Entity is liked +10
 *    <li>Entity is sent +10
 *    <li>Business, Category, or Product is featured by mod +200
 *   </ol>
 *  <li>Posts (has absorbed Article)
 * 	 <ol>
 * 	  <li>Somebody comments +1
 * 	  <li>Post is liked +10
 *    <li>Post is 'sent' +10
 * 	  <li>Post is featured by mod +200
 * 	 </ol>
 *  <li>Reviews
 *   <ol>
 *    <li>Somebody comments on a review you wrote +1
 *    <li>Somebody agrees with a review you wrote +10
 *    <li>Somebody reviews you or an entity you own favorably (score 3 or more) +50
 *   </ol>
 *  <li>Friendships
 *   <ol>
 *    <li>User subscribes to your page (BusinessProfile) +10
 *    <li>User subscribes to your personal account +10
 *   </ol>
 */

@Repository
@Transactional
public class FameDaoImpl implements FameDao {

	static Logger log = LoggerFactory.getLogger(FameDaoImpl.class);
	
	private static final int FAVORABLE_REVIEW_THRESHOLD = 3;
	
	private static final int PTS_COMMENT = 1;
	private static final int PTS_LIKE = 10;
	private static final int PTS_SEND = 10;
	private static final int PTS_FEATURE = 200;
	
	private static final int PTS_REV_AGREE = 10;
	private static final int PTS_REV_GOODREVIEW = 50;
	
	private static final int PTS_SUBS_PERSONAL = 10;
	private static final int PTS_SUBS_BUSINESS = 10;
	
	@Autowired
	private SessionFactory sessions;
	
	@Autowired
	private UserDao users;
	
	@Override
	public Fame computeFame(String username) {
		long startTime = System.currentTimeMillis();
		
		UserExtension user = users.getExtended(username);

		int postfame = getPostFame(user);
		int reviewfame = getReviewFame(user);
		int friendshipfame = getFriendshipFame(user);
		int entityfame = getEntityFame(user);
		int total = postfame + reviewfame + friendshipfame + entityfame;
		
		Fame fame = new Fame();
		fame.setPostfame(postfame);
		fame.setReviewfame(reviewfame);
		fame.setFriendshipfame(friendshipfame);
		fame.setEntityfame(entityfame);
		fame.setTotal(total);
		
		log.debug("Fame computation completed in {} ms.", System.currentTimeMillis() - startTime);
		return fame;
	}
	
	//"Review" operations
	private int getReviewFame(UserExtension user) {
		int reviewComments = 0;
		int reviewAgrees = 0;
		for(BusinessReview review : user.getBusinessReviews()) {
			reviewComments += review.getComments();
			reviewAgrees += review.getAgreeCount();
		}
		for(UserReview review : user.getReviewsWritten()) {
			reviewComments += review.getComments();
			reviewAgrees += review.getAgreeCount();
		}
		
		int favorableReviews = 0;
		for(BusinessProfile business : user.getBusinesses()) {
			for(BusinessReview review : business.getReviews()) {
				if(review.getScore() >= FAVORABLE_REVIEW_THRESHOLD) {
					favorableReviews++;
				}
			}
		}
		
		for(UserReview review : user.getReviewsReceived()) {
			if(review.getScore() >= FAVORABLE_REVIEW_THRESHOLD) {
				favorableReviews++;
			}
		}
		
		int total = 0;
		total += reviewComments * PTS_COMMENT;
		total += reviewAgrees * PTS_REV_AGREE;
		total += favorableReviews * PTS_REV_GOODREVIEW;
		
		return total;
	}
	
	//"Post" operations
	private int getPostFame(UserExtension user) {
		long id = user.getId();
		
		@SuppressWarnings("unchecked")
		List<Long> businessIds = sessions.getCurrentSession().createCriteria(BusinessProfile.class)
			.add(Restrictions.eq(TableConstants.BUSINESS_OWNER, user))
			.setProjection(Projections.property(TableConstants.ID))
			.list();

		log.debug("Business ids: {}", businessIds);
		if(businessIds.size() == 0) return 0;
		
		ProjectionList fields = Projections.projectionList()
				.add(Projections.sum(TableConstants.LIKES))
				.add(Projections.sum(TableConstants.SENDS))
				.add(Projections.sum(TableConstants.TIMESFEATURED))
				.add(Projections.sum(TableConstants.COMMENTS));
		
		Object[] score = (Object[]) sessions.getCurrentSession().createCriteria(Post.class)
				.add(
					Restrictions.disjunction()
						.add( //type = user and postrId = user.id
							Restrictions.conjunction()
								.add(Restrictions.eq(TableConstants.POST_POSTERID, id))
								.add(Restrictions.eq(TableConstants.TYPE, PostType.user))
						)
						.add( //OR type = business and posterId in businessIds
							Restrictions.conjunction()
								.add(Restrictions.eq(TableConstants.TYPE, PostType.business))
								.add(Restrictions.in(TableConstants.POST_POSTERID, businessIds))
						)
				)
				.setProjection(fields)
				.uniqueResult();
		
		Number likes = (Number) score[0];
		Number sends = (Number) score[1];
		Number featured = (Number) score[2];
		Number comments = (Number) score[3];
		
		log.debug("Likes: {}, Sends: {}, Featured: {}, Comments: {}", likes, sends, featured);
		
		int total = 0;
		total += likes.intValue() * PTS_LIKE;
		total += sends.intValue() * PTS_SEND;
		total += featured.intValue() * PTS_FEATURE;
		total += comments.intValue() * PTS_COMMENT;
		
		return total;
	}
	
	private int getFriendshipFame(UserExtension user) {
		long id = user.getId();
		
		Number subscribers = (Number) sessions.getCurrentSession()
					.createQuery("select count(id) from UserExtension u where :userId in elements(u.userSubscriptions)")
					.setLong("userId", id)
					.uniqueResult();

		log.debug("Subscribers: " + subscribers);
		
		int businessSubsTotal = 0;
		for(BusinessProfile business : user.getBusinesses()) {
			Long businessId = business.getId();
			Number businessSubs = (Number) sessions.getCurrentSession()
					.createQuery("select count(*) from UserExtension u where :businessId in elements(u.businessSubscriptions)")
					.setLong("businessId", businessId)
					.uniqueResult();
			businessSubsTotal += businessSubs.intValue();
		}
		
		int total = 0;
		total += subscribers.intValue() * PTS_SUBS_PERSONAL;
		total += businessSubsTotal * PTS_SUBS_BUSINESS;
		
		return total;
	}
	
	private int getEntityFame(UserExtension user) {
		
		int comments = 0;
		int likes = 0;
		int sends = 0;
		
		for(BusinessProfile business : user.getBusinesses()) {
			comments += business.getComments();
			likes += business.getLikes();
			sends += business.getSends();
			
			for(Category category : business.getCategories()) {
				comments += category.getComments();
				likes += category.getLikes();
				sends += category.getSends();
				
				for(Product product : category.getProducts()) {
					comments += product.getComments();
					likes += product.getLikes();
					sends += product.getSends();
				}
			}
		}
		
		int total = 0;
		total += comments * PTS_COMMENT;
		total += likes * PTS_LIKE;
		total += sends * PTS_SEND;
		
		return total;
	}
	
}