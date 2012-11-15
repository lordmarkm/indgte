package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.persistence.constants.Initializable.wishlist;
import static com.baldwin.indgte.webapp.controller.MavBuilder.*;

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
import com.baldwin.indgte.persistence.model.UserExtension;
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
	public ModelAndView profile(Principal principal) {
		log.debug("Profile page requested by {}", principal);
		
		User user = users.getFacebook(principal.getName());
		
		return render(user, "ownprofile")
				.put("authorities", SecurityContextHolder.getContext().getAuthentication().getAuthorities())
				.mav();
	}
	
	@Override
	public ModelAndView myBusinesses(Principal principal, WebRequest request) {
		log.debug("Profile page requested by {}", principal);
		
		User fbUser = users.getFacebook(principal.getName());
		Collection<BusinessProfile> businessez = businesses.getBusinesses(principal.getName());
		
		ModelAndView mav = new ModelAndView()
							.addObject("user", fbUser)
							.addObject("businesses", businessez);
		
		mav.setViewName("businesses");
		
		return mav;
	}
	
	@Override
	public ModelAndView userProfile(Principal principal, @PathVariable String targetUsername) {
		User user = users.getFacebook(principal.getName());
		UserExtension target = users.getExtended(targetUsername, wishlist);

		return render(user, "userprofile")
					.put("target", target)
					.put("subscribed", posts.isSubscribed(principal.getName(), target.getUser().getId(), PostType.user))
					.mav();
	}
	
	@Override
	public ModelAndView businessProfile(Principal principal, WebRequest request, @PathVariable String domain) {
		log.debug("Profile requested for {}", domain);

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
				log.error("Failed to find anything.", e);
				throw new IllegalArgumentException("Unknown domain " + domain);
			}
		}
		
		UserExtension userExtension = (UserExtension) profileObjects[0];
		User user = userExtension.getUser();
		BusinessProfile business = (BusinessProfile) profileObjects[1];
		
		return render(user, "businessprofile")
				.put("business", business)
				.put("subscribed", posts.isSubscribed(principal.getName(), business.getId(), PostType.business))
				.put("owner", business.getOwner().equals(user))
				.put("imgurKey", imgurKey)
				.mav();
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
