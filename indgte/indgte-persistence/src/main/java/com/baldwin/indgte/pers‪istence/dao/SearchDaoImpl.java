package com.baldwin.indgte.pers‪istence.dao;

import static com.baldwin.indgte.persistence.constants.SearchAndSummaryConstants.luceneVersion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.hibernate.Criteria;
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

import com.baldwin.indgte.persistence.constants.SearchAndSummaryConstants;
import com.baldwin.indgte.persistence.dto.OwnerSummarizer;
import com.baldwin.indgte.persistence.dto.Summarizable;
import com.baldwin.indgte.persistence.dto.Summarizer;
import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.YellowPagesEntry;
import com.baldwin.indgte.persistence.model.BusinessGroup;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Product;
import com.baldwin.indgte.persistence.model.Tag;
import com.baldwin.indgte.persistence.model.TopTenList;
import com.baldwin.indgte.persistence.model.User;

@Repository
@Transactional
public class SearchDaoImpl implements SearchDao {
	
	static Logger log = LoggerFactory.getLogger(SearchDaoImpl.class);
	
	@Autowired
	private SessionFactory sessions;
	
	@Autowired
	private InteractiveDao interact;
	
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
		for(TopTenList list : (List<TopTenList>)session.createQuery("from TopTenList").list()) {
			ftSession.index(list);
		}
		for(BuyAndSellItem item : (List<BuyAndSellItem>)session.createQuery("from BuyAndSellItem").list()) {
			ftSession.index(item);
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
			.setProjection(Projections.countDistinct("businessGroup"))
			.list();
		
		log.debug("Results: [{}]", results);
	}
	

	private final String countQuery = "select c.name,b.groupId,count(b.groupId) as count from businesses b, businessCategories c " +
			 "where b.groupId = c.groupId group by b.groupId order by c.name asc";
	/**
	 * <p>WARNING: Functionally equivalent to {@link #getListableGroups()} but crappy.</p> 
	 * 
	 * <p>select c.name,b.categoryId,count(b.categoryId) as count from businesses b, businessCategories c 
	 * where b.categoryId = c.categoryId group by b.categoryId order by c.name asc;</p>
	 * 
	 * <p>cat name		| catId | businesses<br>
	 * abortion clinics | 1809  | 4<br>
	 * drug den			| 1544  | 2<br>
	 * 
	 * Question: how do we do this with JPA? Answer: use {@link #getListableGroups()}
	 */
	@Deprecated
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
			.add(Restrictions.eq("businessGroup.id", categoryId))
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
				.add(Restrictions.eq("businessGroup.id", categoryId))
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

	/**
	 * Functionally equivalent to {@link #countBusinesses()}, but better
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MultiValueMap<String, Number> getListableGroups() {
		List<Object[]> results = sessions.getCurrentSession().createCriteria(BusinessProfile.class)
			.createAlias("businessGroup", "businessGroup")
			.setProjection(
				Projections.projectionList()
					.add(Projections.groupProperty("businessGroup.name"))
					.add(Projections.property("businessGroup.id"))
					.add(Projections.count("businessGroup.name"))	
			)
			.addOrder(Order.asc("businessGroup.name"))
			.list();
		
		MultiValueMap<String, Number> businesses = new LinkedMultiValueMap<String, Number>();
		for(Object[] result : results) {
			log.debug("{}", Arrays.asList(result));
			businesses.put((String)result[0], Arrays.asList(new Number[]{(Number)result[1], (Number)result[2]}));
		}
		
		return businesses;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<TopTenList> searchTopTenLists(String term, int start, int howmany) {
		log.debug("Searching for [{}] in class TopTenLists", term);
		log.debug("Starting with {}, Limiting results to {}", start, howmany == -1 ? "No limit" : howmany);
		
		Session session = sessions.getCurrentSession();
		FullTextSession ftSession = Search.getFullTextSession(session);
		
		QueryBuilder q = ftSession.getSearchFactory().buildQueryBuilder().forEntity(TopTenList.class).get();
		org.apache.lucene.search.Query lQuery;
		try {
			lQuery = q.keyword()
					.fuzzy().withThreshold(0.5f).withPrefixLength(1)
					.onFields(TableConstants.TOPTEN_TITLE, TableConstants.TOPTEN_DESCRIPTION)
					.matching(term).createQuery();
		} catch (Exception e) {
			log.error("Error creating query", e);
			e.printStackTrace();
			return null;
		} 

		Query query = ftSession.createFullTextQuery(lQuery, TopTenList.class);
		if(start != -1) {
			query.setFirstResult(start);
		}
		if(howmany != -1) {
			query.setMaxResults(howmany);
		}
		
		Collection<TopTenList> results = query.list();
		for(TopTenList list : results) {
			interact.initializeAttachment(list.getLeader());
		}
		
		log.debug("Returning {} results", results.size());
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Tag> getTags(Tag.SortColumn sortColumn, int howmany) {
		Criteria crit = sessions.getCurrentSession().createCriteria(Tag.class)
			.setMaxResults(howmany);
		
		switch(sortColumn) {
		case name:
			crit.addOrder(Order.asc(TableConstants.TAG));
			break;
		case numberofitems:
			crit.addOrder(Order.desc(TableConstants.TAG_ITEMS));
			break;
		}
		
		return crit.list();
	}

	@Override
	public Collection<BuyAndSellItem> searchBuySell(String term, int start, int howmany) {
		log.debug("Searching for [{}] in class BuyAndSellItem", term);
		log.debug("Starting with {}, Limiting results to {}", start, howmany == -1 ? "No limit" : howmany);
		
		Session session = sessions.getCurrentSession();
		FullTextSession ftSession = Search.getFullTextSession(session);
		
		QueryBuilder q = ftSession.getSearchFactory().buildQueryBuilder().forEntity(BuyAndSellItem.class).get();
		org.apache.lucene.search.Query lQuery;
		try {
			lQuery = q.keyword()
					.fuzzy().withThreshold(0.5f).withPrefixLength(1)
					.onFields(TableConstants.BUYANDSELL_TAGS, TableConstants.NAME, TableConstants.DESCRIPTION)
					.matching(term).createQuery();
		} catch (Exception e) {
			log.error("Error creating query", e);
			e.printStackTrace();
			return null;
		} 

		Query query = ftSession.createFullTextQuery(lQuery, BuyAndSellItem.class);
		if(start != -1) {
			query.setFirstResult(start);
		}
		if(howmany != -1) {
			query.setMaxResults(howmany);
		}
		
		@SuppressWarnings("unchecked")
		Collection<BuyAndSellItem> results = query.list();
		
		log.debug("Returning {} results", results.size());
		return results;
	}

	@Override
	public Collection<BuyAndSellItem> searchBuySellTag(String tag, String term, int start, int howmany) {
		log.debug("Searching for [{}] in BuyAndSell tag {}", term, tag);
		
		Session session = sessions.getCurrentSession();
		FullTextSession ftSession = Search.getFullTextSession(session);
		
		org.apache.lucene.search.Query termQuery;
		org.apache.lucene.search.Query tagQuery;
		try {
			termQuery = ftSession.getSearchFactory().buildQueryBuilder()
					.forEntity(BuyAndSellItem.class).get().keyword()
					.fuzzy().withThreshold(0.5f).withPrefixLength(1)
					.onFields(TableConstants.BUYANDSELL_TAGS, TableConstants.NAME, TableConstants.DESCRIPTION)
					.matching(term).createQuery();
			
			tagQuery = 	new QueryParser(luceneVersion, TableConstants.BUYANDSELL_TAGS, 
							new WhitespaceAnalyzer(SearchAndSummaryConstants.luceneVersion))
							.parse(tag);
		} catch (Exception e) {
			log.error("Error creating query", e);
			e.printStackTrace();
			return null;
		} 

		BooleanQuery bQuery = new BooleanQuery();
		bQuery.add(new BooleanClause(termQuery, BooleanClause.Occur.MUST));
		bQuery.add(new BooleanClause(tagQuery, BooleanClause.Occur.MUST));
		
		log.debug("About to search using query [{}]", bQuery.toString());
		
		@SuppressWarnings("unchecked")
		Collection<BuyAndSellItem> results = ftSession.createFullTextQuery(bQuery, BuyAndSellItem.class)
				.setFirstResult(start)
				.setMaxResults(howmany)
				.list();
		
		log.debug("Returning {} results", results.size());
		return results;
	}
}