package com.baldwin.indgte.webapp.dto;

import com.baldwin.indgte.persistence.model.BusinessProfile;

public class RegistrationForm {
	private BusinessProfile businessProfile;
	
	public RegistrationForm() {
		
	}
	
	public RegistrationForm(BusinessProfile businessProfile) {
		this.businessProfile = businessProfile;
	}
}
