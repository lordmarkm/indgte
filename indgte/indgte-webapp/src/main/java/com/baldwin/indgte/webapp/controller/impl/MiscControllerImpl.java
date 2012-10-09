package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.MiscController;

@Component
public class MiscControllerImpl implements MiscController {

	@Autowired
	private UserService userService;
	
	@Override
	public ModelAndView help(Principal principal, WebRequest request) {
		String loadhere = request.getParameter("loadhere");
		
		if(loadhere != null && Boolean.parseBoolean(loadhere)) {//return just the help jsp
			return render("help/help").mav();
		} else {//return the whole page with navbar and footer
			User user = userService.getFacebook(principal.getName());
			return render("help")
					.put("user", user)
					.mav();
		}
	}

}
