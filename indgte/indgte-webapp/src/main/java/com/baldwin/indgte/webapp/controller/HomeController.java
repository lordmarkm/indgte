package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public interface HomeController {

	@RequestMapping
	public ModelAndView home(Principal principal);
	
	/**
	 * View a business domain directly
	 */
	@RequestMapping("/{domain}")
	public ModelAndView businessProfile(Principal principal, @PathVariable String domain);
	
}
