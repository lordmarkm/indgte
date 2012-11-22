package com.baldwin.indgte.pers‪istence.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.constants.BusinessCategoryList;
import com.baldwin.indgte.persistence.model.BusinessGroup;

@Repository
@Transactional
public class BusinessGroupDaoImpl implements BusinessGroupDao {
	static Logger log = LoggerFactory.getLogger(BusinessGroupDaoImpl.class);
	
	@Autowired
	SessionFactory sessions;
	
	/**
	 * Load category names from enum. Only needs to be done once
	 * @see com.baldwin.indgte.persistence.constants.ApplicationInitializer
	 */
	public void init() {
		long startTime = System.currentTimeMillis();
		
		for(BusinessCategoryList categoryList : BusinessCategoryList.values()) {
			String[] names = categoryList.getValues().split(",");
			for(String name : names) {
				get(name);
			}
		}
		
		log.debug("Dirty hack completed in {} ms.", System.currentTimeMillis() - startTime);
	}
	
	@Override
	public BusinessGroup get(long id) {
		return (BusinessGroup) sessions.getCurrentSession().get(BusinessGroup.class, id);
	}

	@Override
	public BusinessGroup get(String name) {
		Session session = sessions.getCurrentSession();
		BusinessGroup category = (BusinessGroup) session.createCriteria(BusinessGroup.class)
			.add(Restrictions.eq(TableConstants.BIZCATEGORY_NAME, name))
			.uniqueResult();
		
		if(null == category) {
			category = new BusinessGroup(name);
			session.save(category);
		}
		
		return category;
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getCategories(String firstLetter) {
		List<BusinessGroup> categories = sessions.getCurrentSession().createCriteria(BusinessGroup.class)
				.add(Restrictions.like(TableConstants.BIZCATEGORY_NAME, firstLetter + "%"))
				.addOrder(Order.asc(TableConstants.BIZCATEGORY_NAME))
				.list();
		
		StringBuilder string = new StringBuilder();
		for(Iterator<BusinessGroup> i = categories.iterator(); i.hasNext();) {
			string.append(i.next().getName());
			if(i.hasNext()) string.append(",");
		}
		return string.toString();
	}
}
