package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
	
	@RequestMapping("/")
	public ModelAndView profile();
	
	@RequestMapping("/user/{displayName}")
	public ModelAndView userProfile(String displayName);
	
	@RequestMapping("/{domain}")
	public ModelAndView businessProfile(Principal principal, String domain);
	
}
