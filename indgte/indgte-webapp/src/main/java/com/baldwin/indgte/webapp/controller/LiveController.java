package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import com.baldwin.indgte.webapp.dto.Preview.PreviewType;

@Controller
@RequestMapping("/live/")
public interface LiveController {

	@RequestMapping(method=RequestMethod.GET)
	DeferredResult<JSON> live(Principal principal, 
			boolean initial, 
			String[] chatters, 
			String[] channels, 
			long lastReceivedId,
			long lastNotifId//,
			//Long[] rejectedNotifs
			);

	@RequestMapping(value="/preview/json", method=RequestMethod.GET)
	JSON preview(Principal principal, PreviewType type, String href);
}
