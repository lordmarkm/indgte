package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.constants.Initializable;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.HomeController;

@Component
public class HomeControllerImpl implements HomeController {
	
	@Autowired
	private UserService users;
	
	@Value("${imgur.devkey}")
	private String imgurKey;
	
	@RequestMapping
	public ModelAndView home(Principal principal) {
		UserExtension user = users.getExtended(principal.getName(), Initializable.businesses);
		return render(user, "home")
				.put("imgurKey", imgurKey)
				.mav();
	}
	
}
