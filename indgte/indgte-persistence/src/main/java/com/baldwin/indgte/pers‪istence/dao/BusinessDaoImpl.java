package com.baldwin.indgte.pers‪istence.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.FuzzyQuery;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.constants.SearchConstants;
import com.baldwin.indgte.persistence.dto.SearchResultTransformer;
import com.baldwin.indgte.persistence.dto.SearchResult;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.Product;
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
	public Object update(Object dirtyObject) {
		sessions.getCurrentSession().update(dirtyObject);
		return dirtyObject;
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

	@Override
	public Imgur getProfilepic(String domain) {
		return (Imgur) sessions.getCurrentSession().createQuery("select profilepic from BusinessProfile where domain = :domain")
			.setString("domain", domain)
			.uniqueResult();
	}

	@Override
	public Category createCategory(String domain, String name, String description) {
		log.debug("Creating category {} in {}", name, domain);
		
		Session session = sessions.getCurrentSession();

		BusinessProfile business = get(domain, session);
		Hibernate.initialize(business.getCategories());
		
		Category category = new Category();
		category.setName(name);
		category.setDescription(description);
		category.setBusiness(business);
		
		business.getCategories().add(category);
		session.update(business);
		
		return category;
	}

	@Override
	public Collection<Category> getCategories(String domain, boolean loadProducts) {
		log.debug("Finding categories for domain {}", domain);
		Criteria criteria = sessions.getCurrentSession().createCriteria(BusinessProfile.class)
				.add(Restrictions.eq(TableConstants.BUSINESS_DOMAIN, domain))
				.setFetchMode(TableConstants.BUSINESS_CATEGORIES, FetchMode.JOIN);
		
		if(loadProducts) {
			criteria.setFetchMode(TableConstants.BUSINESS_CATEGORIES + "." + TableConstants.CATEGORY_PRODUCTS, FetchMode.JOIN);
		}
		
		BusinessProfile business = (BusinessProfile) criteria.uniqueResult();
		
		return business.getCategories();
	}

	@Override
	public Category getCategory(long categoryId) {
		return getCategory(categoryId, null);
	}

	private Category getCategory(long categoryId, Session session) {
		if(null == session) {
			session = sessions.getCurrentSession();
		}
		return (Category) session.get(Category.class, categoryId);
	}
	
	@Override
	public void saveCategoryMainpic(String domain, long categoryId, Imgur mainpic) {
		Session session = sessions.getCurrentSession();
		Category category = (Category) session.get(Category.class, categoryId);
		category.setMainpic(mainpic);
		session.update(category);
	}

	@Override
	public void createProduct(long categoryId, Product product) {
		Session session = sessions.getCurrentSession();
		Category category = getCategory(categoryId, session);
		category.getProducts().add(product);
		product.setCategory(category);
		session.update(category);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Product> getProducts(long categoryId) {
		return sessions.getCurrentSession().createCriteria(Product.class)
			.add(Restrictions.eq(TableConstants.PRODUCT_CATEGORYID, categoryId))
			.list();
	}

	@Override
	public Product getProduct(long productId) {
		Product product = (Product) sessions.getCurrentSession().get(Product.class, productId);
		Hibernate.initialize(product.getPics());
		return product;
	}

	@Override
	public Collection<Imgur> getProductPics(long productId) {
//		Product product = (Product) sessions.getCurrentSession().createCriteria(Product.class)
//			.add(Restrictions.eq(TableConstants.PRODUCT_ID, productId))
//			.setFetchMode(TableConstants.PRODUCT_PICS, FetchMode.JOIN)
//			.uniqueResult();
//		List<Imgur> results = new ArrayList<Imgur>(product.getPics());
//		if(null != product.getMainpic()) results.add(product.getMainpic());
//		return results;

//		String hql = "select pic, p.mainpic from Product p inner join p.pics pic where p.id = :productId";
//		List<Imgur> results = sessions.getCurrentSession().createQuery(hql)
//			.setLong("productId", productId)
//			.list();
//		log.debug("getProductPics(...) found {} results for productId {}", results.size(), productId);
//		return results;
		
		return getProductPics(productId, -1);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<Imgur> getProductPics(long productId, int howmany) {
		String hql = "select pic from Product p inner join p.pics pic where p.id = :productId";
		Query query = sessions.getCurrentSession().createQuery(hql)
				.setLong("productId", productId);
		if(howmany != -1) {
			query.setMaxResults(howmany).list();
		}
		Collection<Imgur> results = query.list();
		log.debug("Results: {}", results);
		return results;
	}

	@Override
	public Imgur addProductPic(long productId, Imgur pic) {
		Session session = sessions.getCurrentSession();
		Product product = (Product) session.get(Product.class, productId);
		pic.setUploaded(new Date());
		product.getPics().add(pic);
		return pic;
	}

	@Override
	public Imgur updatePic(long imgurId, String title, String description) {
		Imgur imgur = (Imgur) sessions.getCurrentSession().get(Imgur.class, imgurId);
		imgur.setTitle(title);
		imgur.setDescription(description);
		log.debug("{} updated.", imgur.getTitle());
		return imgur;
	}

	@Override
	public void hidePics(List<Long> imgurIds) {
		String hql = "update Imgur set hidden = true where imageId in (:imgurIds)";
		int updated = sessions.getCurrentSession().createQuery(hql)
			.setParameterList("imgurIds", imgurIds)
			.executeUpdate();
		log.debug("{} pictures hidden.", updated);
	}

	@Override
	public void unhidePics(List<Long> imgurIds) {
		String hql = "update Imgur set hidden = false where imageId in (:imgurIds)";
		int updated = sessions.getCurrentSession().createQuery(hql)
			.setParameterList("imgurIds", imgurIds)
			.executeUpdate();
		log.debug("{} pictures unhidden.", updated);
	}

	@Override
	public void deletePics(long productId, List<Long> imgurIds) {
		Product product = getProduct(productId);
		int deleted = 0;
		for(Iterator<Imgur> i = product.getPics().iterator(); i.hasNext();) {
			Imgur candidate = i.next();
			if(imgurIds.contains(candidate.getImageId())) {
				i.remove();
				++deleted;
			}
		}
		log.debug("{} pictures deleted from database.", deleted);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void reindex() {
		Session session = sessions.getCurrentSession();
		List<BusinessProfile> businesses = session.createQuery("from BusinessProfile").list();
		
		FullTextSession ftSession = Search.getFullTextSession(session);
		for(BusinessProfile business : businesses) {
			ftSession.index(business);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SearchResult> search(String term) throws ParseException {
		log.debug("Searching for term {}", term);
		
		Session session = sessions.getCurrentSession();
		FullTextSession ftSession = Search.getFullTextSession(session);
		MultiFieldQueryParser parser = new MultiFieldQueryParser(SearchConstants.version, BusinessProfile.searchableFields, new StandardAnalyzer(SearchConstants.version));
		
		QueryBuilder q = ftSession.getSearchFactory().buildQueryBuilder().forEntity(BusinessProfile.class).get();
		org.apache.lucene.search.Query lQuery = q.keyword().fuzzy().withThreshold(0.8f).withPrefixLength(1).onFields(BusinessProfile.searchableFields).matching(term).createQuery();

		Query query = ftSession.createFullTextQuery(lQuery, BusinessProfile.class)
				.setResultTransformer(new SearchResultTransformer());
		return query.list();
		
		//This returns exact results
//		Query query = ftSession.createFullTextQuery(parser.parse(term), BusinessProfile.class)
//			.setResultTransformer(new SearchResultTransformer());
//		return query.list();
		
//		List<BusinessProfile> businesses = query.list();
//		List<SearchResult> results = new ArrayList<SearchResult>();
//		for(BusinessProfile business : businesses) {
//			results.add(business.toSearchResult());
//		}
//		return results;
	}
}