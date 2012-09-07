package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.redirect;
import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.webapp.controller.RegistrationController;
import com.baldwin.indgte.webapp.dto.RegistrationForm;

@Component
public class RegistrationControllerImpl implements RegistrationController {
	static Logger log = LoggerFactory.getLogger(RegistrationControllerImpl.class);
	
	@Autowired
	private BusinessService businessService;
	
	public ModelAndView registrationForm(RegistrationForm regForm) {
		log.debug("Registration form requested.");
		
		return render("regform")
				.addObject("regForm", regForm)
				.mav();
	}

	public ModelAndView saveProfile(RegistrationForm regform) {
		BusinessProfile businessProfile = regform.getBusinessProfile();
		
		String domain = businessProfile.getDomain();
		log.info("{} saved.", domain);
		
		businessService.save(regform.getBusinessProfile());
		return redirect("/profile/" + domain)
				.mav();
	}
}
