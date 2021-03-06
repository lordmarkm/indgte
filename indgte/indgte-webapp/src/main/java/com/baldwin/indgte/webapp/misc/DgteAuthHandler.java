package com.baldwin.indgte.webapp.misc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.persistence.service.UserService;

@Component
public class DgteAuthHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    static Logger log = LoggerFactory.getLogger(DgteAuthHandler.class);

	@Autowired
	private LocaleResolver localeResolver;
    
	@Autowired
	private UserService users;
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        
    	String username = (String) authentication.getPrincipal();
    	UserExtension user = users.getExtended(username);
    	log.debug("Got user: [{}]", user);
    	log.debug("Detected localeResolver: {}", localeResolver);
		if(null != localeResolver) {
			log.debug("Setting locale: {}", user.getAppearanceSettings().getLocale());
			localeResolver.setLocale(request, response, Language.determineLocale(user.getAppearanceSettings().getLocale()));
		}
    	
    	try {
    		String originalTargetUrl = getRedirectUrl(request, response);
    		String originalUrl = (String) request.getSession().getAttribute("originalUrl");
    		
    		if(null != originalTargetUrl) {
    			log.debug("Redirecting to original target url {}", originalTargetUrl);
                getRedirectStrategy().sendRedirect(request, response, originalTargetUrl);
                return;
    		} else if(null != originalUrl) {
        		log.debug("Found original url {}", originalUrl);
                getRedirectStrategy().sendRedirect(request, response, originalUrl);
                return;
        	} else {
        		log.debug("No original URL to return to. Will proceed with normal auth process");
        	}
    	} catch (Exception e) {
    		log.debug("Could not obtain original URL. Will proceed with normal auth process");
    	}
    	
    	super.onAuthenticationSuccess(request, response, authentication);
    }
    
    protected String getRedirectUrl(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if(session != null) {
        		SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);            if(savedRequest != null) {
                return savedRequest.getRedirectUrl();
            }
        }

        /* return a sane default in case data isn't there */
        return request.getContextPath() + "/";
    }
}
