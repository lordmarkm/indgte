package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.common.IndgteUtil;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.RegistrationController;
import com.baldwin.indgte.webapp.dto.RegistrationForm;

@Component
public class RegistrationControllerImpl implements RegistrationController {
	static Logger log = LoggerFactory.getLogger(RegistrationControllerImpl.class);
	
	@Autowired 
	private IndgteUtil util;
	
	@Autowired
	private BusinessService bizService;
	
	@Autowired
	private UserService userService;
	
	public ModelAndView registrationForm(RegistrationForm regForm) {
		log.debug("Registration form requested.");
		
		return render("regform")
				.addObject("categories", util.getBean("subcategories"))
				.addObject("regForm", regForm)
				.mav();
	}

	public ModelAndView updateProfile(@ModelAttribute BusinessProfile profile) {
		log.debug("Update form requested for {}", profile);
		RegistrationForm regform = new RegistrationForm(profile);
		return render("regform")
				.addObject("categories", util.getBean("categories"))
				.addObject("regform", regform)
				.mav();
	}

	public ModelAndView saveProfile(RegistrationForm regform) {
		BusinessProfile bizProfile = regform.getBusinessProfile();
		
		String facebookId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userService.getUser(facebookId);
		
		bizProfile.addAdmin(user);
		
		log.info("{} is being saved by {}", bizProfile, user);
		
		bizService.save(regform.getBusinessProfile());
		return render("profile")
				.addObject("user", user)
				.addObject("profile", regform.getBusinessProfile())
				.mav();
	}
}
