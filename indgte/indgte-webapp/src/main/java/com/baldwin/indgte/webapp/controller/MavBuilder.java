package com.baldwin.indgte.webapp.controller;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.baldwin.indgte.persistence.constants.Background;
import com.baldwin.indgte.persistence.constants.Theme;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.webapp.misc.DgteConstants;
import com.baldwin.indgte.webapp.misc.DgteTagWhitelist;

public class MavBuilder {
	
	public static String PAGE_DESCRIPTION = "page_description";
	public static String PAGE_THUMBNAIL = "page_thumbnail";
	
	ModelAndView mav;
	
	public MavBuilder viewName(String viewName) {
		mav.setViewName(viewName);
		return this;
	}
	
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
	@Deprecated
	public static MavBuilder render(User user, String viewname) {
		return new MavBuilder(viewname).put("user", user);
	}
	
	public static MavBuilder render(UserExtension user, String viewname) {
		return new MavBuilder(viewname).put("user", user);
	}
	
	public static MavBuilder redirect(String view) {
		return new MavBuilder(new RedirectView(view, true));
	}
	
	private MavBuilder() {
		mav = new ModelAndView();
		loadGlobals(this);
	}
	
	private MavBuilder(String viewname) {
		this();
		mav.setViewName(viewname);
	}
	
	private MavBuilder(RedirectView redirectView) {
		mav = new ModelAndView(redirectView);
	}
	
	private void loadGlobals(MavBuilder m) {
		m.put("themes", Theme.values());
		m.put("backgrounds", Background.values());
		m.put("imgurKey", DgteConstants.IMGUR_DEVKEY);
		m.put(PAGE_THUMBNAIL, DgteConstants.SITE_THUMBNAIL);
		m.put(PAGE_DESCRIPTION, DgteConstants.SITE_DESCRIPTION);
		m.put("facebookClientId", DgteConstants.FACEBOOK_CLIENT_ID);
	}
	
	public MavBuilder put(String name, Object object) {
		mav.addObject(name, object);
		return this;
	}
	
	public MavBuilder description(String description) {
		mav.addObject(PAGE_DESCRIPTION, description);
		return this;
	}
	
	public MavBuilder thumbnail(String thumbnail) {
		mav.addObject(PAGE_THUMBNAIL, thumbnail);
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
	 * For descriptions
	 */
	public static String basicWithImages(String dirty) {
		return Jsoup.clean(dirty.replaceAll("(\r\n|\n\r|\r|\n)", "<br>"), DgteTagWhitelist.basicWithImages());
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