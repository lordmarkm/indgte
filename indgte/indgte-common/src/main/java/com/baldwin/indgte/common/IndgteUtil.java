package com.baldwin.indgte.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class IndgteUtil implements ApplicationContextAware {
	private ApplicationContext ctx;
	
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		this.ctx = appContext;
	}

	public Object getBean(String name) {
		return ctx.getBean(name);
	}
}