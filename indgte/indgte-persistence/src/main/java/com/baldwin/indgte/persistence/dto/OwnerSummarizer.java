package com.baldwin.indgte.persistence.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Product;

/**
 * Returns only objects that @param username owns
 * @author mbmartinez
 *
 */
public class OwnerSummarizer implements ResultTransformer {
	private static final long serialVersionUID = 8111318885168000740L;
	static Logger log = LoggerFactory.getLogger(OwnerSummarizer.class);
	
	private String username; //owner's username
	
	public OwnerSummarizer(String username) {
		super();
		this.username = username;
	}
	
	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		throw new IllegalStateException("Not used");
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List transformList(List collection) {
		if(null == username) {
			throw new IllegalStateException("Owner name can't be null!");
		}
		
		log.debug("Transforming {} items. Owner name: {}", collection.size(), username);
		
		List transformed = new ArrayList();
		for(Iterator<Summarizable> i = ((List<Summarizable>)collection).iterator(); i.hasNext();) {
			Summarizable result = i.next();
			if(result instanceof BusinessProfile) {
				BusinessProfile business = (BusinessProfile)result;
				if(business.getOwner().getUsername().equals(username)) {
					transformed.add(business.summarize());
				}
			} else if(result instanceof Category) {
				Category category = (Category)result;
				if(category.getBusiness().getOwner().getUsername().equals(username)) {
					transformed.add(category.summarize());
				}
			} else if(result instanceof Product) {
				Product product = (Product)result;
				if(product.getCategory().getBusiness().getOwner().getUsername().equals(username)) {
					transformed.add(product.summarize());
				}
			} else if(result instanceof BuyAndSellItem) {
				BuyAndSellItem item = (BuyAndSellItem)result;
				if(item.getOwner().getUsername().equals(username)) {
					transformed.add(item.summarize());
				}
			} else {
				throw new IllegalArgumentException("Illegal class: " + result.getClass());
			}
		}
		
		return transformed;
	}
}
