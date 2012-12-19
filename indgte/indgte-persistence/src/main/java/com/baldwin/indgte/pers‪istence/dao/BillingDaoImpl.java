package com.baldwin.indgte.pers‪istence.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.dto.Summary.SummaryType;
import com.baldwin.indgte.persistence.model.BillingInformation;
import com.baldwin.indgte.persistence.model.BillingTransaction;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Product;
import com.baldwin.indgte.persistence.model.SidebarFeature;
import com.baldwin.indgte.persistence.model.BillingTransaction.BillingOperation;
import com.baldwin.indgte.persistence.model.BillingTransaction.BillingTransactionDetails;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.model.UserExtension;

@Repository
@Transactional
public class BillingDaoImpl implements BillingDao {
	
	static Logger log = LoggerFactory.getLogger(BillingDaoImpl.class);
	
	@Autowired
	private UserDao users;
	
	@Autowired
	private InteractiveDao interact;
	
	@Autowired
	private BusinessDao businesses;
	
	@Autowired
	private TradeDao trade;
	
	@Autowired
	private SessionFactory sessions;
	
	@Override
	public UserExtension grantCoconuts(String adminName, long receiverId, int howmany) {
		UserExtension admin = users.getExtended(adminName);
		UserExtension user = users.getExtended(receiverId);
		
		log.debug("Admin [{}] is granting {} coconuts to [{}]", adminName, howmany, user.getUsername());
		
		BillingInformation b = user.getBillingInfo();
		b.setCoconuts(b.getCoconuts() + howmany);
		
		BillingTransaction transaction = new BillingTransaction();
		transaction.setAmount(howmany);
		transaction.setOperation(BillingOperation.grant);
		transaction.setUser(user);
		
		BillingTransactionDetails details = transaction.getDetails();
		details.setGrantingAdmin(admin);
		
		sessions.getCurrentSession().save(transaction);
		
		return user;
	}

	@Override
	public void promotePost(String username, Date startDate, Date endDate,	int coconutCost, long postId) {
		Post post = interact.getPost(postId);
		if(null == post) {
			throw new IllegalArgumentException("Nonexistent post: " + postId);
		}
		post.setFeatureStart(startDate);
		post.setFeatureEnd(endDate);
		
		//charge the user
		UserExtension user = users.getExtended(username);
		int coconuts = user.getBillingInfo().getCoconuts();
		if(coconuts < coconutCost) {
			throw new IllegalArgumentException("Not enough coconuts to complete transaction");
		}
		user.getBillingInfo().setCoconuts(coconuts - coconutCost);
		
		BillingTransaction transaction = new BillingTransaction();
		transaction.setAmount(coconutCost);
		transaction.setOperation(BillingOperation.feature_post);
		transaction.setUser(user);
		
		BillingTransactionDetails details = transaction.getDetails();
		details.setFeaturedPost(post);
		
		sessions.getCurrentSession().save(transaction);
	}

	@Override
	public void promoteSidebar(String username, SummaryType type, long id, Date start, Date end, int coconutCost) {
		Session session = sessions.getCurrentSession();
		
		//billing
		UserExtension promoter = users.getExtended(username);
		int currentCoconuts = promoter.getBillingInfo().getCoconuts();
		if(currentCoconuts < coconutCost) throw new IllegalArgumentException("Insufficient coconuts");
		promoter.getBillingInfo().setCoconuts(currentCoconuts - coconutCost);
		
		//promotion
		SidebarFeature feature = new SidebarFeature();
		feature.setStart(start);
		feature.setEnd(end);
		feature.setType(type);
		
		//transaction logging
		BillingTransaction transaction = new BillingTransaction();
		transaction.setAmount(coconutCost);
		transaction.setUser(promoter);
		
		BillingTransactionDetails details = transaction.getDetails();
		session.save(transaction);
		
		switch(type) {
		case business:
			BusinessProfile business = businesses.get(id);
			feature.setBusiness(business);
			transaction.setOperation(BillingOperation.advertise_business);
			details.setAdvertisedBusiness(business);
			break;
		case category:
			Category category = businesses.getCategory(id);
			feature.setCategory(category);
			transaction.setOperation(BillingOperation.advertise_category);
			details.setAdvertisedCategory(category);
			break;
		case product:
			Product product = businesses.getProduct(id);
			feature.setProduct(product);
			transaction.setOperation(BillingOperation.advertise_product);
			details.setAdvertisedProduct(product);
			break;
		case buyandsellitem:
			BuyAndSellItem bas = trade.get(id);
			feature.setBas(bas);
			transaction.setOperation(BillingOperation.advertise_buyandsell);
			details.setAdvertisedItem(bas);
			break;
		default:
			throw new IllegalArgumentException("Illegal feature type: " + type);
		}
		
		session.save(feature);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SidebarFeature> getSidebarPromos() {
		return sessions.getCurrentSession().createCriteria(SidebarFeature.class)
				.list();
	}
	
}
