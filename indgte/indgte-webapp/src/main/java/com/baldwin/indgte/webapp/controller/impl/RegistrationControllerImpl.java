package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.common.IndgteUtil;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.webapp.controller.RegistrationController;
import com.baldwin.indgte.webapp.dto.RegistrationForm;


public class RegistrationControllerImpl implements RegistrationController {
	static Logger log = LoggerFactory.getLogger(RegistrationControllerImpl.class);
	
	@Autowired private IndgteUtil util;
	
	public ModelAndView registrationForm() {
		return render("regform")
				.addObject("categories", util.getBean("categories"))
				.mav();
	}

	public ModelAndView updateProfile() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		BusinessProfile profile = (BusinessProfile) auth.getPrincipal();
		RegistrationForm regform = new RegistrationForm(profile);
		
		return render("regform")
				.addObject("categories", util.getBean("categories"))
				.addObject("regform", regform)
				.mav();
	}

	public ModelAndView saveProfile(RegistrationForm regform) {
		// TODO Auto-generated method stub
		return null;
	}
}
