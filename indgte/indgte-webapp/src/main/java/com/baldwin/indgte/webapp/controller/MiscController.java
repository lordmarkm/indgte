package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handle stuff that doesn't fit anywhere else
 * 
 * @author mbmartinez
 */

@Controller
public interface MiscController {

	@RequestMapping("/help/")
	public ModelAndView help(Principal principal);

	@ExceptionHandler(Exception.class)
	public String redirectToError(Exception e);
	
	@RequestMapping("/error/")
	public String error();
	
	@RequestMapping("/logout/")
	public String logout();
}
