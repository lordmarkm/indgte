package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/")
public interface HomeController {

	@RequestMapping
	public ModelAndView home(HttpServletRequest request, Principal principal);
	
	/**
	 * View a business domain directly
	 */
	@RequestMapping("/{domain}")
	public ModelAndView businessProfile(Principal principal, String domain);
	
	/**
	 * Link to home with predetermined post filter
	 */
	@RequestMapping("/filter/{tag}")
	public ModelAndView homeWithFilter(HttpServletRequest request, Principal principal, String tag);
	
	/**
	 * Redirect when user denies permission on 3rd party and
	 */
	@RequestMapping("/signin")
	public String failedPermissionsRedirect();
	
	/**
	 * robots.txt
	 */
	@RequestMapping("/robots.txt")
	public String robots(HttpServletRequest request);
	
	/**
	 * sitemap
	 */
	@RequestMapping("/sitemap.xml")
	public String sitemap(HttpServletRequest request);
	
	/**
	 * Weird favicon.ico exception
	 */
	@RequestMapping("/favicon.ico")
	public void favicon(HttpServletRequest request);
}
