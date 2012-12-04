package com.baldwin.indgte.webapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthenticationController {
	static Logger log = LoggerFactory.getLogger(AuthenticationController.class);
	
	@RequestMapping("/login/")
	public String sociallogin(HttpServletRequest request) {
		String referrer = request.getHeader("referer");
		log.debug("Referrer: {}", referrer);
		
		if(null != referrer) {
			request.getSession(true).setAttribute("originalUrl", referrer);
		}
		
		return "oauthlogin";
	}
}
