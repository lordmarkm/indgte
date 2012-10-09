package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.*;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.RegistrationController;
import com.baldwin.indgte.webapp.dto.RegistrationForm;

@Component
@SessionAttributes(value={"regform"}, types={RegistrationForm.class})
public class RegistrationControllerImpl implements RegistrationController {
	static Logger log = LoggerFactory.getLogger(RegistrationControllerImpl.class);
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public ModelAndView regform(Principal principal, RegistrationForm regform, ModelMap model, WebRequest request) {
		log.debug("Registration form requested by {}", principal);
		
		User user = userService.getFacebook(principal.getName());
		
		ModelAndView mav = render(user)
			.put("regform", regform)
			.mav();
		
		if(isAjax(request)) {
			mav.setViewName("register/regform");
		} else {
			mav.setViewName("regform");
		}
		
		return mav;
	}

	public ModelAndView savePageOne(Principal principal, @ModelAttribute RegistrationForm regform) {
		log.info("Registration flow, Page 1 save request for domain {} from {}.", regform.getDomain(), principal.getName());
		User user = userService.getFacebook(principal.getName());
		businessService.save(regform.getBusinessProfile(), principal.getName());
		
		return render(user, "pinpointlocation")
				.put("regform", regform)
				.mav();
	}
	
	public ModelAndView savePageTwo(Principal principal, @ModelAttribute RegistrationForm regform) {
		log.info("Registration flow, Page 2 save request for domain {}.", regform.getDomain());
		businessService.update(regform.getBusinessProfile());
		
		return redirect("/p/" + regform.getDomain())
				.mav();
	}
}
