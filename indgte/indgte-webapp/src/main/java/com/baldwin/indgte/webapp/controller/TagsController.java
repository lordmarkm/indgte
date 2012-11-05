package com.baldwin.indgte.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/tags/")
public interface TagsController {
	
	@RequestMapping(value="/{term}.json", method = RequestMethod.GET)
	public JSON autocomplete(String term);

}
