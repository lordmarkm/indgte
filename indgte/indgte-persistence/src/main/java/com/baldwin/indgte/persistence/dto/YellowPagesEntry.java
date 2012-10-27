package com.baldwin.indgte.persistence.dto;

import com.baldwin.indgte.persistence.model.BusinessProfile;

public class YellowPagesEntry {

	private String domain;
	private String name;
	private String address;
	private String email;
	private String landline;
	private String cellphone;
	
	public YellowPagesEntry(BusinessProfile business) {
		this.domain = business.getDomain();
		this.name = business.getFullName();
		this.address = business.getAddress();
		this.email = business.getEmail();
		this.landline = business.getLandline();
		this.cellphone = business.getCellphone();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLandline() {
		return landline;
	}
	public void setLandline(String landline) {
		this.landline = landline;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
}
