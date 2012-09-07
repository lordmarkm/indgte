package com.baldwin.indgte.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller @RequestMapping("/profile/")
public interface ProfileController {
	final String OWNPROFILE = "/";
	
	@RequestMapping("/{domain}")
	ModelAndView profile(String domain);
}
