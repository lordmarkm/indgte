package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.persistence.constants.Initializable.*;
import static com.baldwin.indgte.webapp.controller.MavBuilder.redirect;
import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.constants.Initializable;
import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.persistence.service.BillingService;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.persistence.service.InteractiveService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.controller.ProfileController;
import com.baldwin.indgte.webapp.misc.Language;

@Component
public class ProfileControllerImpl implements ProfileController {
	static Logger log = LoggerFactory.getLogger(ProfileControllerImpl.class);
	
	@Autowired
	private BusinessService businesses;
	
	@Autowired
	private UserService users;
	
	@Autowired
	private InteractiveService posts;
	
	@Autowired
	private BillingService billing;
	
	@Autowired
	private LocaleResolver localeResolver;
	
	@Override
	public String grantCoconuts(Principal principal, @PathVariable long userId, @RequestParam int howmany) {
		UserExtension user = billing.grantCoconuts(principal.getName(), userId, howmany);
		return "redirect:/p/user/" + user.getUsername();
	}
	
	@Override
	public ModelAndView profile(Principal principal) {
		log.info("{} has requested Profile page", principal.getName());
		
		UserExtension user = users.getExtended(principal.getName());
		
		return render(user, "ownprofile")
				.put("authorities", SecurityContextHolder.getContext().getAuthentication().getAuthorities())
				.mav();
	}
	
	@Override
	public ModelAndView myBusinesses(Principal principal, WebRequest request) {
		log.info("{} has requested business management page", principal.getName());
		
		UserExtension user = users.getExtended(principal.getName(), Initializable.businesses);
		
		return render(user, "businesses")
				.put("businesses", user.getBusinesses())
				.mav();
	}
	
	@Override
	public ModelAndView userProfile(Principal principal, @PathVariable String targetUsername) {
		UserExtension user = users.getExtended(principal.getName());
		UserExtension target = users.getExtended(targetUsername, subscriptions, wishlist, buyandsellitems, Initializable.businesses);

		List<BusinessProfile> businessSubscriptions = new ArrayList<BusinessProfile>();
		for(Long id : target.getBusinessSubscriptions()) {
			businessSubscriptions.add(businesses.get(id));
		}
		
		List<UserExtension> userSubscriptions = new ArrayList<UserExtension>();
		for(Long id : target.getUserSubscriptions()) {
			userSubscriptions.add(users.getExtended(id));
		}
		
		return render(user, "userprofile")
					.put("target", target)
					.put("businessSubscriptions", businessSubscriptions)
					.put("userSubscriptions", userSubscriptions)
					.put("subscribed", posts.isSubscribed(principal.getName(), target.getId(), PostType.user))
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

	@Override
	public ModelAndView manageAccount(Principal principal) {
		log.info("{} has requested account management screen", principal.getName());
		
		UserExtension user = users.getExtended(principal.getName(), Initializable.subscriptions);
		
		List<BusinessProfile> businessSubscriptions = new ArrayList<BusinessProfile>();
		for(Long id : user.getBusinessSubscriptions()) {
			businessSubscriptions.add(businesses.get(id));
		}
		
		List<UserExtension> userSubscriptions = new ArrayList<UserExtension>();
		for(Long id : user.getUserSubscriptions()) {
			userSubscriptions.add(users.getExtended(id));
		}
		
		return render(user, "manage")
					.put("languages", Language.values())
					.put("businessSubscriptions", businessSubscriptions)
					.put("userSubscriptions", userSubscriptions)
					.mav();
	}

	@Override
	public ModelAndView changeLocale(Principal principal, 
			HttpServletRequest request, HttpServletResponse response, 
			@PathVariable String localeStr) {
		
		log.info("{} has changed locale to {}", principal.getName(), localeStr);
		
		localeResolver.setLocale(request, response, Language.determineLocale(localeStr));
		users.changeLocale(principal.getName(), localeStr);
		return redirect("/p/manage/").mav();
		
	}

	@ExceptionHandler(Exception.class) 
	public String error() {
		return "redirect:/error/";
	}
}
