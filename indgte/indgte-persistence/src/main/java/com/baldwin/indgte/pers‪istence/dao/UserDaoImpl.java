package com.baldwin.indgte.pers‪istence.dao;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.constants.Initializable;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.model.UserExtension;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {
	static Logger log = LoggerFactory.getLogger(UserDao.class);
	
	@Autowired
	private SessionFactory sessions;
	
	@Value("${default.username}")
	private String defaultUsername;
	
	@Override
	public User getByUsername(String username, String providerId) {
		return (User)sessions.getCurrentSession()
			.createCriteria(User.class)
			.add(Restrictions.eq(TableConstants.USER_USERNAME, username))
			.add(Restrictions.eq(TableConstants.USER_PROVIDERID, providerId))
			.uniqueResult();
	}

//	@Deprecated
//	private User getSpring(String userId) {
//		log.debug("Trying to get SSS User by providerUserId = {}", userId);
//		
//		User user =  (User)sessions.getCurrentSession()
//				.createCriteria(User.class)
//				.add(Restrictions.eq(TableConstants.USER_USERNAME, userId))
//				.add(Restrictions.eq(TableConstants.USER_PROVIDERID, TableConstants.USER_PROVIDERID_SPRINGSOCSEC))
//				.uniqueResult();
//		
//		log.debug("Found {}", user);
//		return user;
//	}
	
	private User getFacebook(String username) {
		log.debug("Trying to get Facebook User by userId [{}]", username);
		
		User user =  (User)sessions.getCurrentSession()
				.createCriteria(User.class)
				.add(Restrictions.eq(TableConstants.USER_USERNAME, username))
				.add(Restrictions.eq(TableConstants.USER_PROVIDERID, TableConstants.USER_PROVIDERID_FACEBOOK))
				.uniqueResult();
		
		log.debug("Found {}", user);
		return user;
	}

	private User getTwitter(String username) {
		log.debug("Trying to get Twitter User by username [{}]", username);
		
		User user =  (User)sessions.getCurrentSession()
				.createCriteria(User.class)
				.add(Restrictions.eq(TableConstants.USER_USERNAME, username))
				.add(Restrictions.eq(TableConstants.USER_PROVIDERID, TableConstants.USER_PROVIDERID_TWITTER))
				.uniqueResult();
		
		log.debug("Found {}", user);
		return user;
	}
	
	@Override
	public UserExtension getExtended(String username) {
		return getExtended(username, true);
	}
	
	/**
	 * Will return null if createIfAbsent is false
	 */
	@Override
	public UserExtension getExtended(String username, boolean createIfAbsent) {
		Session session = sessions.getCurrentSession();
		UserExtension userExtension = (UserExtension) session.createCriteria(UserExtension.class)
				.createAlias(TableConstants.USEREXTENSION_USER, "user")
				.add(Restrictions.eq("user.username", username))
				.uniqueResult();
		
		if(createIfAbsent && null == userExtension) {
			userExtension = createUserExtension(username);
		}
		
		return userExtension;
	}
	
	private UserExtension createUserExtension(String username) {
		User user = getFacebook(username);
		if(null == user) {
			user = getTwitter(username);
		}
		
		UserExtension extension = new UserExtension(user);
		user.setExtension(extension);
		sessions.getCurrentSession().save(extension);
		
		//subscribe user to himself
		extension.getUserSubscriptions().add(extension.getId());
		
		//subscribe user to admins
		//TODO
		
		return extension;
	}
	
	@Override 
	public UserExtension getExtended(long userId, Initializable... initializables) {
		UserExtension userExtension = (UserExtension) sessions.getCurrentSession().get(UserExtension.class, userId);
		return initialize(userExtension, initializables);
		
//		return getExtended(null, userId, initializables);
	}

	@Override
	public UserExtension getExtended(String username, Initializable... initializables) {
		UserExtension userExtension = getExtended(username);
		return initialize(userExtension, initializables);
	}
	
	//TODO
	@SuppressWarnings("unused")
	private UserExtension getExtended(String username, long id, Initializable... initializables) {
		Criteria c = sessions.getCurrentSession().createCriteria(User.class);
		
		if(username != null) {
			c.add(Restrictions.eq(TableConstants.USER_USERNAME, username));
		} else {
			c.add(Restrictions.eq(TableConstants.ID, id));
		}
		
		for(Initializable initializable : initializables) {
			switch(initializable) {
			case wishlist:
				c.setFetchMode(TableConstants.USER_WISHLIST, FetchMode.JOIN);
				break;
			case reviewqueue:
				c.setFetchMode(TableConstants.USER_REVIEWQUEUE, FetchMode.JOIN);
				break;
			case reviewsreceived:
				c.setFetchMode(TableConstants.USER_REVIEWSRECEIVED, FetchMode.JOIN);
				break;
			case toptenvotes:
				c.setFetchMode(TableConstants.USER_TOPTENVOTES, FetchMode.JOIN);
				break;
			case watchedtags:
				c.setFetchMode(TableConstants.USER_WATCHEDTAGS, FetchMode.JOIN);
				break;
			case buyandsellitems:
				c.setFetchMode(TableConstants.USER_BUYSELLITEMS, FetchMode.JOIN);
				break;
			case businesses:
				c.setFetchMode(TableConstants.USER_BUSINESSES, FetchMode.JOIN);
				break;
			}
		}

		return (UserExtension) c.uniqueResult();
	}
	
	private UserExtension initialize(UserExtension userExtension, Initializable... initializables) {
		for(Initializable initializable : initializables) {
			switch(initializable) {
			case wishlist:
				Hibernate.initialize(userExtension.getWishlist());
				break;
			case reviewqueue:
				Hibernate.initialize(userExtension.getForReview());
				break;
			case reviewsreceived:
				Hibernate.initialize(userExtension.getReviewsReceived());
				break;
			case toptenvotes:
				Hibernate.initialize(userExtension.getVotes());
				break;
			case watchedtags:
				Hibernate.initialize(userExtension.getWatchedTags());
				break;
			case buyandsellitems:
				Hibernate.initialize(userExtension.getBuyAndSellItems());
				break;
			case businesses:
				Hibernate.initialize(userExtension.getBusinesses());
				break;
			case subscriptions:
				Hibernate.initialize(userExtension.getUserSubscriptions());
				Hibernate.initialize(userExtension.getBusinessSubscriptions());
				break;
			}
		}
		return userExtension;
	}

	@Override
	public UserExtension getDefault(Initializable... initializables) {
		return getExtended(defaultUsername, initializables);
	}

	/**
	 * This method depends on springSocialSecurity provided imageUrl being null.
	 */
	@Override
	public String getImageUrl(String username) {
		return (String) sessions.getCurrentSession().createCriteria(User.class)
			.add(Restrictions.eq(TableConstants.USER_USERNAME, username))
			.add(Restrictions.isNotNull(TableConstants.USER_IMAGEURL))
			.setProjection(Projections.property(TableConstants.USER_IMAGEURL))
			.uniqueResult();
	}

}
