package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.clean;
import static com.baldwin.indgte.webapp.controller.MavBuilder.isAjax;
import static com.baldwin.indgte.webapp.controller.MavBuilder.redirect;
import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.constants.Initializable;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.Product;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.model.UserExtension;
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
	public @ResponseBody JSON getCategory(@PathVariable String domain, @PathVariable long categoryId, @PathVariable int howmany) {
		try {
			Category category = businesses.getCategoryWithProducts(categoryId);
			int difference = category.getProducts().size() - howmany;
			Collection<Product> products;
			if(difference > 0) {
				products = new ArrayList<Product>(category.getProducts()).subList(0, howmany);
			} else {
				products = category.getProducts();
			}
			log.debug("Returning {} and {} products", category.getName(), products.size());
			return JSON.ok().put("category", category)
					.put("products", products)
					.put("moreproducts", difference); //again, @JsonIgnore
		} catch (Exception e) {
			return JSON.status500(e);
		}
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
		
		UserExtension user = users.getExtended(principal.getName(), Initializable.wishlist);
		BusinessProfile business = businesses.get(domain);
		Product product = businesses.getProduct(productId);
		
		return render(user.getUser(), "viewproduct")
					.put("business", business)
					.put("product", product)
					.put("owner", business.getOwner().equals(user.getUser()))
					.put("inwishlist", user.inWishlist(product))
					.put("imgurKey", imgurKey)
					.mav();
	}

	@Override
	public String viewProduct(@PathVariable long productId) {
		String domain = businesses.findDomainForProductId(productId);
		return "redirect:/b/products/" + domain + "/" + productId;
	}
	
	@Override
	public @ResponseBody JSON getProductWithPics(@PathVariable String domain, @PathVariable long productId, @PathVariable int howmany) {
		try {
			Product product = businesses.getProductWithPics(productId);
			int difference = product.getPics().size() - howmany;
			
			List<Imgur> pics;
			if(difference > 0) {
				pics = product.getPics().subList(0, howmany);
			} else {
				pics = product.getPics();
			}
			
			return JSON.ok().put("product", product)
					.put("pics", pics) //necessary because of JsonIgnore
					.put("morepics", difference);
		} catch (Exception e) {
			return JSON.status500(e);
		}
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
	public @ResponseBody JSON getProductPics(@PathVariable String domain, @PathVariable long productId, @PathVariable int howmany) {
		try {
			return JSON.ok().put("imgurs", businesses.getProductPics(productId, howmany));
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

	@Override
	public ModelAndView editProductPage(Principal principal, @PathVariable String domain, @PathVariable long productId, Model model) {
		User user = users.getFacebook(principal.getName());
		BusinessProfile business = businesses.get(domain);
		Product product = businesses.getProduct(productId);
		model.addAttribute("product", product);
		
		log.debug("Edit product requested for {}-{}", product.getId(), product.getName());
		
		return render(user, "editproduct")
					.put("business", business)
					.put("product", product)
					.mav();
	}

	@Override
	public ModelAndView editProduct(Principal principal, @PathVariable String domain, @PathVariable long productId, @ModelAttribute("product") Product product) {
		log.debug("Edit form submitted for {}-{}", product.getId(), product.getName());
		product.setName(clean(product.getName()));
		product.setDescription(clean(product.getDescription()));
		businesses.update(product);
		
		return redirect("/b/products/" + domain + "/" + productId).mav();
	}

	@Override
	public @ResponseBody JSON editPic(Principal principal, @PathVariable String domain, @PathVariable long imgurId, Imgur imgur) {
		try {
			return JSON.ok().put("imgur", businesses.updatePic(imgurId, clean(imgur.getTitle()), clean(imgur.getDescription())));
		} catch (Exception e) {
			log.error("Exception updating picture", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON hidePics(Principal principal, @PathVariable String domain, @RequestParam("imgurIds[]") List<Long> imgurIds) {
		log.debug("Pics to hide: " + imgurIds);
		try {
			businesses.hidePics(imgurIds);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Exception hiding pics", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON showPics(Principal principal, @PathVariable String domain, @RequestParam("imgurIds[]") List<Long> imgurIds) {
		log.debug("Pics to unhide: " + imgurIds);
		try {
			businesses.unhidePics(imgurIds);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Exception unhiding pics", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON deletePics(Principal principal, @PathVariable String domain,	@PathVariable long productId, @RequestParam("imgurIds[]") List<Long> imgurIds) {
		log.debug("Pics to delete: " + imgurIds);
		try {
			businesses.deletePics(productId, imgurIds);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Exception deleting pics", e);
			return JSON.status500(e);
		}
	}
}
