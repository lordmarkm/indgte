package com.baldwin.indgte.webapp.controller;

import org.springframework.web.servlet.ModelAndView;

public class MavBuilder {
	ModelAndView mav;
	
	public static MavBuilder render(String view) {
		return new MavBuilder(view);
	}
	
	public MavBuilder(String view) {
		mav = new ModelAndView(view);
	}
	
	public MavBuilder addObject(String name, Object object) {
		mav.addObject(name, object);
		return this;
	}
	
	public ModelAndView mav() {
		return mav;
	}
}
