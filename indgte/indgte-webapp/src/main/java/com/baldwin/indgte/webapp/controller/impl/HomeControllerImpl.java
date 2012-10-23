package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.HomeController;

@Component
public class HomeControllerImpl implements HomeController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BusinessService businesses;
	
	@Value("${imgur.devkey}")
	private String imgurKey;
	
	@RequestMapping
	public ModelAndView home(Principal principal) {
		User user = userService.getFacebook(principal.getName());
		Collection<BusinessProfile> userBusinesses = businesses.getBusinesses(principal.getName());
		return render(user, "home")
				.put("businesses", userBusinesses)
				.put("imgurKey", imgurKey)
				.mav();
	}
	
}
