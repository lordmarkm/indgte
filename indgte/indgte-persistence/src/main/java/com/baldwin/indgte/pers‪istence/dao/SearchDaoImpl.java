package com.baldwin.indgte.pers‪istence.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.baldwin.indgte.persistence.dto.OwnerSummarizer;
import com.baldwin.indgte.persistence.dto.Summarizable;
import com.baldwin.indgte.persistence.dto.Summarizer;
import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.YellowPagesEntry;
import com.baldwin.indgte.persistence.model.BusinessGroup;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Product;
import com.baldwin.indgte.persistence.model.User;

@Repository
@Transactional
public class SearchDaoImpl implements SearchDao {
	
	static Logger log = LoggerFactory.getLogger(SearchDaoImpl.class);
	
	@Autowired
	private SessionFactory sessions;
	
	@Override
	@SuppressWarnings("unchecked")
	public void reindex() {
		Session session = sessions.getCurrentSession();
		List<BusinessProfile> businesses = session.createQuery("from BusinessProfile").list();
		
		FullTextSession ftSession = Search.getFullTextSession(session);
		for(BusinessProfile business : businesses) {
			ftSession.index(business);
		}
		for(Category category : (List<Category>)session.createQuery("from Category").list()) {
			ftSession.index(category);
		}
		for(Product product : (List<Product>)session.createQuery("from Product").list()) {
			ftSession.index(product);
		}
		for(User user : (List<User>)session.createQuery("from User").list()) {
			ftSession.index(user);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Summary> search(String term, int maxResults, Class<? extends Summarizable> c, String ownername) {
		log.debug("Searching for [{}] in class {}", term, c);
		log.debug("Limiting results to [{}] and owner {}", maxResults == -1 ? "No limit" : maxResults, ownername == null ? "Any owner" : ownername);
		
		Session session = sessions.getCurrentSession();
		FullTextSession ftSession = Search.getFullTextSession(session);
		
		QueryBuilder q = ftSession.getSearchFactory().buildQueryBuilder().forEntity(c).get();
		org.apache.lucene.search.Query lQuery;
		try {
			String[] fields = (String[])c.getDeclaredMethod("getSearchableFields").invoke(c.newInstance());
			lQuery = q.keyword()
					.fuzzy().withThreshold(0.8f).withPrefixLength(1)
					.onFields(fields)
					.matching(term).createQuery();
		} catch (Exception e) {
			log.error("Error creating query", e);
			e.printStackTrace();
			return null;
		} 

		Query query = ftSession.createFullTextQuery(lQuery, c);
		
		if(null == ownername) {
			query.setResultTransformer(new Summarizer());
		} else {
			query.setResultTransformer(new OwnerSummarizer(ownername));
		}
		
		if(maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		
		return query.list();
	}

	@Override
	public void test() {
		Object results = sessions.getCurrentSession().createCriteria(BusinessProfile.class)
			.setProjection(Projections.countDistinct("category"))
			.list();
		
		log.debug("Results: [{}]", results);
	}
	
	/**
	 * select c.name,b.categoryId,count(b.categoryId) as count from businesses b, businessCategories c 
	 * where b.categoryId = c.categoryId group by b.categoryId order by c.name asc;
	 * 
	 * cat name			| catId | businesses
	 * abortion clinics | 1809  | 4
	 * drug den			| 1544  | 2
	 * 
	 * @return LinkedHashMap to preserve (alphabetical) order
	 * TODO: how do we do this with JPA?
	 */
	private final String countQuery = "select c.name,b.categoryId,count(b.categoryId) as count from businesses b, businessCategories c " +
			 "where b.categoryId = c.categoryId group by b.categoryId order by c.name asc";
	@Override
	@SuppressWarnings("unchecked")
	public MultiValueMap<String, Number> countBusinesses() {
		Session session = sessions.getCurrentSession();
		List<Object[]> results = session.createSQLQuery(countQuery).list();
		
		MultiValueMap<String, Number> businesses = new LinkedMultiValueMap<String, Number>();
		for(Object[] row : results) {
			log.debug("Constructing " + row[0] + "," + row[1] + "," + row[2]);
			String name = (String) row[0];
			Number catId = (Number) row[1];
			Number count = (Number) row[2];
			
			businesses.put(name, Arrays.asList(new Number[]{catId, count}));
		}
		
		return businesses;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Summary> getBusinesses(Long categoryId, int howmany) {
		List<BusinessProfile> businesses = sessions.getCurrentSession().createCriteria(BusinessProfile.class)
			.setMaxResults(howmany)
			.add(Restrictions.eq("category.id", categoryId))
			.list();
		
		List<Summary> summarized = new ArrayList<Summary>();
		for(BusinessProfile business : businesses) {
			Summary summary = business.summarize();
			summary.setDescription(business.getAddress());
			summarized.add(summary);
		}
		
		return summarized;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<YellowPagesEntry> getYellowPagesEntries(long categoryId) {
		List<BusinessProfile> businesses = sessions.getCurrentSession().createCriteria(BusinessProfile.class)
				.add(Restrictions.eq("category.id", categoryId))
				.addOrder(Order.asc("fullName"))
				.list();
		List<YellowPagesEntry> yellowPagesEntries = new ArrayList<YellowPagesEntry>();
		for(BusinessProfile business : businesses) {
			yellowPagesEntries.add(new YellowPagesEntry(business));
		}
		return yellowPagesEntries;
	}

	@Override
	public BusinessGroup getBusinessGroup(long groupId) {
		return (BusinessGroup) sessions.getCurrentSession().get(BusinessGroup.class, groupId);
	}
}