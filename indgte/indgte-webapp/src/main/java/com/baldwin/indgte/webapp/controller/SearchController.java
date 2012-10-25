package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/s/")
public interface SearchController {
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView searchpage();
	
	@RequestMapping("/{term}")
	public ModelAndView search(Principal principal, String term);
	
	@RequestMapping("/{term}.json")
	public JSON autocomplete(Principal principal, String term);
	
	@RequestMapping("/own/{term}.json")
	public JSON autocompleteOwn(Principal principal, String term);
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/index/")
	public void reindex();
	
}