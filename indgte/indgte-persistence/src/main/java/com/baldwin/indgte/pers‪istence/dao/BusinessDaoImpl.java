package com.baldwin.indgte.pers‪istence.dao;

import java.util.Collection;
import java.util.Date;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.User;

@Repository 
@Transactional
public class BusinessDaoImpl implements BusinessDao {

	static Logger log = LoggerFactory.getLogger(BusinessDao.class);
	
	@Autowired 
	private SessionFactory sessions;
	
	@Override
	public BusinessProfile get(String domain) {
		return get(domain, null);
	}
	
	private BusinessProfile get(String domain, Session session) {
		if(null == session) {
			session = sessions.getCurrentSession();
		}
		return (BusinessProfile) session.createCriteria(BusinessProfile.class)
				.add(Restrictions.eq(TableConstants.BUSINESS_DOMAIN, domain))
				.uniqueResult();
	}

	@Override
	public void create(BusinessProfile profile, String ownerName) {
		Session session = sessions.getCurrentSession();
		
		User owner = (User) session.createCriteria(User.class)
				.add(Restrictions.eq(TableConstants.USER_PROVIDER_USERID, ownerName))
				.add(Restrictions.eq(TableConstants.USER_PROVIDERID, TableConstants.USER_PROVIDERID_SPRINGSOCSEC))
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

	@Override
	public Collection<BusinessProfile> getBusinesses(String userId) {
		Session session = sessions.getCurrentSession();
		
		User owner = (User) session.createCriteria(User.class)
				.add(Restrictions.eq(TableConstants.USER_PROVIDER_USERID, userId))
				.add(Restrictions.eq(TableConstants.USER_PROVIDERID, TableConstants.USER_PROVIDERID_SPRINGSOCSEC))
				.setFetchMode("businesses", FetchMode.JOIN)
				.uniqueResult();
		
		return owner.getBusinesses();
	}

	@Override
	public void saveProfilepic(String domain, Imgur profilepic) {
		Session session = sessions.getCurrentSession();
		BusinessProfile business = get(domain, session);
		if(null != business.getProfilepic()) {
			session.delete(business.getProfilepic());
		}
		
		profilepic.setUploaded(new Date());
		business.setProfilepic(profilepic);
		session.update(business);
	}

	@Override
	public void saveCoverpic(String domain, Imgur coverpic) {
		Session session = sessions.getCurrentSession();
		BusinessProfile business = get(domain, session);
		if(null != business.getCoverpic()) {
			session.delete(business.getCoverpic());
		}
		
		coverpic.setUploaded(new Date());
		business.setCoverpic(coverpic);
		session.update(business);
	}
}
