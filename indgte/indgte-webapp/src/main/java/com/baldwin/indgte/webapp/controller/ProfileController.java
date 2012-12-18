package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.webapp.controller.impl.ProfileControllerImpl;

/**
 * Handles both business and personal profiles
 * 
 * 1. View own (personal) profile
 * 2. View other people's personal profiles
 * 3. View business profiles
 * 
 * <p>{@link ProfileControllerImpl}
 * 
 * @author mbmartinez
 */

@Controller
@RequestMapping("/p/")
public interface ProfileController {
	
	/**
	 * Coconuts
	 */
	@RequestMapping("/user/grantcoconuts/{userId}")
	public String grantCoconuts(Principal principal, long userId, int howmany);
	
	/**
	 * Current U=user's profile
	 */
	@RequestMapping("/")
	public ModelAndView profile(Principal principal);

	@RequestMapping("/user/{targetUsername}")
	public ModelAndView userProfile(Principal principal, String targetUsername);
	
	@RequestMapping("/manage/")
	public ModelAndView manageAccount(Principal principal);
	
	@RequestMapping("/manage/lang/{localeStr}")
	public ModelAndView changeLocale(Principal principal, HttpServletRequest request, HttpServletResponse response, String locale);
	
	@RequestMapping("/businesses")
	public ModelAndView myBusinesses(Principal principal, WebRequest request);
	
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
