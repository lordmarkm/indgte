package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/a/")
public interface AdminController {
	
	@RequestMapping(value="/daotest/", method = RequestMethod.GET)
	public ModelAndView daoTester(Principal principal);
	
	@RequestMapping("/daotest/{name}")
	public JSON daoTest(String name,  WebRequest request);
	
}
