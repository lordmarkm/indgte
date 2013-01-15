package com.baldwin.indgte.pers‪istence.dao;

import static com.baldwin.indgte.pers‪istence.dao.TableConstants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
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
import com.baldwin.indgte.persistence.constants.Background;
import com.baldwin.indgte.persistence.constants.Initializable;
import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.constants.ReviewType;
import com.baldwin.indgte.persistence.constants.Theme;
import com.baldwin.indgte.persistence.constants.WishType;
import com.baldwin.indgte.persistence.model.BillingTransaction;
import com.baldwin.indgte.persistence.model.BusinessGroup;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.BusinessReview;
import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.model.Product;
import com.baldwin.indgte.persistence.model.Review;
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
	public Collection<Post> getPosts(int start, int howmany, String tagFilter) {
		Criteria crit = sessions.getCurrentSession().createCriteria(Post.class)
				.setFirstResult(start)
				.setMaxResults(howmany)
				.addOrder(Order.desc(POST_TIME));
		if(null != tagFilter && tagFilter.length()>0) {
			crit.add(Restrictions.like(TAGS, "% " + tagFilter + " %"));
		}
		return crit.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<Post> getPostsByPopularity(int start, int howmany, String tagFilter) {
		long startTime = System.currentTimeMillis();
		
		String queryString = "select *, (p.likes*10 + p.comments + 3) * 10/(timestampdiff(HOUR,p.postTime,now())^1.4) as 'quotient'" +
				" from posts p order by quotient desc, postId desc limit :start, :howmany";
		
		if(null != tagFilter && tagFilter.length()>0) {
			queryString =  "select *, (p.likes*10 + p.comments + 3) * 10/(timestampdiff(HOUR,p.postTime,now())^1.4) as 'quotient'" +
					" from posts p where tags like '% " + tagFilter + " %' order by quotient desc, postId desc limit :start, :howmany";
		}
		
		Query query = sessions.getCurrentSession().createSQLQuery(queryString)
				.addEntity(Post.class)
				.setInteger("start", start).setInteger("howmany", howmany);
		
		List<Post> results = query.list();
		
		log.debug("Find posts by popularity query completed in {} ms", System.currentTimeMillis() - startTime);

		return results;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Post> getSubposts(String username, int start, int howmany, String tagFilter) {
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
		
		Criteria crit = session.createCriteria(Post.class)
			.add(orJunction)
			.setFirstResult(start)
			.setMaxResults(howmany)
			.addOrder(Order.desc(TableConstants.POST_TIME));
		
		if(null != tagFilter && tagFilter.length()>0) {
			crit.add(Restrictions.like(TAGS, "% " + tagFilter + " %"));
		}
		
		return crit	.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Post> getBusinessGroupPosts(long groupId, int start, int howmany, String tagFilter) {
		String query = "select id from BusinessProfile where businessGroup.id = :groupId";
		
		List<Long> businessIds = sessions.getCurrentSession().createQuery(query)
			.setLong("groupId", groupId)
			.list();
		
		Criteria crit = sessions.getCurrentSession().createCriteria(Post.class)
			.add(Restrictions.eq(TableConstants.TYPE, PostType.business))
			.add(Restrictions.in(TableConstants.POST_POSTERID, businessIds))
			.setFirstResult(start).setMaxResults(howmany)
			.addOrder(Order.desc(TableConstants.POST_TIME));

		if(null != tagFilter && tagFilter.length()>0) {
			crit.add(Restrictions.like(TAGS, "% " + tagFilter + " %"));
		}
		
		return crit.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<Post> getById(long posterId, PostType type, int start, int howmany, String tagFilter) {
		Criteria crit = sessions.getCurrentSession().createCriteria(Post.class)
				.add(Restrictions.eq("posterId", posterId))
				.add(Restrictions.eq("type", type))
				.setFirstResult(start)
				.setMaxResults(howmany)
				.addOrder(Order.desc(TableConstants.POST_TIME));
		
		if(null != tagFilter && tagFilter.length()>0) {
			crit.add(Restrictions.like(TAGS, "% " + tagFilter + " %"));
		}
		
		return crit.list();
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
		UserExtension user = users.getExtended(username);
		user.getUserSubscriptions().add(id);
	}

	@Override
	public void unsubscribeFromUser(String username, Long id) {
		UserExtension user = users.getExtended(username);
		user.getUserSubscriptions().remove(id);
	}
	
	@Override
	public void subscribeToBusiness(String username, Long id) {
		UserExtension user = users.getExtended(username);
		user.getBusinessSubscriptions().add(id);
	}
	
	@Override
	public void unsubscribeFromBusiness(String username, Long id) {
		UserExtension user = users.getExtended(username);
		user.getBusinessSubscriptions().remove(id);
	}
	
	@Override
	public int subscount(PostType type, Long id) {
		switch(type) {
		case business:
			log.debug("Querying for subscriptions to business with id {}", id);
			Long count = (Long) sessions.getCurrentSession().createQuery("select count(*) from UserExtension u where :businessId in elements(u.businessSubscriptions)")
				.setLong("businessId", id)
				.uniqueResult();
			return null == count ? 0 : count.intValue();
		default:
			log.error("Unsupported subs type: {}", type);
			return -1;
		}
	}
	
	private Map<PostType, Set<Long>> getSubscriptions(String name, PostType[] types) {
		log.debug("Getting {} subscriptions for {}", types, name);
		
		UserExtension user = users.getExtended(name);
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
			return users.getExtended(name).getUserSubscriptions().contains(targetId);
		case business:
			return users.getExtended(name).getBusinessSubscriptions().contains(targetId);
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
		BusinessProfile business = businesses.get(businessId);

		BusinessReview review = getBusinessReview(name, businessId);
		if(null == review) {
			review = new BusinessReview();
			
			UserExtension reviewer = users.getExtended(name);
			reviewer.getBusinessReviews().add(review);
			
			business.getReviews().add(review);
			
			review.setReviewer(reviewer);
			review.setReviewed(business);
			
			reviewer.getForReview().remove(business);
		}
		
		review.setScore(score);
		review.setJustification(justification);
		review.setTime(new Date()); //last updated
		
		session.saveOrUpdate(review);
		
		double total = 0;
		for(Review r : business.getReviews()) {
			total += r.getScore();
		}

		double avg = Math.round(2*(total/business.getReviews().size())) / 2d;
		business.setAverageReviewScore(avg);
		
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
	public Collection<TopTenList> getToptens(int start, int howmany, String orderColumn, int initialize) {
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
			for(int i = 0; i < initialize && i < list.getCandidates().size(); ++i) {
				TopTenCandidate candidate = list.getOrdered().get(i);
				if(candidate.getAttachmentType() != AttachmentType.none) {
					initializeAttachment(candidate);
				}	
			}
		}
		return toptens;
	}

	@Override
	public Collection<TopTenList> getUserToptens(String name, int initialize) {
		Collection<TopTenList> toptens = users.getExtended(name).getCreatedToptens();
		for(TopTenList list : toptens) {
			for(int i = 0; i < initialize && i < list.getCandidates().size(); ++i) {
				TopTenCandidate candidate = list.getOrdered().get(i);
				if(candidate.getAttachmentType() != AttachmentType.none) {
					initializeAttachment(candidate);
				}	
			}
		}
		return toptens;
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
	public void saveTopTenList(String name, TopTenList list) {
		log.debug("Saving top ten list with title: {}, # of candidates: {}, creator: {}", list.getTitle(), list.getCandidates().size(), name);

		UserExtension user = users.getExtended(name);
		
		log.debug("Before filter: " + list.getCandidates().size());
		for(Iterator<TopTenCandidate> i = list.getCandidates().iterator(); i.hasNext();) {
			TopTenCandidate candidate = i.next();
			candidate.setList(list);
			candidate.setCreator(user);
			if(candidate.getTitle() == null && candidate.getAttachmentType() == AttachmentType.none) {
				//Workaround for when first candidate is removed in frontend
				log.debug("Invalid entry. Removing.");
				i.remove();
				continue;
			} 
		}
		log.debug("After filter: " + list.getCandidates().size());
		
		list.setTime(new Date());
		list.setCreator(user);
		user.getCreatedToptens().add(list);
		
		sessions.getCurrentSession().save(list);
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
	public TopTenCandidate toptenVote(String name, long candidateId) {
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
		
		return candidate;
	}

	@Override
	public void initializeAttachment(TopTenCandidate candidate) {
		if(null == candidate || null == candidate.getAttachmentType()) {
			return;
		}
		
		switch(candidate.getAttachmentType()) {
		case business:
			candidate.setAttachment(businesses.get(candidate.getAttachmentId()));
			break;
		case category:
			candidate.setAttachment(businesses.getCategory(candidate.getAttachmentId()));
			break;
		case product:
			candidate.setAttachment(businesses.getProduct(candidate.getAttachmentId()));
			break;
		case imgur:
			candidate.setAttachment(getImgur(candidate.getAttachmentId()));
			break;
		}
	}
	
	private Imgur getImgur(long id) {
		return (Imgur) sessions.getCurrentSession().createCriteria(Imgur.class)
				.add(Restrictions.eq(TableConstants.IMGUR_ID, id))
				.uniqueResult();
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
			log.info("{} has added {} to wishlist.", name, product.getName());
			break;
		case buyandsell:
			BuyAndSellItem tradeItem = trade.get(id);
			wish.setBuyAndSellItem(tradeItem);
			log.info("{} has added {} to wishlist.", name, tradeItem.getName());
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

	@Override
	public void attachImageToCandidate(String name, long candidateId,	Imgur imgur) {
		log.debug("Imgur {} will be attached to TopTenCandidate with id {}, requested by {}", imgur, candidateId, name);
		Session session = sessions.getCurrentSession();
		
		imgur.setUploaded(new Date());
		session.save(imgur);
		
		TopTenCandidate candidate = (TopTenCandidate) session.createCriteria(TopTenCandidate.class)
				.add(Restrictions.eq(TableConstants.TOPTEN_CANDIDATE_ID, candidateId))
				.uniqueResult();
		
		candidate.setAttachmentId(imgur.getId());
		candidate.setAttachmentType(AttachmentType.imgur);
	}

	@Override
	public Imgur addDescriptionToCandidate(long candidateId, String description) {
		TopTenCandidate candidate = (TopTenCandidate) sessions.getCurrentSession().get(TopTenCandidate.class, candidateId);
		if(candidate.getAttachmentType() != AttachmentType.imgur) {
			throw new IllegalStateException("Trying to attach description to non-imgur attachment.");
		}
		initializeAttachment(candidate);
		Imgur imgur = (Imgur) candidate.getAttachment();
		imgur.setDescription(description);
		return imgur;
	}

	@Override
	public String addDescriptionToList(long listId, String description) {
		TopTenList list = (TopTenList) sessions.getCurrentSession().get(TopTenList.class, listId);
		list.setDescription(description);
		return description;
	}

	/**
	 * Optional operation, if entity already exists in TopTen list, candidate set remains unchanged.
	 * <p>See {@link Set#add(Object)}
	 */
	@Override
	public void addEntityToList(String name, long listId, AttachmentType type,	long attachmentId) {
		UserExtension creator = users.getExtended(name);
		TopTenList list = (TopTenList) sessions.getCurrentSession().get(TopTenList.class, listId);
		TopTenCandidate candidate = new TopTenCandidate();
		candidate.setList(list);
		candidate.setAttachmentType(type);
		candidate.setAttachmentId(attachmentId);
		candidate.setCreator(creator);
		
		boolean fresh = list.getCandidates().add(candidate);
		if(fresh) {
			log.debug("New entity added to topten list");
		} else {
			log.debug("Entity already existed in list. No action taken.");
		}
		
		log.debug("Attaching {} with id {} to list {}, requested by {}",
				type, attachmentId, list.getTitle(), name);
		sessions.getCurrentSession().save(candidate);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<TopTenList> getAllLists() {
		return sessions.getCurrentSession().createCriteria(TopTenList.class)
					.addOrder(Order.asc(TableConstants.TOPTEN_TITLE))
					.list();
	}

	@Override
	public void changetheme(String name, Theme newtheme) {
		UserExtension user = users.getExtended(name);
		user.getAppearanceSettings().setTheme(newtheme);
	}
	
	@Override
	public void changebg(String name, Background newBg) {
		UserExtension user = users.getExtended(name);
		user.getAppearanceSettings().setBackground(newBg);
	}

	@Override
	public Post getPost(long postId) {
		return (Post) sessions.getCurrentSession().get(Post.class, postId);
	}

	@Override
	public Review getReview(ReviewType type, long reviewId, boolean getReactors) {
		switch(type) {
		case business:
			Criteria bCriteria = sessions.getCurrentSession().createCriteria(BusinessReview.class)
				.add(Restrictions.eq(TableConstants.ID, reviewId));
			
			if(getReactors) {
				bCriteria.setFetchMode(TableConstants.REVIEW_AGREERS, FetchMode.JOIN)
					.setFetchMode(TableConstants.REVIEW_DISAGREERS, FetchMode.JOIN);
			}
			
			return (Review) bCriteria.uniqueResult();
		case user:
			Criteria uCriteria = sessions.getCurrentSession().createCriteria(UserReview.class)
				.add(Restrictions.eq(TableConstants.ID, reviewId));
			if(getReactors) {
				uCriteria.setFetchMode(TableConstants.REVIEW_AGREERS, FetchMode.JOIN)
					.setFetchMode(TableConstants.REVIEW_DISAGREERS, FetchMode.JOIN);
			}
			
			return (Review) uCriteria.uniqueResult();
		default:
			return null;
		}
	}

	@Override
	public Review reviewReact(String name, ReviewType type, String mode, long reviewId) {
		log.info("{} {}s with {} review with id {}", name, mode, type, reviewId);
		
		UserExtension user = users.getExtended(name);
		Review review = getReview(type, reviewId, false);
		
		switch(type) {
		case business:
			BusinessReview bReview = (BusinessReview)review;
			switch(mode) {
			case "agree":
				bReview.getDisagreers().remove(user);
				bReview.getAgreers().add(user);
				bReview.setDisagreeCount(bReview.getDisagreers().size());
				bReview.setAgreeCount(bReview.getAgreers().size());
				return bReview;
			case "disagree":
				bReview.getAgreers().remove(user);
				bReview.getDisagreers().add(user);
				bReview.setAgreeCount(bReview.getAgreers().size());
				bReview.setDisagreeCount(bReview.getDisagreers().size());
				return bReview;
			default:
				return null;
			}
		case user:
			UserReview uReview = (UserReview)review;
			switch(mode) {
			case "agree":
				uReview.getDisagreers().remove(user);
				uReview.getAgreers().add(user);
				uReview.setDisagreeCount(uReview.getDisagreers().size());
				uReview.setAgreeCount(uReview.getAgreers().size());
				return uReview;
			case "disagree":
				uReview.getAgreers().remove(user);
				uReview.getDisagreers().add(user);
				uReview.setAgreeCount(uReview.getAgreers().size());
				uReview.setDisagreeCount(uReview.getDisagreers().size());
				return uReview;
			default:
				return null;
			}
		}
		return null;
	}
	
	public BusinessReview getBusinessReview(long reviewId) {
		return (BusinessReview) sessions.getCurrentSession().get(BusinessReview.class, reviewId);
	}
	
	public UserReview getUserReview(long reviewId) {
		return (UserReview) sessions.getCurrentSession().get(UserReview.class, reviewId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getBusinessSubscriptionDomains(String username) {
		UserExtension user = users.getExtended(username);
		if(user.getBusinessSubscriptions().size() == 0) return Collections.<String>emptyList();
		return sessions.getCurrentSession().createCriteria(BusinessProfile.class)
					.add(Restrictions.in(TableConstants.ID, user.getBusinessSubscriptions()))
					.setProjection(Projections.property("domain"))
					.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> getUserSubscripionSummaries(String username) {
		UserExtension user = users.getExtended(username);
		if(user.getUserSubscriptions().size() == 0) return Collections.<User>emptyList();
		return (List<User>) sessions.getCurrentSession().createCriteria(User.class) //relies on User and UserExtension having the same id due to @MapsId
				.add(Restrictions.in(TableConstants.ID, user.getUserSubscriptions()))
				.list();
	}

	Random random = new Random();
	@Override
	public Post getRandomFeaturedPost() {
		Date now = new Date();
		
		@SuppressWarnings("unchecked")
		List<Post> featuredPosts = (List<Post>) sessions.getCurrentSession().createCriteria(Post.class)
			.add(Restrictions.le("featureStart", now))
			.add(Restrictions.ge("featureEnd", now))
			.list();
		
		Post featuredPost;
		if(featuredPosts.size() > 0) {
			int size = featuredPosts.size();
			int rand = random.nextInt(size);
			log.debug("Featured posts: {}, returning {}-th element", size, rand);
			featuredPost = featuredPosts.get(rand);
			return featuredPost;
		} else {
			return null;
		}
	}
	
	@Override
	public Post getBusinessGroupFeaturedPost(long groupId) {
		String query = "select id from BusinessProfile where businessGroup.id = :groupId";
		
		@SuppressWarnings("unchecked")
		List<Long> businessIds = sessions.getCurrentSession().createQuery(query)
			.setLong("groupId", groupId)
			.list();

		Date now = new Date();
		@SuppressWarnings("unchecked")
		List<Post> featuredPosts = (List<Post>) sessions.getCurrentSession().createCriteria(Post.class)
			.add(Restrictions.le("featureStart", now))
			.add(Restrictions.ge("featureEnd", now))
			.add(Restrictions.eq(TableConstants.TYPE, PostType.business))
			.add(Restrictions.in(TableConstants.POST_POSTERID, businessIds))
			.list();
		
		Post featuredPost;
		if(featuredPosts.size() > 0) {
			int size = featuredPosts.size();
			int rand = random.nextInt(size);
			log.debug("Featured posts: {}, returning {}-th element", size, rand);
			featuredPost = featuredPosts.get(rand);
			return featuredPost;
		} else {
			return null;
		}
	}

	@Override
	public void deletepost(long id) {
		Session session = sessions.getCurrentSession();
		
		Post p = getPost(id);
		
		List<BillingTransaction> transactions = p.getTransactions();
		for(BillingTransaction b : transactions) {
			b.getDetails().setFeaturedPost(null);
			session.update(b);
		}
		
		session.delete(p);
	}

	@Override
	public void deleteReview(ReviewType type, long reviewId) {
		Session session = sessions.getCurrentSession();
		
		String deleteNotifsQuery = "delete from ReviewReactNotification where reviewType = :type and reviewId = :reviewId";
		session.createQuery(deleteNotifsQuery)
			.setParameter(TYPE, type)
			.setLong(REVIEWREACT_REVIEWID, reviewId)
			.executeUpdate();
		
		switch(type) {
		case business:
			BusinessReview bReview = (BusinessReview) getReview(type, reviewId, false);
			session.delete(bReview);
			break;
		case user:
			UserReview uReview = (UserReview) getReview(type, reviewId, false);
			session.delete(uReview);
			break;
		default:
			throw new IllegalArgumentException("Invalid review type: " + type);
		}
	}

}
