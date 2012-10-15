package com.baldwin.indgte.pers‪istence.dao;

import java.util.Collection;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.Product;

public interface BusinessDao {
	BusinessProfile get(String bizName);
	void create(BusinessProfile bizProfile, String owner);
	void update(BusinessProfile bizProfile);
	void delete(BusinessProfile bizProfile);
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
	Imgur addProductPic(long productId, Imgur pic);
}