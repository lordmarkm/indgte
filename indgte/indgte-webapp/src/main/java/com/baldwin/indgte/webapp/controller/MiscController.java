package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handle stuff that doesn't fit anywhere else
 * 
 * @author mbmartinez
 */

@Controller
public interface MiscController {

	@RequestMapping(value = "/etc/help/", method = RequestMethod.GET)
	public ModelAndView help(HttpServletRequest request, Principal principal);

	@RequestMapping("/error/")
	public String error();
	
	@RequestMapping("/logout/")
	public String logout();
	
	@ExceptionHandler(Exception.class)
	public String redirectToError(Exception e);
	
}
