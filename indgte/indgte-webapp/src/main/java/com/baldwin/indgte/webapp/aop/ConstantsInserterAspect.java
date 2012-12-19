package com.baldwin.indgte.webapp.aop;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.SidebarFeature;
import com.baldwin.indgte.pers‪istence.dao.BillingDao;
import com.baldwin.indgte.webapp.misc.ConstantsInserterBean;

/**
 * Inserts constants as an aspect
 * 
 * See also {@link ConstantsInserterBean}
 * @author mbmartinez
 */

@Aspect
//@Component
public class ConstantsInserterAspect {

	static Logger log = LoggerFactory.getLogger(ConstantsInserterAspect.class);
	
	@Autowired
	private BillingDao billing;

//	@AfterReturning(
//		pointcut = "execution(org.springframework.web.servlet.ModelAndView com.baldwin.indgte.webapp.controller.*.*(..))",
//		returning = "result"
//	)
//	public void insertConstants(JoinPoint joinPoint, Object result) {
//		
//		log.debug("Inserting sidebar promos now...");
//		
//		ModelAndView mav = (ModelAndView) result;
//		List<SidebarFeature> promos = billing.getSidebarPromos();
//		mav.addObject("sidebarPromos", promos);
//		
//	}
	
	@Around("execution(org.springframework.web.servlet.ModelAndView com.baldwin.indgte.webapp.controller.*.*(..))")
	public void insertConstants(ProceedingJoinPoint joinPoint) throws Throwable {
//		ModelAndView mav = (ModelAndView) joinPoint.proceed();
//		List<SidebarFeature> promos = billing.getSidebarPromos();
//		mav.addObject("sidebarPromos", promos);
	}
}