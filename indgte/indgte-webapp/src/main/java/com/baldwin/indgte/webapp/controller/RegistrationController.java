package com.baldwin.indgte.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.webapp.dto.RegistrationForm;

/**
 * Handles Business Profiles only. Personal profiles are all social-network managed.
 * 
 * 1. Create Profile
 * 2. Update Profile
 * 3. View Profile
 * 
 * @author mbmartinez
 */

@Controller @RequestMapping("/register")
public interface RegistrationController {
	final String URL_REGISTER = "/";
	final String URL_UPDATE = "/update/";
	final String URL_SAVE = "/save/";
	
	@RequestMapping(URL_REGISTER)
	public ModelAndView registrationForm(RegistrationForm regForm);
	
	@RequestMapping(URL_UPDATE)
	public ModelAndView updateProfile(BusinessProfile bizProfile);
	
	@RequestMapping(URL_SAVE)
	public ModelAndView saveProfile(RegistrationForm regForm);
}
