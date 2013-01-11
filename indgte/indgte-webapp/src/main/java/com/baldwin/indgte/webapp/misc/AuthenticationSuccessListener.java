package com.baldwin.indgte.webapp.misc;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {
	static Logger log = LoggerFactory.getLogger(AuthenticationSuccessListener.class);

	@Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
    	Object principal = event.getAuthentication().getPrincipal();
    	
		log.info("Auth success! {}", principal);
		
		//Make Mark Martinez an user, mod, admin
		if(principal instanceof String && ((String)principal).equals("mark.martinez.986")) {
			Authentication authentication = event.getAuthentication();
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(authentication.getAuthorities());
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			authorities.add(new SimpleGrantedAuthority("ROLE_MODERATOR"));
			
			Authentication adminAuthentication = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authorities);
			SecurityContextHolder.getContext().setAuthentication(adminAuthentication);
		} 
		
		//Gemgem is user, mod
		else if(principal instanceof String && ((String)principal).equals("van.martinez")) {
			Authentication authentication = event.getAuthentication();
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(authentication.getAuthorities());
			authorities.add(new SimpleGrantedAuthority("ROLE_MODERATOR"));
			
			Authentication adminAuthentication = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authorities);
			SecurityContextHolder.getContext().setAuthentication(adminAuthentication);
		}
    }
}
