package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.redirect;
import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.BusinessProfile;
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
	
	public ModelAndView regform(RegistrationForm regform, ModelMap model) {
		log.debug("Registration form requested.");
		
		model.put("regform", regform);
		
		return render("register/regform")
				.put("regForm", regform)
				.mav();
	}

	public ModelAndView savePageOne(Principal principal, @ModelAttribute RegistrationForm regform) {
		log.info("Registration flow, Page 1 save request for domain {} from {}.", regform.getDomain(), principal.getName());
		
		businessService.save(regform.getBusinessProfile(), principal.getName());
		
		return render("register/pinpointlocation")
				.put("regform", regform)
				.mav();
	}
	
	public ModelAndView savePageTwo(@ModelAttribute RegistrationForm regform) {
		log.info("Registration flow, Page 2 save request for domain {}.", regform.getDomain());
		
		businessService.update(regform.getBusinessProfile());
		
		return redirect("/p/" + regform.getDomain())
				.mav();
	}
}
