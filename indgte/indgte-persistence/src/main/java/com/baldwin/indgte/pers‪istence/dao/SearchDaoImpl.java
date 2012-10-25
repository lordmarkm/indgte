package com.baldwin.indgte.pers‪istence.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.dto.OwnerSummarizer;
import com.baldwin.indgte.persistence.dto.Summarizer;
import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Product;

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
	}

	@Override
	public List<Summary> search(String term, int maxResults) {
		return search(term, maxResults, null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Summary> search(String term, int maxResults, String ownername) {
		Session session = sessions.getCurrentSession();
		FullTextSession ftSession = Search.getFullTextSession(session);
		
		QueryBuilder q = ftSession.getSearchFactory().buildQueryBuilder().forEntity(BusinessProfile.class).get();
		org.apache.lucene.search.Query lQuery = q.keyword().fuzzy().withThreshold(0.8f).withPrefixLength(1).onFields(BusinessProfile.searchableFields).matching(term).createQuery();

		Query query = ftSession.createFullTextQuery(lQuery, BusinessProfile.class);
		
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
	public List<Summary> searchCategory(String term, int maxResults) {
		return searchCategory(term, maxResults, null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Summary> searchCategory(String term, int maxResults, String ownername) {
		Session session = sessions.getCurrentSession();
		FullTextSession ftSession = Search.getFullTextSession(session);
		
		QueryBuilder q = ftSession.getSearchFactory().buildQueryBuilder().forEntity(Category.class).get();
		org.apache.lucene.search.Query lQuery = q.keyword()
				.fuzzy().withThreshold(0.8f).withPrefixLength(1)
				.onFields(Category.searchableFields).matching(term).createQuery();

		Query query = ftSession.createFullTextQuery(lQuery, Category.class);
		
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
	public List<Summary> searchProduct(String term, int maxResults) {
		return searchProduct(term, maxResults, null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Summary> searchProduct(String term, int maxResults, String ownername) {
		Session session = sessions.getCurrentSession();
		FullTextSession ftSession = Search.getFullTextSession(session);
		
		QueryBuilder q = ftSession.getSearchFactory().buildQueryBuilder().forEntity(Product.class).get();
		org.apache.lucene.search.Query lQuery = q.keyword()
				.fuzzy().withThreshold(0.8f).withPrefixLength(1)
				.onFields(Product.searchableFields).matching(term).createQuery();

		Query query = ftSession.createFullTextQuery(lQuery, Product.class);

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
}
