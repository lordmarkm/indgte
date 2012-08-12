package com.baldwin.indgte.persistence.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="INDGTE_PROFILE_USERS")
public class User {
	@Id @GeneratedValue @Column(name="USER_ID")
	private long id;
	
	@Column(name="FACEBOOK_ID", nullable=false, unique=true)
	private long facebookId;
	
	@Column
	private String name;

	@ManyToMany(mappedBy="admins")
	private Set<BusinessProfile> businesses;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(long facebookId) {
		this.facebookId = facebookId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<BusinessProfile> getBusinesses() {
		return businesses;
	}

	public void setBusinesses(Set<BusinessProfile> businesses) {
		this.businesses = businesses;
	}
}
