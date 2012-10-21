package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.isAjax;
import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.persistence.service.PostsService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.controller.ProfileController;

@Component
public class ProfileControllerImpl implements ProfileController {
	static Logger log = LoggerFactory.getLogger(ProfileControllerImpl.class);
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PostsService posts;
	
	@Value("${imgur.devkey}")
	private String imgurKey;
	
	@Override
	public ModelAndView profile(Principal principal, WebRequest request) {
		log.debug("Profile page requested by {}", principal);
		
		User user = userService.getFacebook(principal.getName());
		String loadhere = request.getParameter("loadhere");
		
		ModelAndView mav = new ModelAndView()
							.addObject("user", user)
							.addObject("authorities", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		
		if(loadhere != null && Boolean.parseBoolean(loadhere)) {
			mav.setViewName("profile/ownprofile");
		} else {
			mav.setViewName("ownprofile");
		}
		
		return mav;
	}
	
	@Override
	public ModelAndView myBusinesses(Principal principal, WebRequest request) {
		log.debug("Profile page requested by {}", principal);
		
		User fbUser = userService.getFacebook(principal.getName());
		Collection<BusinessProfile> businesses = businessService.getBusinesses(principal.getName());
		
		String loadhere = request.getParameter("loadhere");
		
		ModelAndView mav = new ModelAndView()
							.addObject("user", fbUser)
							.addObject("businesses", businesses);
		
		if(loadhere != null && Boolean.parseBoolean(loadhere)) {
			mav.setViewName("profile/businesses");
		} else {
			mav.setViewName("businesses");
		}
		
		return mav;
	}
	
	@Override
	public ModelAndView userProfile(@PathVariable String userId) {
		//TODO
		return null;
	}
	
	@Override
	public ModelAndView businessProfile(Principal principal, WebRequest request, @PathVariable String domain) {
		log.debug("Profile requested for {}", domain);

		User user = userService.getFacebook(principal.getName());
		BusinessProfile business = businessService.get(domain);
		
		ModelAndView mav = render(user)
				.put("business", business)
				.put("subscribed", posts.isSubscribed(principal.getName(), business.getId()))
				.put("owner", business.getOwner().getUsername().equals(user.getUsername()))
				.put("imgurKey", imgurKey)
				.mav();
		
		if(isAjax(request)) {
			mav.setViewName("profile/businessprofile");
		} else {
			mav.setViewName("businessprofile");
		}
		
		return mav;
	}

	@Override
	public @ResponseBody Imgur profilepic(@PathVariable String domain) {
		return businessService.getProfilepic(domain);
	}

	@Override
	public @ResponseBody JSON newProfilepic(@PathVariable String domain, Imgur profilepic) {
		businessService.saveProfilepic(domain, profilepic);
		return JSON.ok();
	}
	
	@Override
	public @ResponseBody JSON newCoverpic(@PathVariable String domain, Imgur coverpic) {
		businessService.saveCoverpic(domain, coverpic);
		return JSON.ok();
	}
}
