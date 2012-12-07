package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baldwin.indgte.pers‪istence.dao.FameDao;

@Controller
@RequestMapping("/test/")
public class TestController {

	static Logger log = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	private FameDao fdao;
	
	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody JSON test(Principal principal) {
		log.debug("Computing fame for {}", principal);
		return JSON.ok().put("result", fdao.computeFame(principal.getName()));
	}
}
