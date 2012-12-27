package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.redirect;
import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.constants.Initializable;
import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.persistence.service.InteractiveService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.HomeController;
import com.baldwin.indgte.webapp.controller.MavBuilder;
import com.baldwin.indgte.webapp.misc.ConstantsInserterBean;

@Component
public class HomeControllerImpl implements HomeController {
	
	static Logger log = LoggerFactory.getLogger(HomeControllerImpl.class);
	
	@Autowired
	private UserService users;
	
	@Autowired
	private BusinessService businesses;
	
	@Autowired
	private InteractiveService interact;
	
	@Autowired
	private ConstantsInserterBean constants;
	
	@Override
	public ModelAndView home(Principal principal) {
		MavBuilder mav = render("home");

		if(null != principal) {
			UserExtension user = users.getExtended(principal.getName(), Initializable.businesses);
			mav.put("user", user);
		}
		
		constants.insertConstants(mav);
		return mav.mav();
	}
	
	@Override
	public ModelAndView businessProfile(Principal principal, @PathVariable("domain") String domain) {
		log.debug("Profile requested for {}", domain);

		if(null == principal) {
			//user is anon
			BusinessProfile business = businesses.get(domain);
			if(null == business) {
				try {
					business = businesses.get(Long.parseLong(domain));
				} catch (Exception e) {
					throw new IllegalArgumentException("Unknown domain " + domain);
				}
			}
			
			List<BusinessProfile> suggestions = businesses.getSuggestions(business);
			
			MavBuilder mav = render("businessprofile")
					.put("business", business)
					.put("suggestions", suggestions)
					.put("subscribed", false)
					.put("owner", false)
					.put(MavBuilder.PAGE_DESCRIPTION, business.getDescription());
			
			//metadata
			if(null != business.getImgur()) {
				mav.thumbnail(business.getImgur().getSmallSquare());
			}
			if(null != business.getDescription() && business.getDescription().length() > 0) {
				mav.description(business.getDescription());
			}
			
			return mav.mav();
			
		} else {
			//user is logged in
			Object[] profileObjects = businesses.getForViewProfile(principal.getName(), domain);
			if(null == profileObjects) {
				log.debug("No business profile found for domain {}. Trying as an Id", domain);
				try {
					long id = Long.parseLong(domain);
					String queriedDomain = businesses.getDomain(id);
					if(null == queriedDomain) {
						throw new IllegalArgumentException("Unknown domain " + domain);
					}
					return redirect("/p/" + queriedDomain).mav();
				} catch (Exception e) {
					throw new IllegalArgumentException("Unknown domain " + domain);
				}
			}
			
			UserExtension userExtension = (UserExtension) profileObjects[0];
			BusinessProfile business = (BusinessProfile) profileObjects[1];
			List<BusinessProfile> suggestions = businesses.getSuggestions(business);
			
			return render(userExtension, "businessprofile")
					.put("business", business)
					.put("suggestions", suggestions)
					.put("subscribed", interact.isSubscribed(principal.getName(), business.getId(), PostType.business))
					.put("owner", business.getOwner().equals(userExtension))
					.mav();
		}
	}

	@Override
	public String failedPermissionsRedirect() {
		return "redirect:/login/";
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public String unknownDomainException() {
		return "redirect:/error/";
	}
}
