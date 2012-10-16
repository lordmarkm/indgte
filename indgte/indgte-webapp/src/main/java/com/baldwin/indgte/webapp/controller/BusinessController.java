package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.Product;

/**
 * Controller for Business Operations (owner only, unless we handle payments and stuff in The Future)
 * @author mbmartinez
 */

@Controller
@RequestMapping("/b/")
@SessionAttributes(types = Product.class)
public interface BusinessController {
	/*
	 * Category operations for domain
	 */
	
	@RequestMapping(value = "/categories/{domain}.json", method = RequestMethod.GET)
	public JSON getCategoriesJSON(Principal principal, String domain);
	
	@RequestMapping(value = "/categories/{domain}/{categoryId}", method = RequestMethod.GET)
	public ModelAndView viewcategory(Principal principal, String domain, long categoryId);
	
	@RequestMapping(value = "/categories/{domain}/{categoryId}/mainpic/", method = RequestMethod.POST)
	public JSON uploadCategoryPic(String domain, long categoryId, Imgur mainpic);
	
	@RequestMapping(value = "/newcategory/{domain}", method = RequestMethod.GET)
	public ModelAndView createCategoryPage(Principal principal, String domain, WebRequest request);
	
	@RequestMapping(value = "/newcategory/{domain}.json", method = RequestMethod.POST)
	public JSON createCategory(Principal principal, String domain, WebRequest request);
	
	/*
	 * Product operations for domain and category
	 */
	
	@RequestMapping(value = "/products/{domain}/{categoryId}.json", method = RequestMethod.GET)
	public JSON getProductsJSON(Principal principal, String domain, long categoryId);
	
	@RequestMapping(value = "/products/{domain}/{productId}", method = RequestMethod.GET)
	public ModelAndView viewProduct(Principal principal, String domain, long productId);
	
	@RequestMapping(value = "/products/{domain}/{productId}/pics.json", method = RequestMethod.GET)
	public JSON getProductPics(String domain, long productId);

	@RequestMapping(value = "/products/{domain}/{productId}/pics/{howmany}.json", method = RequestMethod.GET)
	public JSON getProductPics(String domain, long productId, int howmany);
	
	@RequestMapping(value = "/products/{domain}/{productId}/pics.json", method = RequestMethod.POST)
	public JSON addProductPic(String domain, long productId, Imgur pic);
	
	/**
	 * Create a new product for business domain and category
	 * Note: if categoryId = 0, then category is 'Uncategorized'
	 */
	@RequestMapping(value = "/newproduct/{domain}/{categoryId}", method = RequestMethod.GET)
	public ModelAndView createProductPage(Principal principal, String domain, long categoryId, WebRequest request);
	
	@RequestMapping(value = "/newproduct/{domain}/{categoryId}.json", method = RequestMethod.POST)
	public JSON createProduct(Principal principal, String domain, long categoryId, Product product);
	
	@RequestMapping(value = "/editproduct/{domain}/{productId}", method = RequestMethod.GET)
	public ModelAndView editProductPage(Principal principal, String domain, long productId, Model model);
	
	@RequestMapping(value = "/editproduct/{domain}/{productId}", method = RequestMethod.POST)
	public ModelAndView editProduct(Principal principal, String domain, long productId, Product product);
	
	/*
	 * Picture operations
	 */
	
	@RequestMapping(value = "/editpics/{domain}/{imgurId}.json", method = RequestMethod.POST)
	public JSON editPic(Principal principal, String domain, long imgurId, String title, String description);
}
