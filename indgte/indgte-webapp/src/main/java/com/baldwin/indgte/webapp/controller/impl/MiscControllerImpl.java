package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.MavBuilder;
import com.baldwin.indgte.webapp.controller.MiscController;

@Component
public class MiscControllerImpl implements MiscController {

	static Logger log = LoggerFactory.getLogger(MiscControllerImpl.class);
	
	@Autowired
	private UserService users;
	
	@Override
	public ModelAndView help(Principal principal) {
		
		MavBuilder mav = render("help");
		
		if(null != principal) {
			mav.put("user", users.getExtended(principal.getName()));
		}
		
		return mav.mav();
		
	}

	@Override
	public String error() {
		return "error";
	}

	@Override
	public String redirectToError(Exception e) {
		log.debug("Got exception : {}", e);
		return "redirect:/error/";
	}
	
	@Override
	public String logout() {
		return "logout";
	}

}