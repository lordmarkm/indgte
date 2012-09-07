package com.baldwin.indgte.webapp.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class MavBuilder {
	ModelAndView mav;
	
	public static MavBuilder render(String view) {
		return new MavBuilder(view);
	}
	
	public static MavBuilder redirect(String view) {
		return new MavBuilder(new RedirectView(view));
	}
	
	public MavBuilder(String view) {
		mav = new ModelAndView(view);
	}
	
	public MavBuilder(RedirectView redirectView) {
		mav = new ModelAndView(redirectView);
	}
	
	public MavBuilder addObject(String name, Object object) {
		mav.addObject(name, object);
		return this;
	}
	
	public ModelAndView mav() {
		return mav;
	}
}
