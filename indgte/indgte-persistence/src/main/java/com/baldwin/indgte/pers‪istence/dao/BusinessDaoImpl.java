package com.baldwin.indgte.pers‪istence.dao;

import static com.baldwin.indgte.pers‪istence.dao.TableConstants.BUSINESS_DOMAIN;
import static com.baldwin.indgte.pers‪istence.dao.TableConstants.BUSINESS_INFO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.model.BillingTransaction;
import com.baldwin.indgte.persistence.model.BusinessGroup;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.BusinessReview;
import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.Product;
import com.baldwin.indgte.persistence.model.TopTenCandidate;
import com.baldwin.indgte.persistence.model.TopTenList;
import com.baldwin.indgte.persistence.model.UserExtension;

@Repository 
@Transactional
public class BusinessDaoImpl implements BusinessDao {

	static Logger log = LoggerFactory.getLogger(BusinessDao.class);
	
	@Autowired 
	private SessionFactory sessions;
	
	@Autowired
	private UserDao users;
	
	@Autowired
	private BusinessGroupDao groups;
	
	@Value("${review.queue.maxsize}")
	private int forReviewQueueMaxSize;
	
	@Override
	public BusinessProfile get(String domain) {
		return (BusinessProfile) sessions.getCurrentSession().createCriteria(BusinessProfile.class)
				.add(Restrictions.eq(TableConstants.BUSINESS_DOMAIN, domain))
				.uniqueResult();
	}
	
	@Override
	public BusinessProfile get(long businessId) {
		return (BusinessProfile) sessions.getCurrentSession().get(BusinessProfile.class, businessId);
	}
	
	@Override
	public Object[] getForViewProfile(String username, String domain) { 
		BusinessProfile business = get(domain);
		UserExtension userExtension = users.getExtended(username);
		
		if(null == business) {
			return null;
		}
		
		boolean unreviewed = true;
		for(BusinessReview businessReview : userExtension.getBusinessReviews()) {
			if(businessReview.getReviewed().equals(business)) {
				unreviewed = false;
				break;
			}
		}
		
		if(unreviewed //owner can't review his own businesses
				&& !business.getOwner().equals(userExtension) //don't notify if user has already reviewed the business
				&& !userExtension.getNeverReview().contains(business.getId())) { //don't notify if user has indicated he never wants to review the business
			List<BusinessProfile> forReview = userExtension.getForReview();
			forReview.remove(business);
			forReview.add(0, business);
			if(forReview.size() > forReviewQueueMaxSize) {
				forReview.remove(forReview.size() - 1);
			}
		}
		sessions.getCurrentSession().update(userExtension);
		return new Object[] {userExtension, business};
	}
	
	@Override
	public void create(BusinessProfile profile, String ownerName) {
		Session session = sessions.getCurrentSession();
		UserExtension owner = users.getExtended(ownerName);
		
		log.info("Creating business {} for owner {}", profile.getDomain(), ownerName);
		
		owner.getBusinesses().add(profile);
		profile.setOwner(owner);
		
		session.save(profile);

		//subscribe owner to his own business
		owner.getBusinessSubscriptions().add(profile.getId());
		
		BusinessGroup group = groups.get(profile.getCategory().getId());
		TopTenList list;
		if(null != group && (list = group.getTopTenList()) != null) { //if list is null it will be added later on
			TopTenCandidate candidate = new TopTenCandidate(profile);
			candidate.setList(list);
			candidate.setCreator(owner);
			
			list.getCandidates().add(candidate);
			session.save(candidate);
		}
	}

	@Override
	public Object update(Object dirtyObject) {
		sessions.getCurrentSession().update(dirtyObject);
		return dirtyObject;
	}
	
	@Override
	public void saveOrUpdate(BusinessProfile businessProfile, String owner) {
		log.info("Saving: [{}]", businessProfile);
		log.info("Owner: [{}]", businessProfile.getOwner());
		
		if(businessProfile.getOwner() != null && owner.equals(businessProfile.getOwner().getUsername())) {
			update(businessProfile);
		} else {
			create(businessProfile, owner);
		}
	}
	
	@Override
	public Collection<BusinessProfile> getBusinesses(String userId) {
		UserExtension owner = users.getExtended(userId);
		return owner.getBusinesses();
	}

	@Override
	public void saveProfilepic(String domain, Imgur profilepic) {
		Session session = sessions.getCurrentSession();
		BusinessProfile business = get(domain);
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
		BusinessProfile business = get(domain);
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

		BusinessProfile business = get(domain);
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

	@Override
	public Category getCategoryWithProducts(long categoryId) {
		Category category = (Category) sessions.getCurrentSession().get(Category.class, categoryId);
		Hibernate.initialize(category.getProducts());
		return category;
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
		product.setTime(new Date());
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
	public String findDomainForProductId(long productId) {
		Product product = (Product) sessions.getCurrentSession().get(Product.class, productId);
		return product.getCategory().getBusiness().getDomain();
	}

	@Override
	public Product getProductWithPics(long productId) {
		Product product = (Product) sessions.getCurrentSession().get(Product.class, productId);
		Hibernate.initialize(product.getPics());
		return product;
	}

	@Override
	public String getDomain(long id) {
		return (String) sessions.getCurrentSession().createCriteria(BusinessProfile.class)
			.setProjection(Projections.property("domain"))
			.add(Restrictions.eq("id", id))
			.uniqueResult();
	}

	@Override
	public void delete(Long id) {
		log.warn("Deleting business with id {}!!!", id);
		sessions.getCurrentSession().createQuery("update BusinessProfile set deleted = true where id = :id")
			.setLong(TableConstants.ID, id)
			.executeUpdate();
	}

	@Override
	public void editInfo(String username, String domain, String info) {
		get(domain).setInfo(info);
	}

	@Override
	public String getInfo(String domain) {
		return (String) sessions.getCurrentSession().createCriteria(BusinessProfile.class)
			.add(Restrictions.eq(BUSINESS_DOMAIN, domain))
			.setProjection(Projections.property(BUSINESS_INFO))
			.uniqueResult();
	}

	private class ReviewComparator implements Comparator<BusinessProfile> {
		@Override
		public int compare(BusinessProfile o1, BusinessProfile o2) {
			if(o1.getAverageReviewScore() == o2.getAverageReviewScore()) {
				return o1.getFullName().compareTo(o2.getFullName());
			}
			return o1.getAverageReviewScore() - o2.getAverageReviewScore() > 0 ? -1 : 1;
		}
	}
	
	@Override
	public List<BusinessProfile> getSuggestions(BusinessProfile business) {
		return getTopRated(business.getCategory(), business, 5);
	}
	
	private List<BusinessProfile> getTopRated(BusinessGroup group, BusinessProfile exclude, int max) {
		Criteria crit = sessions.getCurrentSession().createCriteria(BusinessProfile.class)
			.add(Restrictions.eq("businessGroup", group));
		
		if(null != exclude) {
			crit.add(Restrictions.ne("id", exclude.getId()));
		}
		
		@SuppressWarnings("unchecked")
		List<BusinessProfile> similar = (List<BusinessProfile>) crit.list();	
			
		Collections.sort(similar, new ReviewComparator());
		if(max != -1 && similar.size() > max) {
			return similar.subList(0, max);
		} else {
			return similar;
		}
	}
	
	@Override
	public void setSoldout(long productId, boolean isSoldout) {
		Product product = getProduct(productId);
		product.setSoldout(isSoldout);
	}
	
	@Override
	public void deleteProduct(long productId) {
		Product product = getProduct(productId);
		deleteProduct(product);
	}
	
	private void deleteProduct(Product product) {
		for(BillingTransaction b : product.getTransactions()) {
			b.getDetails().setAdvertisedProduct(null);
		}
		
		sessions.getCurrentSession().delete(product);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Summary> getLatestEntities() {
		Session session = sessions.getCurrentSession();
		
		List<Product> products = session.createCriteria(Product.class)
			.addOrder(Order.desc("time"))
			.setMaxResults(5)
			.list();
		
		List<BuyAndSellItem> bass = session.createCriteria(BuyAndSellItem.class)
				.addOrder(Order.desc("time"))
				.setMaxResults(5)
				.list();
		
		List<Summary> summaries = new ArrayList<>();
		for(Product product : products) {
			summaries.add(product.summarize());
		}
		for(BuyAndSellItem bas : bass) {
			summaries.add(bas.summarize());
		}
		
		Collections.sort(summaries, new Comparator<Summary>(){
			@Override
			public int compare(Summary o1, Summary o2) {
				return -1 * o1.getTime().compareTo(o2.getTime());
			}
		});
		
		if(summaries.size() > 5) {
			return summaries.subList(0, 5);
		} else {
			return summaries;
		}
	}

	@Override
	public void deleteCategory(long categoryId) {
		Category category = getCategory(categoryId);
		
		List<BillingTransaction> transactions = category.getTransactions();
		for(BillingTransaction b : transactions) {
			b.getDetails().setAdvertisedCategory(null);
		}
		
		for(Product product : category.getProducts()) {
			deleteProduct(product);
		}
		
		sessions.getCurrentSession().delete(category);
	}
}