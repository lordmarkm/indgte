package com.baldwin.indgte.webapp.misc;

import javax.annotation.PostConstruct;

import org.socialsignin.springsocial.security.signin.SpringSocialSecurityAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.twitter.api.Twitter;

/**
 * Request-scoped beans for @Inject go here until I figure out how to put them in the XML
 * @author mbmartinez
 */
@Configuration
public class SpringSocialSecurityConfig {

	@Autowired
	JdbcUsersConnectionRepository repository;
	
	@Autowired
	SpringSocialSecurityAuthenticationFilter filter;
	
	@PostConstruct
	public void init() {
		filter.setAuthenticationSuccessHandler(new DgteAuthHandler());
	}
	
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)	
	public Facebook facebook() {
	    return connectionRepository().getPrimaryConnection(Facebook.class).getApi();
	}
	
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public Twitter twitter() {
		return connectionRepository().getPrimaryConnection(Twitter.class).getApi();
	}
	
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public ConnectionRepository connectionRepository() {
	    String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    return repository.createConnectionRepository(userId);
	}
}
