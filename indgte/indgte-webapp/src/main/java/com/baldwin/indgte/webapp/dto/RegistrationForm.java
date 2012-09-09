package com.baldwin.indgte.webapp.dto;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.User;

public class RegistrationForm {
	private BusinessProfile businessProfile;
	
	public RegistrationForm() {
		this.businessProfile = new BusinessProfile();
	}
	
	public RegistrationForm(BusinessProfile businessProfile) {
		this.businessProfile = businessProfile;
	}

	public BusinessProfile getBusinessProfile() {
		return businessProfile;
	}

	public void setBusinessProfile(BusinessProfile businessProfile) {
		this.businessProfile = businessProfile;
	}

	public String toString() {
		return businessProfile.toString();
	}

	public long getId() {
		return businessProfile.getId();
	}

	public void setId(long id) {
		businessProfile.setId(id);
	}

	public String getDomain() {
		return businessProfile.getDomain();
	}

	public void setDomain(String domain) {
		businessProfile.setDomain(domain);
	}

	public String getFullName() {
		return businessProfile.getFullName();
	}

	public void setFullName(String fullName) {
		businessProfile.setFullName(fullName);
	}

	public String getAddress() {
		return businessProfile.getAddress();
	}

	public void setAddress(String address) {
		businessProfile.setAddress(address);
	}

	public String getEmail() {
		return businessProfile.getEmail();
	}

	public void setEmail(String email) {
		businessProfile.setEmail(email);
	}

	public String getLandline() {
		return businessProfile.getLandline();
	}

	public void setLandline(String landline) {
		businessProfile.setLandline(landline);
	}

	public String getCellphone() {
		return businessProfile.getCellphone();
	}

	public void setCellphone(String cellphone) {
		businessProfile.setCellphone(cellphone);
	}

	public String getDescription() {
		return businessProfile.getDescription();
	}

	public void setDescription(String description) {
		businessProfile.setDescription(description);
	}

	public User getOwner() {
		return businessProfile.getOwner();
	}

	public void setOwner(User owner) {
		businessProfile.setOwner(owner);
	}

	public Double getLatitude() {
		return businessProfile.getLatitude();
	}

	public void setLatitude(Double latitude) {
		businessProfile.setLatitude(latitude);
	}

	public Double getLongitude() {
		return businessProfile.getLongitude();
	}

	public void setLongitude(Double longitude) {
		businessProfile.setLongitude(longitude);
	}

	
}
