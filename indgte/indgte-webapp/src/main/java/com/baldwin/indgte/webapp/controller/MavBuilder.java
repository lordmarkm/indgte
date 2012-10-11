package com.baldwin.indgte.webapp.controller;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.baldwin.indgte.persistence.model.User;

public class MavBuilder {
	ModelAndView mav;
	
	public static MavBuilder render(User user) {
		return new MavBuilder()
			.put("user", user);
	}
	
	public static MavBuilder render(String viewname) {
		return new MavBuilder(viewname);
	}
	
	public static MavBuilder render(User user, String viewname) {
		return new MavBuilder(viewname).put("user", user);
	}
	
	public static MavBuilder redirect(String view) {
		return new MavBuilder(new RedirectView(view, true));
	}
	
	public MavBuilder() {
		mav = new ModelAndView();
	}
	
	public MavBuilder(String viewname) {
		mav = new ModelAndView(viewname);
	}
	
	public MavBuilder(RedirectView redirectViewname) {
		mav = new ModelAndView(redirectViewname);
	}
	
	public MavBuilder put(String name, Object object) {
		mav.addObject(name, object);
		return this;
	}
	
	public ModelAndView mav() {
		return mav;
	}
	
	/**
	 * Ajax as determined by ?loadhere=true
	 * @param request
	 * @return boolean
	 */
	public static boolean isAjax(WebRequest request) {
		String loadhere = request.getParameter("loadhere");
		return (loadhere != null && Boolean.parseBoolean(loadhere));
	}
}
