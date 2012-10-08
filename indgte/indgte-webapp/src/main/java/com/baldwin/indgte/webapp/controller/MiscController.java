package com.baldwin.indgte.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handle stuff that doesn't fit anywhere else
 * 
 * @author mbmartinez
 */

@Controller
@RequestMapping("/etc/")
public interface MiscController {

	@RequestMapping("/help/")
	public ModelAndView help(WebRequest request);

}
