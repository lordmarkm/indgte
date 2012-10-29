package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.isAjax;
import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.persistence.service.InteractiveService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.controller.ProfileController;

@Component
public class ProfileControllerImpl implements ProfileController {
	static Logger log = LoggerFactory.getLogger(ProfileControllerImpl.class);
	
	@Autowired
	private BusinessService businesses;
	
	@Autowired
	private UserService users;
	
	@Autowired
	private InteractiveService posts;
	
	@Value("${imgur.devkey}")
	private String imgurKey;
	
	@Override
	public ModelAndView profile(Principal principal, WebRequest request) {
		log.debug("Profile page requested by {}", principal);
		
		User user = users.getFacebook(principal.getName());
		String loadhere = request.getParameter("loadhere");
		
		ModelAndView mav = new ModelAndView()
							.addObject("user", user)
							.addObject("authorities", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		
		if(loadhere != null && Boolean.parseBoolean(loadhere)) {
			mav.setViewName("profile/ownprofile");
		} else {
			mav.setViewName("ownprofile");
		}
		
		return mav;
	}
	
	@Override
	public ModelAndView myBusinesses(Principal principal, WebRequest request) {
		log.debug("Profile page requested by {}", principal);
		
		User fbUser = users.getFacebook(principal.getName());
		Collection<BusinessProfile> businessez = businesses.getBusinesses(principal.getName());
		
		String loadhere = request.getParameter("loadhere");
		
		ModelAndView mav = new ModelAndView()
							.addObject("user", fbUser)
							.addObject("businesses", businessez);
		
		if(loadhere != null && Boolean.parseBoolean(loadhere)) {
			mav.setViewName("profile/businesses");
		} else {
			mav.setViewName("businesses");
		}
		
		return mav;
	}
	
	@Override
	public ModelAndView userProfile(Principal principal, @PathVariable String userId) {
		User user = users.getFacebook(principal.getName());
		User targetFacebook = users.getFacebook(userId);
		User targetMain = users.getMain(userId);
		
		return render(user, "userprofile")
					.put("subscribed", posts.isSubscribed(principal.getName(), targetFacebook.getId(), PostType.user))
					.put("targetFacebook", targetFacebook)
					.put("targetMain", targetMain)
					.mav();
	}
	
	@Override
	public ModelAndView businessProfile(Principal principal, WebRequest request, @PathVariable String domain) {
		log.debug("Profile requested for {}", domain);

		User user = users.getFacebook(principal.getName());
		BusinessProfile business = businesses.get(domain);
		
		ModelAndView mav = render(user)
				.put("business", business)
				.put("subscribed", posts.isSubscribed(principal.getName(), business.getId(), PostType.business))
				.put("owner", business.getOwner().getUsername().equals(user.getUsername()))
				.put("imgurKey", imgurKey)
				.mav();
		
		if(isAjax(request)) {
			mav.setViewName("profile/businessprofile");
		} else {
			mav.setViewName("businessprofile");
		}
		
		return mav;
	}

	@Override
	public @ResponseBody Imgur profilepic(@PathVariable String domain) {
		return businesses.getProfilepic(domain);
	}

	@Override
	public @ResponseBody JSON newProfilepic(@PathVariable String domain, Imgur profilepic) {
		businesses.saveProfilepic(domain, profilepic);
		return JSON.ok();
	}
	
	@Override
	public @ResponseBody JSON newCoverpic(@PathVariable String domain, Imgur coverpic) {
		businesses.saveCoverpic(domain, coverpic);
		return JSON.ok();
	}
}
