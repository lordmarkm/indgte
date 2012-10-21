package com.baldwin.indgte.webapp.controller;

import org.apache.commons.lang.StringEscapeUtils;
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
	
	/**
	 * @param user - must always be 3rd party provider (like Facebook)!
	 */
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
	 * Ajax as determined by 
	 * 	1. presence of ?loadhere=true (iframe-like behavior)
	 *  2. presence of ?dialog=true (view to be displayed as dialog)
	 * @param request
	 * @return boolean
	 */
	public static boolean isAjax(WebRequest request) {
		String loadhere = request.getParameter("loadhere");
		String dialog = request.getParameter("dialog");
		if(loadhere != null && Boolean.parseBoolean(loadhere)) return true;
		if(dialog != null && Boolean.parseBoolean(dialog)) return true;
		return false;
	}
	
	/**
	 * Escape potentially exploitative user inputs while retaining newlines
	 * (and potentially other useful harmless markup in the future)
	 */
	public static String clean(String dirty) {
		return clean(dirty, true);
	}
	
	public static String clean(String dirty, boolean allowBr) {
		if(null == dirty) {
			return null;
		} else if(allowBr) {
			return StringEscapeUtils.escapeHtml(dirty).replaceAll("(\r\n|\n\r|\r|\n)", "<br>");
		} else {
			return StringEscapeUtils.escapeHtml(dirty);
		}
	}
}