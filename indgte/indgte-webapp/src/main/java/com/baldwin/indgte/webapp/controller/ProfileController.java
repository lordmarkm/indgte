package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.Imgur;

/**
 * Handles both business and personal profiles
 * 
 * 1. View own (personal) profile
 * 2. View other people's personal profiles
 * 3. View business profiles
 * 
 * @author mbmartinez
 */

@Controller
@RequestMapping("/p/")
public interface ProfileController {
	
	/**
	 * Current U=user's profile
	 */
	@RequestMapping("/")
	public ModelAndView profile(Principal principal, WebRequest request);

	@RequestMapping("/user/{userId}")
	public ModelAndView userProfile(String userId);
	
	@RequestMapping("/businesses")
	public ModelAndView myBusinesses(Principal principal, WebRequest request);
	
	/**
	 * View a business domain directly
	 */
	@RequestMapping("/{domain}")
	public ModelAndView businessProfile(Principal principal, WebRequest request, String domain);

	/**
	 * Return a link to the business's existing profile pic
	 */
	@RequestMapping(value = "/{domain}/profilepic", method = RequestMethod.GET)
	public Imgur profilepic(String domain);
	
	/**
	 * The owner has uploaded a new profile pic for a business and we need to save the details
	 */
	@RequestMapping(value = "/{domain}/profilepic", method = RequestMethod.POST)
	public JSON newProfilepic(String domain, Imgur profilepic);

	@RequestMapping(value = "/{domain}/coverpic", method = RequestMethod.POST)
	public JSON newCoverpic(String domain, Imgur coverpic);
}
