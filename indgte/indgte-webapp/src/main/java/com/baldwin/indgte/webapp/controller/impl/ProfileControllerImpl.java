package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.ProfileController;

@Component
public class ProfileControllerImpl implements ProfileController {
	static Logger log = LoggerFactory.getLogger(ProfileControllerImpl.class);
	
	private static String defaultProviderId = "facebook";
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ConnectionRepository conns;
	
	@Override
	public ModelAndView profile(Principal principal) {
		log.debug("Profile page requested by {}", principal);
		
		User user = userService.getByUsername(principal.getName(), defaultProviderId);
		
		return render("ownprofile")
				.put("user", user)
				.mav();
	}
	
	@Override
	public ModelAndView userProfile(@PathVariable String displayName) {
		//TODO
		return null;
	}
	
	@Override
	public ModelAndView businessProfile(Principal principal, @PathVariable String domain) {
		log.debug("Profile requested for {}", domain);
		
		Connection<Facebook> conn = conns.findPrimaryConnection(Facebook.class);
		
		log.debug("Display name {}", conn.getDisplayName());
		
		BusinessProfile profile = businessService.get(domain);
		
		return render("profile/profile")
				.put("business", profile)
				.mav();
	}
}
