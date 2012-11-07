package com.baldwin.indgte.pers‪istence.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Override
	public User getByUsername(String username, String providerId) {
		return (User)sessions.getCurrentSession()
			.createCriteria(User.class)
			.add(Restrictions.eq(TableConstants.USER_USERNAME, username))
			.add(Restrictions.eq(TableConstants.USER_PROVIDERID, providerId))
			.uniqueResult();
	}

	@Override
	public User getSpring(String userId) {
		log.debug("Trying to get SSS User by providerUserId = {}", userId);
		
		User user =  (User)sessions.getCurrentSession()
				.createCriteria(User.class)
				.add(Restrictions.eq(TableConstants.USER_USERNAME, userId))
				.add(Restrictions.eq(TableConstants.USER_PROVIDERID, TableConstants.USER_PROVIDERID_SPRINGSOCSEC))
				.uniqueResult();
		
		log.debug("Found {}", user);
		return user;
	}

	@Override
	public User getFacebook(String userId) {
		log.debug("Trying to get Facebook User by userId [{}]", userId);
		
		User user =  (User)sessions.getCurrentSession()
				.createCriteria(User.class)
				.add(Restrictions.eq(TableConstants.USER_USERNAME, userId))
				.add(Restrictions.eq(TableConstants.USER_PROVIDERID, TableConstants.USER_PROVIDERID_FACEBOOK))
				.uniqueResult();
		
		log.debug("Found {}", user);
		return user;
	}

	@Override
	public UserExtension getExtended(String username) {
		Session session = sessions.getCurrentSession();
		UserExtension userExtension = (UserExtension) session.createCriteria(UserExtension.class)
				.createAlias(TableConstants.USEREXTENSION_USER, "user")
				.add(Restrictions.eq("user.username", username))
				.uniqueResult();
		
		if(null == userExtension) {
			User user = getFacebook(username);
			userExtension = new UserExtension(user);
			user.setExtension(userExtension);
			
			session.save(userExtension);
		}
		
		return userExtension;
	}
	
	@Override 
	public UserExtension getExtended(long userId, Initializable... initializables) {
		UserExtension userExtension = (UserExtension) sessions.getCurrentSession().get(UserExtension.class, userId);
		return initialize(userExtension, initializables);
	}

	@Override
	public UserExtension getExtended(String username, Initializable... initializables) {
		UserExtension userExtension = getExtended(username);
		return initialize(userExtension, initializables);
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
			}
		}
		return userExtension;
	}
}
