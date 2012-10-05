package com.baldwin.indgte.pers‪istence.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.User;

@Repository 
@Transactional
public class BusinessDaoImpl implements BusinessDao {

	static Logger log = LoggerFactory.getLogger(BusinessDao.class);
	
	private final static String BUSINESS_DOMAIN = "domain";
	
	private final static String USER_USERNAME = "username";
	private final static String USER_PROVIDERID = "providerId";
	private final static String USER_PROVIDERID_SPRINGSOCSEC = "springSocialSecurity";
	
	@Autowired 
	private SessionFactory sessions;
	
	@Override
	public BusinessProfile get(String domain) {
		return (BusinessProfile) sessions.getCurrentSession()
				.createCriteria(BusinessProfile.class)
				.add(Restrictions.like(BUSINESS_DOMAIN, domain))
				.uniqueResult();
	}

	@Override
	public void create(BusinessProfile profile, String ownerName) {
		Session session = sessions.getCurrentSession();
		
		User owner = (User) session.createCriteria(User.class)
				.add(Restrictions.like(USER_USERNAME, ownerName))
				.add(Restrictions.like(USER_PROVIDERID, USER_PROVIDERID_SPRINGSOCSEC))
				.uniqueResult();
		
		log.info("Creating business {} for owner {}", profile.getDomain(), owner.getDisplayName());
		
		owner.getBusinesses().add(profile);
		profile.setOwner(owner);
		
		session.update(owner);
		session.save(profile);
	}

	@Override
	public void update(BusinessProfile profile) {
		sessions.getCurrentSession().update(profile);
	}
	
	@Override
	public void delete(BusinessProfile profile) {
		sessions.getCurrentSession().delete(profile);
	}
}
