package com.baldwin.indgte.pers‪istence.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.model.User;

@Repository
@Transactional
public class PostDaoImpl implements PostDao {

	static Logger log = LoggerFactory.getLogger(PostDaoImpl.class);
	
	@Autowired
	private SessionFactory sessions;
	
	@Autowired
	private UserDao users;
	
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
	public Set<Post> getById(long posterId, PostType type, int start, int howmany) {
		@SuppressWarnings("unchecked")
		List<Post> posts = sessions.getCurrentSession().createCriteria(Post.class)
				.add(Restrictions.eq("posterId", posterId))
				.add(Restrictions.eq("type", type))
				.list();
		
		return new LinkedHashSet<Post>(posts);
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
}
