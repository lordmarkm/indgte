package com.baldwin.indgte.webapp.misc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.model.SidebarFeature;
import com.baldwin.indgte.pers‪istence.dao.BillingDao;
import com.baldwin.indgte.pers‪istence.dao.BusinessDao;
import com.baldwin.indgte.webapp.aop.ConstantsInserterAspect;
import com.baldwin.indgte.webapp.controller.MavBuilder;

/**
 * Inserts constants as a bean.. needs to be manually called somewhere
 * see also {@link ConstantsInserterAspect}
 * @author mbmartinez
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConstantsInserterBean {
	
	@Autowired
	private BillingDao billing;

	@Autowired
	private BusinessDao businesses;
	
	public void insertConstants(MavBuilder builder) {
		insertConstants(builder.mav());
	}
	
	public void insertConstants(ModelAndView mav) {
		
		List<SidebarFeature> promos = billing.getSidebarPromos();
		mav.addObject("sidebarPromos", promos);
		
		List<Summary> newEntities = businesses.getLatestEntities();
		mav.addObject("newEntities", newEntities);
	}
}
