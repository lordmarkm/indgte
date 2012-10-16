package com.baldwin.indgte.persistence.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.Product;
import com.baldwin.indgte.pers‪istence.dao.BusinessDao;

@Service
public class BusinessService {
	@Autowired BusinessDao dao;
	
	public BusinessProfile get(String domain) {
		return dao.get(domain);
	}
	public void save(BusinessProfile bizProfile, String owner) {
		dao.create(bizProfile, owner);
	}
	public void update(Object dirty) {
		dao.update(dirty);
	}
	public void delete(BusinessProfile bizProfile) {
		dao.delete(bizProfile);
	}
	public Collection<BusinessProfile> getBusinesses(String userId) {
		return dao.getBusinesses(userId);
	}
	public void saveProfilepic(String domain, Imgur profilepic) {
		dao.saveProfilepic(domain, profilepic);
	}
	public void saveCoverpic(String domain, Imgur coverpic) {
		dao.saveCoverpic(domain, coverpic);
	}
	public Imgur getProfilepic(String domain) {
		return dao.getProfilepic(domain);
	}
	public Category createCategory(String domain, String name, String description) {
		return dao.createCategory(domain, name, description);
	}
	public Collection<Category> getCategories(String domain, boolean loadProducts) {
		return dao.getCategories(domain, loadProducts);
	}
	public Category getCategory(long categoryId) {
		return dao.getCategory(categoryId);
	}
	public void saveCategoryMainpic(String domain, long categoryId, Imgur mainpic) {
		dao.saveCategoryMainpic(domain, categoryId, mainpic);
	}
	public void createProduct(long categoryId, Product product) {
		dao.createProduct(categoryId, product);
	}
	public Collection<Product> getProducts(long categoryId) {
		return dao.getProducts(categoryId);
	}
	public Product getProduct(long productId) {
		return dao.getProduct(productId);
	}
	public Collection<Imgur> getProductPics(long productId, int howmany) {
		return dao.getProductPics(productId, howmany);
	}
	public Collection<Imgur> getProductPics(long productId) {
		return dao.getProductPics(productId);
	}
	public Imgur addProductPic(long productId, Imgur pic) {
		return dao.addProductPic(productId, pic);
	}
	public Imgur updatePic(long imgurId, String title, String description) {
		return dao.updatePic(imgurId, title, description);
	}
}
