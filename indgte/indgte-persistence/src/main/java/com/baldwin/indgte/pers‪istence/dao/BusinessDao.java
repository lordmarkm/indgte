package com.baldwin.indgte.pers‪istence.dao;

import java.util.Collection;
import java.util.List;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.Product;

public interface BusinessDao {
	BusinessProfile get(String bizName);
	BusinessProfile get(long businessId);
	
	/**
	 * Awful return type to save DB queries
	 * @return [UserExtension, BusinessProfile]
	 */
	Object[] getForViewProfile(String username, String domain);
	
	void create(BusinessProfile bizProfile, String owner);
	Object update(Object dirtyObject);
	void saveOrUpdate(BusinessProfile businessProfile, String owner);
	Collection<BusinessProfile> getBusinesses(String userId);
	void saveProfilepic(String domain, Imgur profilepic);
	void saveCoverpic(String domain, Imgur coverpic);
	Imgur getProfilepic(String domain);
	Category createCategory(String domain, String name, String description);
	Collection<Category> getCategories(String domain, boolean loadProducts);
	Category getCategory(long categoryId);
	void saveCategoryMainpic(String domain, long categoryId, Imgur mainpic);
	void createProduct(long categoryId, Product product);
	Collection<Product> getProducts(long categoryId);
	Product getProduct(long productId);
	Collection<Imgur> getProductPics(long productId);
	Collection<Imgur> getProductPics(long productId, int howmany);
	Imgur addProductPic(long productId, Imgur pic);
	Imgur updatePic(long imgurId, String title, String description);
	void hidePics(List<Long> imgurIds);
	void unhidePics(List<Long> imgurIds);
	void deletePics(long productId, List<Long> imgurIds);
	String findDomainForProductId(long productId);
	Product getProductWithPics(long productId);
	Category getCategoryWithProducts(long categoryId);
	String getDomain(long id);
	void delete(Long id);
	void editInfo(String username, String domain, String info);
	String getInfo(String domain);
	List<BusinessProfile> getSuggestions(BusinessProfile business);
	void setSoldout(long productId, boolean isSoldout);
	void deleteProduct(long productId);
}