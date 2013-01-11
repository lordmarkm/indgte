package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.MiscController;
import com.baldwin.indgte.webapp.misc.ConstantsInserterBean;

@Component
public class MiscControllerImpl implements MiscController {

	static Logger log = LoggerFactory.getLogger(MiscControllerImpl.class);
	
	@Autowired
	private UserService users;
	
	@Autowired
	private ConstantsInserterBean constants;
	
	@Override
	public ModelAndView help(HttpServletRequest request, Principal principal) {
		
		log.info("{} has requested the help page", principal == null ? request.getRemoteAddr() : principal.getName());
		
		ModelAndView mav = render("help")
				.put("user", principal == null ? null : users.getExtended(principal.getName()))
				.mav();
		constants.insertConstants(mav);
		return mav;
		
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