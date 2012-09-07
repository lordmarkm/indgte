package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
	@Autowired
	private BusinessService bizService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public ModelAndView profile(@PathVariable String domain) {
		log.debug("Profile requested for {}", domain);
		
		BusinessProfile profile = bizService.get(domain);
		String facebookId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userService.getUser(facebookId);
		
		boolean admin = false;
		if(profile.isAdmin(user)) {
			admin = true;
		}
		
		return render("profile")
				.addObject("profile", profile)
				.addObject("admin", admin)
				.mav();
	}
}
