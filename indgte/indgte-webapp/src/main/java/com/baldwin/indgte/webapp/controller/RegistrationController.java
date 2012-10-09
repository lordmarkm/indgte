package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.webapp.dto.RegistrationForm;

/**
 * Handles Business Profiles only. Personal profiles are all social-network managed.
 * 
 * 1. Create Profile
 * 2. Update Profile
 * 
 * @author mbmartinez
 */

@Controller 
@RequestMapping("/r/")
public interface RegistrationController {
	final static String URL_UPDATE = "/update/";
	final static String URL_SAVE_PAGE1 = "/save/1/";
	final static String URL_SAVE_PAGE2 = "/save/2/";
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView regform(Principal principal, RegistrationForm regform, ModelMap model, WebRequest request);
	
	/**
	 * Save essential business information
	 * @param regform
	 * @return Page 2 of registration flow
	 */
	@RequestMapping(value = URL_SAVE_PAGE1, method = RequestMethod.POST)
	public ModelAndView savePageOne(Principal principal, RegistrationForm regform);
	
	/**
	 * Save request after User pinpoints business location on a Google Map. Not much will actually happen
	 * in this method since we will be using Google Places to save Business information. Perhaps save the Place
	 * ID?
	 * @param regform
	 * @return redirect to profile page
	 */
	@RequestMapping(value = URL_SAVE_PAGE2, method = RequestMethod.POST)
	public ModelAndView savePageTwo(Principal principal, RegistrationForm regform);
}
