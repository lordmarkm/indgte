package com.baldwin.indgte.pers‪istence.dao;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.model.User;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

	private final static String USER_USERNAME = "username";
	private final static String USER_PROVIDERID = "providerId";
	
	@Autowired
	private SessionFactory sessions;
	
	@Override
	public User getByUsername(String username, String providerId) {
		return (User)sessions.getCurrentSession()
			.createCriteria(User.class)
			.add(Restrictions.like(USER_USERNAME, username))
			.add(Restrictions.like(USER_PROVIDERID, providerId))
			.uniqueResult();
	}

}
