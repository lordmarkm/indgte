package com.baldwin.indgte.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthenticationController {
	@RequestMapping("/sociallogin")
	public String sociallogin() {
		return "oauthlogin";
	}
}
