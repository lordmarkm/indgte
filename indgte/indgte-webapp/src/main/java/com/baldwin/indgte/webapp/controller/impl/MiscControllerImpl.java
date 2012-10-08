package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.webapp.controller.MiscController;

public class MiscControllerImpl implements MiscController {

	@Override
	public ModelAndView help(WebRequest request) {
		String loadhere = request.getParameter("loadhere");
		
		if(loadhere != null && Boolean.parseBoolean(loadhere)) {//return just the help jsp
			return render("/jsp/help").mav();
		} else {//return the whole page with navbar and footer
			return render("help").mav();
		}
	}

}
