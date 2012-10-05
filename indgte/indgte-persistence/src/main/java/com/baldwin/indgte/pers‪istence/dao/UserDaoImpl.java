package com.baldwin.indgte.pers‪istence.dao;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.model.User;

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
	public User getBySSSUserId(String userId) {
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
}
