package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.clean;
import static com.baldwin.indgte.webapp.controller.MavBuilder.redirect;
import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.BusinessController;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.controller.MavBuilder;
import com.baldwin.indgte.webapp.misc.ConstantsInserterBean;
import com.baldwin.indgte.webapp.misc.DgteTagWhitelist;

@Component
public class BusinessControllerImpl implements BusinessController {
	static Logger log = LoggerFactory.getLogger(BusinessControllerImpl.class);
	
	@Autowired
	private UserService users;
	
	@Autowired
	private BusinessService businesses;
	
	@Autowired
	private ConstantsInserterBean constants;
	
	@Override
	public @ResponseBody JSON getInfo(@PathVariable String domain) {
		return JSON.ok().put("info", businesses.getInfo(domain));
	}
	
	@Override
	public String editInfo(Principal principal, @PathVariable String domain, @RequestParam String info) {
		log.debug("Edit info request received for {}", domain);
		info = Jsoup.clean(info, DgteTagWhitelist.relaxed());
		businesses.editInfo(principal.getName(), domain, info);
		return "redirect:/" + domain;
	}
	
	@Override
	public @ResponseBody JSON getCategoriesJSON(Principal principal, @PathVariable String domain) {
		return JSON.ok().put("categories", businesses.getCategories(domain, true));
	}
	
	@Override
	public ModelAndView viewcategory(Principal principal, @PathVariable String domain, @PathVariable long categoryId) {
		BusinessProfile business = businesses.get(domain);
		Category category = businesses.getCategory(categoryId);
		
		if(null == principal) {
			log.info("Category {} ({}) view requested", category.getName(), domain);
		} else {
			log.info("Category {} ({}) view requested by {}", category.getName(), domain, principal.getName());
		}
		
		MavBuilder mav = render("viewcategory")
				.put("business", business)
				.put("category", category);
		
		//metadata
		if(null != category.getImgur()) {
			mav.thumbnail(category.getImgur().getSmallSquare());
		}
		if(null != category.getDescription() && category.getDescription().length() > 0) {
			mav.description(category.getDescription());
		}
		
		if(null != principal) {
			UserExtension user = users.getExtended(principal.getName());
			mav.put("user", user)
    		   .put("owner", business.getOwner().equals(user));
		}
		
		constants.insertConstants(mav);
		
		return mav.mav();
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
		log.info("Category creation page for domain {} requested by {}", domain, principal.getName());
		
		BusinessProfile business = businesses.get(domain);
		UserExtension user = users.getExtended(principal.getName());
		return render(user, "createcategory")
				.put("business", business).mav();
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
			log.error("Error uploading pic", e);
			return JSON.status500(e);
		}
		return JSON.ok();
	}

	@Override
	public ModelAndView editCategory(Principal principal, @PathVariable long categoryId, String name, String description) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean mod = isModerator(auth);
		
		name = Jsoup.clean(name, Whitelist.none());
		description = Jsoup.clean(description.replace("\n", "jsoupbr2nl"), Whitelist.none()).replace("jsoupbr2nl", "\n");
		businesses.editCategory(mod, principal.getName(), categoryId, name, description);
		
		return redirect("/b/categories/" + categoryId).mav();
	}
	
	@Override
	public @ResponseBody JSON deleteCategory(Principal principal, @PathVariable long categoryId) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			boolean moderator = isModerator(auth);
			businesses.deleteCategory(moderator, principal.getName(), categoryId);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Error deleting category.", e);
			return JSON.status500(e);
		}
	}

	
	@Override
	public @ResponseBody JSON getProductsJSON(Principal principal, @PathVariable String domain, @PathVariable long categoryId) {
		log.debug("JSON getProducts(...) called. Domain: {}, categoryId: {}", domain, categoryId);
		try {
			List<Product> products = (List<Product>) businesses.getProducts(categoryId);
			Collections.sort(products);
			log.debug("Found {} results", products.size());
			return JSON.ok().put("products", products);
		} catch (Exception e) {
			log.error("Exception getting products. categoryId: {}", categoryId, e);
			return JSON.status500(e);
		}
	}
	
	@Override
	public ModelAndView createProductPage(Principal principal, @PathVariable String domain, @PathVariable long categoryId, WebRequest request) {
		log.info("Product creation page for domain {} requested by {}", domain, principal.getName());
		
		UserExtension user = users.getExtended(principal.getName());
		return render(user, "createproduct").mav();
	}

	@Override
	public @ResponseBody JSON createProduct(Principal principal, @PathVariable String domain, @PathVariable long categoryId, @RequestBody Product product) {
		product.setName(clean(product.getName()));
		product.setDescription(clean(product.getDescription()));
		businesses.createProduct(categoryId, product);
		return JSON.ok().put("product", product);
	}

	@Override
	public ModelAndView viewProduct(Principal principal, @PathVariable String domain, @PathVariable long productId) {
		BusinessProfile business = businesses.get(domain);
		Product product = businesses.getProduct(productId);
		List<BusinessProfile> suggestions = businesses.getSuggestions(business);

		log.info("Product {} requested by {}.", product.getName(), principal == null ? "Anonymous" : principal);
		
		MavBuilder mav = render("viewproduct")
					.put("business", business)
					.put("suggestions", suggestions)
					.put("product", product);
		
		//metadata
		if(null != product.getImgur()) {
			mav.thumbnail(product.getImgur().getSmallSquare());
		}
		if(null != product.getDescription() && product.getDescription().length() > 0) {
			mav.description(product.getDescription());
		}
		
		if(null != principal) {
			UserExtension user = users.getExtended(principal.getName(), Initializable.wishlist);
			mav.put("user", user)
			   .put("owner", business.getOwner().equals(user))
			   .put("inwishlist", user.inWishlist(product));
		}
		
		return mav.mav();
	}

	@Override
	public @ResponseBody JSON toggleSoldout(Principal principal, @PathVariable long productId, @PathVariable boolean isSoldout) {
		try {
			log.debug("{} wants to mark product with id {} as {}", principal.getName(), productId, isSoldout ? "sold out" : "available");
			businesses.setSoldout(principal.getName(), productId, isSoldout);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Error toggling soldout", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON delete(Principal principal, @PathVariable long productId) {
		try {
			log.debug("{} wants to delete product with id {}", principal.getName(), productId);
			businesses.deleteProduct(principal.getName(), productId);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Exception deleting product", e);
			return JSON.status500(e);
		}
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
		UserExtension user = users.getExtended(principal.getName());
		BusinessProfile business = businesses.get(domain);
		Product product = businesses.getProduct(productId);
		model.addAttribute("product", product);
		
		log.info("Edit product requested for {} ({}) by {}", product.getName(), domain, principal.getName());
		
		return render(user, "editproduct")
					.put("business", business)
					.put("product", product)
					.mav();
	}

	@Override
	public ModelAndView editProduct(Principal principal, @PathVariable String domain, @PathVariable long productId, @ModelAttribute("product") Product product) {
		log.info("Edit form submitted for {} ({}) by {}", product.getName(), domain, principal.getName());
		
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

	@Override
	public String redirectCategory(@PathVariable long categoryId) {
		Category category = businesses.getCategory(categoryId);
		return "redirect:/b/categories/" + category.getBusiness().getDomain() + "/" + categoryId;
	}

	@Override
	public @ResponseBody JSON deleteBusiness(Principal principal, @PathVariable Long id) {
		try {
			businesses.delete(id);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Exception deleting business", e);
			return JSON.status500(e);
		}
	}
	
	private boolean isModerator(Authentication auth) {
		log.debug("Checking authentication. Authorities: {}", auth.getAuthorities());
		for(GrantedAuthority g : auth.getAuthorities()) {
			if(g.getAuthority().equals("ROLE_MODERATOR")) return true;
		}
		return false;
	}
	
	@ExceptionHandler(Exception.class)
	public String error(Exception e) {
		log.error("Exception!", e);
		return "redirect:/error/";
	}

}
