package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.clean;
import static com.baldwin.indgte.webapp.controller.MavBuilder.redirect;
import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.BusinessCategory;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.controller.RegistrationController;
import com.baldwin.indgte.webapp.dto.RegistrationForm;

@Component
@SessionAttributes(value={"regform", "user"}, types={RegistrationForm.class, User.class})
public class RegistrationControllerImpl implements RegistrationController {
	static Logger log = LoggerFactory.getLogger(RegistrationControllerImpl.class);
	
	@Autowired
	private BusinessService businesses;
	
	@Autowired
	private UserService userService;
	
	@Override
	public ModelAndView regform(Principal principal, RegistrationForm regform) {
		log.debug("Registration form requested by {}", principal);
		User user = userService.getFacebook(principal.getName());
		
		if(null != regform) {
			log.debug("Extant business profile: " + regform.getBusinessProfile());
			regform.setBusinessProfile(new BusinessProfile());
		} else {
			log.warn("Regform is null.");
		}

		return render(user, "regform")
			.put("regform", regform)
			.mav();
	}

	@Override
	public @ResponseBody boolean isDomainTaken(@RequestParam String domain) {
		log.debug("Checking uniqueness of domain [{}]", domain);
		return null == businesses.get(domain);
	}
	
	public ModelAndView savePageOne(Principal principal, @ModelAttribute User user, @ModelAttribute RegistrationForm regform) {
		log.info("Registration flow, Page 1 save request for domain {} from {}.", regform.getDomain(), principal.getName());
		//User user = userService.getFacebook(principal.getName());
		
		return render(user, "choosecategory")
				.put("regform", regform)
				.mav();
	}
	
	public ModelAndView savePageTwo(Principal principal, @ModelAttribute User user, @ModelAttribute RegistrationForm regform) {
		log.debug("Registration flow, Page 2 save request form domain {}. Category is now [{}]", regform.getDomain(), regform.getCategory());
		//User user = userService.getFacebook(principal.getName());
		String name = clean(regform.getCategory(), false);
		BusinessCategory category = businesses.getCategory(name);
		regform.getBusinessProfile().setCategory(category);
		
		return render(user, "pinpointlocation")
				.put("regform", regform)
				.mav();		
	}
	
	public ModelAndView savePageThree(Principal principal, @ModelAttribute RegistrationForm regform) {
		log.info("Successful edit or registration of domain {} by {}.", regform.getDomain(), principal.getName());
		businesses.saveOrUpdate(regform.getBusinessProfile(), principal.getName());
		
		return redirect("/p/" + regform.getDomain())
				.mav();
	}

	@Override
	public @ResponseBody JSON getCategories(@PathVariable String firstLetter) {
		try {
			String categories = businesses.getBusinessCategories(firstLetter);
			return JSON.ok().put("categories", categories);
		} catch(Exception e) {
			return JSON.status500(e);
		}
	}
}
