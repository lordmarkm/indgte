package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.persistence.constants.Initializable.*;
import static com.baldwin.indgte.webapp.controller.MavBuilder.redirect;
import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;

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

import com.baldwin.indgte.persistence.constants.Initializable;
import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.persistence.service.InteractiveService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.controller.ProfileController;

@Component
public class ProfileControllerImpl implements ProfileController {
	static Logger log = LoggerFactory.getLogger(ProfileControllerImpl.class);
	
	@Autowired
	private BusinessService businesses;
	
	@Autowired
	private UserService users;
	
	@Autowired
	private InteractiveService posts;
	
	@Value("${imgur.devkey}")
	private String imgurKey;
	
	@Override
	public ModelAndView profile(Principal principal) {
		log.debug("Profile page requested by {}", principal);
		
		UserExtension user = users.getExtended(principal.getName());
		
		return render(user, "ownprofile")
				.put("authorities", SecurityContextHolder.getContext().getAuthentication().getAuthorities())
				.mav();
	}
	
	@Override
	public ModelAndView myBusinesses(Principal principal, WebRequest request) {
		log.debug("Profile page requested by {}", principal);
		
		UserExtension user = users.getExtended(principal.getName(), Initializable.businesses);
		
		return render(user, "businesses")
				.put("businesses", user.getBusinesses())
				.mav();
	}
	
	@Override
	public ModelAndView userProfile(Principal principal, @PathVariable String targetUsername) {
		UserExtension user = users.getExtended(principal.getName());
		UserExtension target = users.getExtended(targetUsername, wishlist, Initializable.businesses);

		return render(user, "userprofile")
					.put("target", target)
					.put("subscribed", posts.isSubscribed(principal.getName(), target.getId(), PostType.user))
					.mav();
	}
	
	@Override
	public @ResponseBody Imgur profilepic(@PathVariable String domain) {
		return businesses.getProfilepic(domain);
	}

	@Override
	public @ResponseBody JSON newProfilepic(@PathVariable String domain, Imgur profilepic) {
		businesses.saveProfilepic(domain, profilepic);
		return JSON.ok();
	}
	
	@Override
	public @ResponseBody JSON newCoverpic(@PathVariable String domain, Imgur coverpic) {
		businesses.saveCoverpic(domain, coverpic);
		return JSON.ok();
	}
}
