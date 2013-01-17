package com.baldwin.indgte.persistence.service;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.model.BusinessGroup;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.Product;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.pers‪istence.dao.BusinessDao;
import com.baldwin.indgte.pers‪istence.dao.BusinessGroupDao;
import com.baldwin.indgte.pers‪istence.dao.UserDao;

@Service
public class BusinessService {
	static Logger log = LoggerFactory.getLogger(BusinessService.class);
	
	@Autowired UserDao users;
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
	public void deleteProduct(String name, Long productId) throws IllegalAccessException {
		Product product = dao.getProduct(productId);
		if(product.getCategory().getBusiness().getOwner().getUsername().equals(name)) {
			dao.deleteProduct(productId);
		} else {
			throw new IllegalAccessException("You do not have permission to delete this object");
		}
	}
	public void editInfo(String username, String domain, String info) {
		dao.editInfo(username, domain, info);
	}
	public String getInfo(String domain) {
		return dao.getInfo(domain);
	}
	public List<BusinessProfile> getSuggestions(BusinessProfile business) {
		return dao.getSuggestions(business);
	}
	public void setSoldout(String name, long productId, boolean isSoldout) {
		Product product = dao.getProduct(productId);
		if(product.getCategory().getBusiness().getOwner().getUsername().equals(name)) {
			dao.setSoldout(productId, isSoldout);
		}
	}
	public void deleteCategory(boolean moderator, String name, long categoryId) {
		Category category = dao.getCategory(categoryId);
		if(moderator) {
			log.info("Moderator {} is deleting category {} in {}", category.getName(), category.getBusiness().getName());
		}
		if(moderator || category.getBusiness().getOwner().getUsername().equals(name)) {
			dao.deleteCategory(category.getId());
		}
	}
	public void editCategory(boolean moderator, String name, long categoryId, String categoryName, String description) {
		UserExtension user = users.getExtended(name);
		Category category = dao.getCategory(categoryId);
		
		if(category.getBusiness().getOwner().equals(user)) {
			log.info("{} has updated category with id {}. Name: {} description: {}", name, categoryId, categoryName, description);
			dao.updateCategory(categoryId, categoryName, description);
		} else if(moderator) {
			log.info("[moderator] {} has updated category with id {}. Name: {} description: {}", name, categoryId, categoryName, description);
			dao.updateCategory(categoryId, categoryName, description);
		} else {
			throw new IllegalStateException("You do not have permissions to edit this category.");
		}
	}
}
