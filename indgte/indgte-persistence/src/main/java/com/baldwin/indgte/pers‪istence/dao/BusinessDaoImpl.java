package com.baldwin.indgte.pers‪istence.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
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
		Product product = (Product) sessions.getCurrentSession().createCriteria(Product.class)
			.add(Restrictions.eq(TableConstants.PRODUCT_ID, productId))
			.setFetchMode(TableConstants.PRODUCT_PICS, FetchMode.JOIN)
			.uniqueResult();
		List<Imgur> results = new ArrayList<Imgur>(product.getPics());
		if(null != product.getMainpic()) results.add(product.getMainpic());
		return results;

//		String hql = "select pic, p.mainpic from Product p inner join p.pics pic where p.id = :productId";
//		List<Imgur> results = sessions.getCurrentSession().createQuery(hql)
//			.setLong("productId", productId)
//			.list();
//		log.debug("getProductPics(...) found {} results for productId {}", results.size(), productId);
//		return results;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<Imgur> getProductPics(long productId, int howmany) {
		String hql = "select pic from Product p inner join p.pics pic where p.id = :productId";
		List<Imgur> results = sessions.getCurrentSession().createQuery(hql)
				.setLong("productId", productId)
				.setMaxResults(howmany).list();
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
		return imgur;
	}
}