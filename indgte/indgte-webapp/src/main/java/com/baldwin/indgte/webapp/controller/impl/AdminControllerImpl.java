package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.pers‪istence.dao.InteractiveDao;
import com.baldwin.indgte.pers‪istence.dao.SearchDao;
import com.baldwin.indgte.webapp.controller.AdminController;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.misc.DaoTesterConstants;

@Component
public class AdminControllerImpl implements AdminController {

	static Logger log = LoggerFactory.getLogger(AdminControllerImpl.class);
	
	@Autowired
	private UserService users;
	
	@Autowired 
	private InteractiveDao postDao;
	
	@Autowired
	private BusinessService businesses;
	
	@Autowired
	private SearchDao search;
	
	@Override
	public ModelAndView daoTester(Principal principal) {
		UserExtension user = users.getExtended(principal.getName());
		return render(user, "daotester").mav();
	}

	@SuppressWarnings("deprecation")
	@Override
	public @ResponseBody JSON daoTest(@PathVariable String name, WebRequest request) {
		log.debug("Request params: {}", request.getParameterMap().keySet());
		
		DaoTesterConstants method = DaoTesterConstants.valueOf(name);
		try {
		switch(method) {
			case postsGetByDomain:
				String domain = request.getParameter("domain");
				String start = request.getParameter("start");
				String howmany = request.getParameter("howmany");
				
				log.info("Testing postsGetByDomain with args {}", domain + ":" + start + ":" + howmany);
				
				Set<Post> posts = postDao.getByDomain(domain, Integer.parseInt(start), Integer.parseInt(howmany));
				return JSON.ok().put("posts", posts);
			case postsGetSubposts:
				String username = request.getParameter("username");
				try{
					Collection<Post> subPosts = postDao.getSubposts(username, 0, 15);
					if(null == subPosts) {
						return JSON.status404().put("message", username + " not found.");
					}
					return JSON.ok().put("posts", subPosts);
				} catch (Exception e) {
					return JSON.status500(e);
				}
			case test:
				search.test();
				return JSON.ok();
			default:
				return JSON.status404();
			}
		} catch (Exception e) {
			log.error("Exception testing dao method", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON getCategory(@PathVariable String name) {
		Collection<Category> categories = businesses.getCategories(name, true);
		log.debug("Found {} categories for {}", categories.size(), name);
		return JSON.ok().put("cat", categories);
	}
}
