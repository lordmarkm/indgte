package com.baldwin.indgte.pers‪istence.dao;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.model.BusinessProfile;

@Repository @Transactional
public class BusinessDaoImpl implements BusinessDao {

	@Autowired SessionFactory sessionFactory;
	
	@Override
	public BusinessProfile get(String bizDomain) {
		return (BusinessProfile) sessionFactory.getCurrentSession()
				.createCriteria(BusinessProfile.class)
				.add(Restrictions.like("domain", bizDomain))
				.uniqueResult();
	}

	@Override
	public void save(BusinessProfile profile) {
		sessionFactory.getCurrentSession().saveOrUpdate(profile);
	}

	@Override
	public void delete(BusinessProfile profile) {
		sessionFactory.getCurrentSession().delete(profile);
	}
}
