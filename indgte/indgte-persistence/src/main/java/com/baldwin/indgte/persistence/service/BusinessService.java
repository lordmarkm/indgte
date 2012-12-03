package com.baldwin.indgte.persistence.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.model.BusinessGroup;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.Product;
import com.baldwin.indgte.pers‪istence.dao.BusinessDao;
import com.baldwin.indgte.pers‪istence.dao.BusinessGroupDao;

@Service
public class BusinessService {
	@Autowired BusinessDao dao;
	@Autowired BusinessGroupDao cDao;
	public BusinessProfile get(String domain) {
		return dao.get(domain);
	}
	public BusinessProfile get(long businessId) {
		return dao.get(businessId);
	}
	/**
	 * @see BusinessDao#getForViewProfile(String, String)
	 */
	public Object[] getForViewProfile(String username, String domain) {
		return dao.getForViewProfile(username, domain);
	}
	public void save(BusinessProfile bizProfile, String owner) {
		dao.create(bizProfile, owner);
	}
	public void update(Object dirty) {
		dao.update(dirty);
	}
	public void saveOrUpdate(BusinessProfile businessProfile, String owner) {
		dao.saveOrUpdate(businessProfile, owner);
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
	public Category getCategoryWithProducts(long categoryId) {
		return dao.getCategoryWithProducts(categoryId);
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
	public void hidePics(List<Long> imgurIds) {
		dao.hidePics(imgurIds);
	}
	public void unhidePics(List<Long> imgurIds) {
		dao.unhidePics(imgurIds);
	}
	public void deletePics(long productId, List<Long> imgurIds) {
		dao.deletePics(productId, imgurIds);
	}
	public String findDomainForProductId(long productId) {
		return dao.findDomainForProductId(productId);
	}
	public Product getProductWithPics(long productId) {
		return dao.getProductWithPics(productId);
	}
	
	//category dao ops
	public BusinessGroup getCategory(String name) {
		return cDao.get(name);
	}
	public String getBusinessCategories(String firstLetter) {
		return cDao.getCategories(firstLetter);
	}
	public String getDomain(long id) {
		return dao.getDomain(id);
	}
	public void delete(Long id) {
		dao.delete(id);
	}
}
