package com.baldwin.indgte.pers‪istence.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.constants.AttachmentType;
import com.baldwin.indgte.persistence.constants.Initializable;
import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.constants.ReviewType;
import com.baldwin.indgte.persistence.constants.WishType;
import com.baldwin.indgte.persistence.model.BusinessGroup;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.BusinessReview;
import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.model.Product;
import com.baldwin.indgte.persistence.model.TopTenCandidate;
import com.baldwin.indgte.persistence.model.TopTenList;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.persistence.model.UserReview;
import com.baldwin.indgte.persistence.model.Wish;

@Repository
@Transactional
public class InteractiveDaoImpl implements InteractiveDao {

	static Logger log = LoggerFactory.getLogger(InteractiveDaoImpl.class);
	
	@Autowired
	private SessionFactory sessions;
	
	@Autowired
	private UserDao users;
	
	@Autowired
	private BusinessDao businesses;
	
	@Autowired
	private TradeDao trade;
	
	@Value("${wishlist.maxitems}")
	private int wishlistMaxItems;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Post> getSubposts(String username, int start, int howmany) {
		Session session = sessions.getCurrentSession();
		
		final PostType[] supportedTypes = new PostType[] {PostType.business, PostType.user};
		
		Map<PostType, Set<Long>> subscriptions = getSubscriptions(username, supportedTypes);
		if(null == subscriptions) {
			return null;
		}
		
		int queriedTypes = 0;
		Disjunction orJunction = Restrictions.disjunction();
		
		for(PostType type : supportedTypes) {
			if(subscriptions.get(type).size() != 0) {
				orJunction.add(
						Restrictions.conjunction()
							.add(Restrictions.in("posterId", subscriptions.get(type)))
							.add(Restrictions.eq("type", type))
						);
				++queriedTypes;
			}
		}
		
		if(queriedTypes == 0) {
			log.debug("No active subscriptions for {}, returning empty list", username);
			return new ArrayList<Post>();
		}
		
		return session.createCriteria(Post.class)
			.add(orJunction)
			.setFirstResult(start)
			.setMaxResults(howmany)
			.addOrder(Order.desc(TableConstants.POST_TIME))
			.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<Post> getById(long posterId, PostType type, int start, int howmany) {
		return sessions.getCurrentSession().createCriteria(Post.class)
				.add(Restrictions.eq("posterId", posterId))
				.add(Restrictions.eq("type", type))
				.setFirstResult(start)
				.setMaxResults(howmany)
				.addOrder(Order.desc(TableConstants.POST_TIME))
				.list();
	}
	
	/**
	 * @deprecated use getById instead
	 */
	@Deprecated
	public Set<Post> getByDomain(String domain, int start, int howmany) {
		Session session = sessions.getCurrentSession();
		
		long posterId = (Long) session.createQuery("select id from BusinessProfile where domain = :domain")
						.setString("domain", domain)
						.uniqueResult();
		
		log.debug("Poster id: {}", posterId);
		
		@SuppressWarnings("unchecked")
		List<Post> posts = session.createCriteria(Post.class)
				.add(Restrictions.eq("posterId", posterId))
				.add(Restrictions.eq("type", PostType.business))
				.list();
		
		return new HashSet<Post>(posts);
	}

	@Override
	public Post newPost(long posterId, PostType type, String title, String text) {
		Session session = sessions.getCurrentSession();
		
		Post post = null;
		
		switch(type) {
		case business:
			BusinessProfile businessPoster = (BusinessProfile) session.get(BusinessProfile.class, posterId);
			post = new Post(businessPoster.summarize(), null);
			break;
		default:
		}
		
		post.setType(type);
		post.setTitle(title);
		post.setText(text);
		post.setPostTime(new Date());
		
		session.save(post);
		return post;
	}

	@Override
	public void subscribeToUser(String username, Long id) {
		User user = users.getSpring(username);
		user.getUserSubscriptions().add(id);
	}

	@Override
	public void unsubscribeFromUser(String username, Long id) {
		User user = users.getSpring(username);
		user.getUserSubscriptions().remove(id);
	}
	
	@Override
	public void subscribeToBusiness(String name, Long id) {
		User user = users.getSpring(name);
		user.getBusinessSubscriptions().add(id);
	}
	
	@Override
	public void unsubscribeFromBusiness(String username, Long id) {
		User user = users.getSpring(username);
		user.getBusinessSubscriptions().remove(id);
	}
	
	private Map<PostType, Set<Long>> getSubscriptions(String name, PostType[] types) {
		log.debug("Getting {} subscriptions for {}", types, name);
		
		Criteria crit = sessions.getCurrentSession().createCriteria(User.class)
			.add(Restrictions.eq(TableConstants.USER_USERNAME, name))
			.add(Restrictions.eq(TableConstants.USER_PROVIDERID, TableConstants.USER_PROVIDERID_SPRINGSOCSEC));
			
		for(PostType type : types) {
			switch(type) {
			case user:
				crit.setFetchMode("userSubscriptions", FetchMode.JOIN);
				break;
			case business: 
				crit.setFetchMode("businessSubscriptions", FetchMode.JOIN);
				break;
			}
		}
		
		User user = (User) crit.uniqueResult();
		
		if(null == user) {
			log.debug("User {} not found.", name);
			return null;
		}
		
		Map<PostType, Set<Long>> subscriptions = new HashMap<PostType, Set<Long>>();
		subscriptions.put(PostType.user, user.getUserSubscriptions());
		subscriptions.put(PostType.business, user.getBusinessSubscriptions());
		
		log.debug("Subscriptions: {}", subscriptions);
		return subscriptions;
	}

	@Override
	public boolean isSubscribed(String name, long targetId, PostType type) {
		switch(type) {
		case user:
			return users.getSpring(name).getUserSubscriptions().contains(targetId);
		case business:
			return users.getSpring(name).getBusinessSubscriptions().contains(targetId);
		}
		return false;
	}

	@Override
	public void saveOrUpdate(Post post) {
		sessions.getCurrentSession().saveOrUpdate(post);
	}

	@Override
	public BusinessReview getBusinessReview(String name, long businessId) {
		return (BusinessReview) sessions.getCurrentSession().createCriteria(BusinessReview.class)
			.createAlias("reviewer.user", "reviewer") //user.username needed
			.createAlias("reviewed", "reviewed")
			.add(Restrictions.eq("reviewer.username", name))
			.add(Restrictions.eq("reviewed.id", businessId))
			.uniqueResult();
	}
	

	@Override
	public UserReview getUserReview(String name, long userId) {
		return (UserReview) sessions.getCurrentSession().createCriteria(UserReview.class)
				.createAlias("reviewer.user", "reviewer") //user.username needed Update: trying delegate method
				.createAlias("reviewee", "reviewee") //userExtension.id needed
				.add(Restrictions.eq("reviewer.username", name))
				.add(Restrictions.eq("reviewee.id", userId))
				.uniqueResult();
	}

	@Override
	public BusinessReview businessReview(String name, long businessId, int score, String justification) {
		Session session = sessions.getCurrentSession();
		
		BusinessReview review = getBusinessReview(name, businessId);
		if(null == review) {
			review = new BusinessReview();
			
			UserExtension reviewer = users.getExtended(name);
			reviewer.getBusinessReviews().add(review);
			
			BusinessProfile business = businesses.get(businessId);
			business.getReviews().add(review);
			
			review.setReviewer(reviewer);
			review.setReviewed(business);
			
			reviewer.getForReview().remove(business);
		}
		
		review.setScore(score);
		review.setJustification(justification);
		review.setTime(new Date()); //last updated
		
		session.saveOrUpdate(review);
		
		return review;
	}

	@Override
	public UserReview userReview(String name, long targetId, int score, String clean) {
		Session session = sessions.getCurrentSession();
		
		UserReview review = getUserReview(name, targetId);
		if(null == review) {
			review = new UserReview();
			
			UserExtension reviewer = users.getExtended(name);
			reviewer.getReviewsWritten().add(review);
			review.setReviewer(reviewer);
			
			UserExtension reviewee = users.getExtended(targetId);
			reviewee.getReviewsReceived().add(review);
			review.setReviewee(reviewee);
			
			//TODO reviewer.getUsersForReview().remove(reviewee);
		}
		
		review.setScore(score);
		review.setJustification(clean);
		review.setTime(new Date());
		
		session.saveOrUpdate(review);
		
		return review;
	}
	
	@Override
	public Collection<BusinessReview> getBusinessReviews(long businessId) {
		BusinessProfile business = (BusinessProfile) sessions.getCurrentSession().get(BusinessProfile.class, businessId);
		Hibernate.initialize(business.getReviews());
		return business.getReviews();
	}
	
	@Override
	public Collection<UserReview> getUserReviews(long userId) {
		UserExtension user = users.getExtended(userId, Initializable.reviewsreceived);
		return user.getReviewsReceived();
	}

	@Override
	public void noReview(String username, long businessId) {
		UserExtension user = users.getExtended(username);
		for(Iterator<BusinessProfile> i = user.getForReview().iterator(); i.hasNext();) {
			BusinessProfile business = i.next();
			if(businessId == business.getId()) {
				i.remove();
			}
		}
	}

	@Override
	public void neverReview(String username, long businessId) {
		UserExtension user = users.getExtended(username);
		user.getNeverReview().add(businessId);
		for(Iterator<BusinessProfile> i = user.getForReview().iterator(); i.hasNext();) {
			BusinessProfile business = i.next();
			if(businessId == business.getId()) {
				i.remove();
			}
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<TopTenList> getToptens(int start, int howmany, String orderColumn) {
		Session session = sessions.getCurrentSession();
		Criteria criteria = session.createCriteria(TopTenList.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(null != orderColumn) {
			criteria.addOrder(Order.desc(orderColumn));
		}
		
		if(start > -1 && howmany > 0) {
			criteria.setFirstResult(start);
			criteria.setMaxResults(howmany);
		}
		
		//we only need to initialize the attachment of the winning candidate, really, since this is for the sidebar
		Collection<TopTenList> toptens = criteria.list();
		for(TopTenList list : toptens) {
			if(list.getLeader().getAttachmentType() != AttachmentType.none) {
				initializeAttachment(list.getLeader());
			}
			
			if(log.isDebugEnabled()) {
				log.debug("List id: {}, title: {}, candidates: {}, leader: {}", list.getId(), list.getTitle(), list.getCandidates(), list.getLeader());
			}
		}
		return toptens;
	}

	@Override
	public Collection<TopTenList> getUserToptens(String name) {
		return users.getExtended(name).getCreatedToptens();
	}

	@Override
	public TopTenList getTopTenList(long topTenId) {
		TopTenList list = (TopTenList) sessions.getCurrentSession().get(TopTenList.class, topTenId);
		for(TopTenCandidate candidate : list.getCandidates()) {
			initializeAttachment(candidate);
		}
		return list;
	}

	@Override
	public TopTenList createTopTenList(String name, String title) {
		UserExtension user = users.getExtended(name);
		
		TopTenList newlist = new TopTenList();
		newlist.setCreator(user);
		newlist.setTime(new Date());
		newlist.setTitle(title);

		user.getCreatedToptens().add(newlist);
		
		sessions.getCurrentSession().save(newlist);
		
		return newlist;
	}

	@Override
	public TopTenCandidate createTopTenCandidate(String name, long topTenId, String title) {
		UserExtension user = users.getExtended(name);
		TopTenList list = getTopTenList(topTenId);
		
		TopTenCandidate candidate = new TopTenCandidate();
		candidate.setCreator(user);
		candidate.setList(list);
		candidate.setTitle(title);
		
		if(list.getCandidates().contains(candidate)) {
			return null;
		}
		list.getCandidates().remove(candidate);
		list.getCandidates().add(candidate);
		
		sessions.getCurrentSession().save(candidate);
		
		return candidate;
	}
	
	@Override
	public void toptenVote(String name, long candidateId) {
		log.debug("Peristing vote from {} for candidate with id {}", name, candidateId);
		
		UserExtension user = users.getExtended(name);
		TopTenCandidate candidate = (TopTenCandidate) sessions.getCurrentSession().get(TopTenCandidate.class, candidateId);
		
		log.debug("Found {} and {}", user, candidate);
		
		TopTenList parent = candidate.getList();
		boolean fresh = true;
		for(TopTenCandidate competitor : parent.getCandidates()) {
			if(competitor.getVoters().remove(user)) { //transfer vote
				competitor.setVotes(competitor.getVoters().size());
				fresh = false;
				break;
			}
		}
		if(fresh) {
			log.debug("Fresh vote, incrementing list total votes. Before increment: {}", parent.getTotalVotes());
			parent.setTotalVotes(parent.getTotalVotes() + 1);
			log.debug("After increment: {}", parent.getTotalVotes());
		} else {
			log.debug("Transfer vote, no increment");
		}
		
		candidate.getVoters().add(user);
		candidate.setVotes(candidate.getVoters().size());
		user.getVotes().add(candidate);
	}

	private void initializeAttachment(TopTenCandidate candidate) {
		if(null == candidate.getAttachmentType()) {
			return;
		}
		
		switch(candidate.getAttachmentType()) {
		case business:
			candidate.setAttachment(businesses.get(candidate.getAttachmentId()));
			break;
		}
	}
	
	@Override
	public boolean addToWishlist(String name, WishType type, long id) {
		log.debug("Adding item of type {} to {}'s wishlist id: {}", type, name, id);
		
		UserExtension user = users.getExtended(name);
		if(user.getWishlist().size() >= wishlistMaxItems) {
			return false;
		}
		
		Wish wish = new Wish();
		switch(type) {
		case product:
			Product product = businesses.getProduct(id);
			wish.setProduct(product);
			break;
		case buyandsell:
			BuyAndSellItem tradeItem = trade.get(id);
			wish.setBuyAndSellItem(tradeItem);
			break;
		default:
			throw new IllegalStateException("Unknown type: " + type);
		}
		
		wish.setTime(new Date());
		wish.setType(type);
		wish.setWisher(user);
		
		List<Wish> wishlist = user.getWishlist();
		
		wishlist.remove(wish);
		wishlist.add(wish);
		for(int i = 0; i < wishlist.size(); i++) {
			wishlist.get(i).setWishOrder(i);
		}
		
		sessions.getCurrentSession().save(wish);
		return true;
	}

	@Override
	public Object[] getBusinessReviewStats(long targetId, ReviewType type) {
		ProjectionList projections = Projections.projectionList()
				.add(Projections.rowCount())
				.add(Projections.avg("score"));
		
		Object[] results = (Object[]) sessions.getCurrentSession().createCriteria(BusinessReview.class)
			.createAlias("reviewed", "business")
			.add(Restrictions.eq("business.id", targetId))
			.setProjection(projections)
			.uniqueResult();
		
		log.debug("Requested business review stats for id {}, got {}", targetId, Arrays.asList(results));
		
		return results;
	}

	@Override
	public Object[] getUserReviewStats(long targetId, ReviewType type) {
		ProjectionList projections = Projections.projectionList()
				.add(Projections.rowCount())
				.add(Projections.avg("score"));
		
		Object[] results = (Object[]) sessions.getCurrentSession().createCriteria(UserReview.class)
			.createAlias("reviewee", "reviewee")
			.add(Restrictions.eq("reviewee.id", targetId))
			.setProjection(projections)
			.uniqueResult();
		
		log.debug("Requested user review stats for id {}, got {}", targetId, Arrays.asList(results));
		
		return results;
	}

	@Override
	public long getBusinessTopTenListId(long groupId) {
		Session session = sessions.getCurrentSession();
		BusinessGroup businessGroup = (BusinessGroup) session.get(BusinessGroup.class, groupId);
		if(null == businessGroup.getTopTenList()) {
			UserExtension user = users.getDefault();

			TopTenList newlist = new TopTenList();
			newlist.setCreator(user);
			newlist.setTime(new Date());
			newlist.setTitle("Top " + StringUtils.capitalize(businessGroup.getName()));
			
			for(BusinessProfile business : businessGroup.getBusinesses()) {
				TopTenCandidate candidate = new TopTenCandidate(business);
				candidate.setList(newlist);
				candidate.setCreator(user);
				newlist.getCandidates().add(candidate);
			}
			
			user.getCreatedToptens().add(newlist);
			businessGroup.setTopTenList(newlist);
			
			session.save(newlist);
		}
		
		return businessGroup.getTopTenList().getId();
	}
}
