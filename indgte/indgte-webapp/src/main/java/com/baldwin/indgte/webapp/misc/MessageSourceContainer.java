package com.baldwin.indgte.webapp.misc;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceContainer implements MessageSourceAware {

	private MessageSource messages;
	
	public String getMessage(String code, Locale locale, Object...args) {
		return messages.getMessage(code, args, locale);
	}
	
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messages = messageSource;
	}

}
