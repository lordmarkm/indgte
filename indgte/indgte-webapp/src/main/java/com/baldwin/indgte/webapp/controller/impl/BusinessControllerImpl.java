package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.clean;
import static com.baldwin.indgte.webapp.controller.MavBuilder.isAjax;
import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.Product;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.BusinessController;
import com.baldwin.indgte.webapp.controller.JSON;

@Component
public class BusinessControllerImpl implements BusinessController {
	static Logger log = LoggerFactory.getLogger(BusinessControllerImpl.class);
	
	@Autowired
	private UserService users;
	
	@Autowired
	private BusinessService businesses;
	
	@Value("${imgur.devkey}")
	private String imgurKey;
	
	@Override
	public @ResponseBody JSON getCategoriesJSON(Principal principal, @PathVariable String domain) {
		return JSON.ok().put("categories", businesses.getCategories(domain, true));
	}
	
	@Override
	public ModelAndView viewcategory(Principal principal, @PathVariable String domain, @PathVariable long categoryId) {
		BusinessProfile business = businesses.get(domain);
		Category category = businesses.getCategory(categoryId);
		User user = users.getFacebook(principal.getName());
		
		return render(user, "viewcategory")
				.put("business", business)
				.put("owner", business.getOwner().getUsername().equals(user.getUsername()))
				.put("category", category)
				.put("imgurKey", imgurKey)
				.mav();
	}
	
	@Override
	public ModelAndView createCategoryPage(Principal principal, @PathVariable String domain, WebRequest request) {
		BusinessProfile business = businesses.get(domain);
		
		if(isAjax(request)) {
			return render("businessops/createcategory")
					.put("business", business).mav();
		} else {
			User user = users.getFacebook(principal.getName());
			return render(user, "createcategory")
					.put("business", business).mav();
		}
	}

	@Override
	public @ResponseBody JSON createCategory(Principal principal, @PathVariable String domain, WebRequest request) {
		try {
			String name = clean(request.getParameter("name"));
			String description = clean(request.getParameter("description"));
			Category category = businesses.createCategory(domain, name, description);
			return JSON.ok().put("category", category);
		} catch (Exception e) {
			log.warn("Error creating category", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON uploadCategoryPic(@PathVariable String domain, @PathVariable long categoryId, Imgur mainpic) {
		try {
			businesses.saveCategoryMainpic(domain, categoryId, mainpic);
		} catch(Exception e) {
			return JSON.status500(e);
		}
		return JSON.ok();
	}
	
	@Override
	public @ResponseBody JSON getProductsJSON(Principal principal, @PathVariable String domain, @PathVariable long categoryId) {
		log.debug("JSON getProducts(...) called. Domain: {}, categoryId: {}", domain, categoryId);
		try {
			Collection<Product> products = businesses.getProducts(categoryId);
			log.debug("Found {} results", products.size());
			return JSON.ok().put("products", products);
		} catch (Exception e) {
			log.error("Exception getting products. categoryId: {}", categoryId, e);
			return JSON.status500(e);
		}
	}
	
	@Override
	public ModelAndView createProductPage(Principal principal, @PathVariable String domain, @PathVariable long categoryId, WebRequest request) {
		log.debug("Product creation page requested?");
		
		User user = users.getFacebook(principal.getName());
		return render(user, "createproduct").mav();
	}

	@Override
	public @ResponseBody JSON createProduct(Principal principal, @PathVariable String domain, @PathVariable long categoryId, @RequestBody Product product) {
		log.debug("JSON createProduct(...) called. Product: {}", product);
		
		product.setName(clean(product.getName()));
		product.setDescription(clean(product.getDescription()));
		businesses.createProduct(categoryId, product);
		return JSON.ok().put("product", product);
	}

	@Override
	public ModelAndView viewProduct(Principal principal, @PathVariable String domain, @PathVariable long productId) {
		log.debug("ModelAndView for Product with id {} requested.", productId);
		
		User user = users.getFacebook(principal.getName());
		BusinessProfile business = businesses.get(domain);
		Product product = businesses.getProduct(productId);
		
		return render(user, "viewproduct")
					.put("business", business)
					.put("product", product)
					.put("owner", business.getOwner().getUsername().equals(user.getUsername()))
					.put("imgurKey", imgurKey)
					.mav();
	}

	@Override
	public @ResponseBody JSON getProductPics(@PathVariable String domain, @PathVariable long productId) {
		try {
			return JSON.ok().put("imgurs", businesses.getProductPics(productId));
		} catch (Exception e) {
			log.error("Exception getting product pics", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON addProductPic(@PathVariable String domain, @PathVariable long productId, Imgur pic) {
		log.debug("JSON addProductPic(...) called. productId: {}, pic: {}", productId, pic);
		try {
			return JSON.ok().put("imgur", businesses.addProductPic(productId, pic));
		} catch (Exception e) {
			log.error("Exception adding product pic", e);
			return JSON.status500(e);
		}
	}
}
