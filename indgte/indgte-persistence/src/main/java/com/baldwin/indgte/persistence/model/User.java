package com.baldwin.indgte.persistence.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="UserConnection")
public class User {
	@Id 
	@GeneratedValue 
	@Column(name="connection_id")
	private long id;
	
	@Column(
		nullable=false,
		unique=false,
		name="userId"
	)
	private String username;
	
	@Column(
		nullable=false,
		unique=false
	)
	private String providerId;
	
	@Column(nullable=false)
	private String providerUserId;
	
	@Column
	private Integer rank;
	
	@Column
	private String displayName;
	
	@Column
	private String profileUrl;
	
	@Column
	private String imageUrl;
	
	@Column
	private String accessToken;
	
	@Column
	private String secret;
	
	@Column
	private String refreshToken;
	
	@Column
	private Long expireTime;
	
	@OneToMany(
		fetch=FetchType.LAZY,
		mappedBy="owner"
	)
	private Set<BusinessProfile> businesses;

	@Override
	public String toString() {
		return username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	public Set<BusinessProfile> getBusinesses() {
		if(null == businesses) {
			businesses = new HashSet<BusinessProfile>();
		}
		return businesses;
	}

	public void setBusinesses(Set<BusinessProfile> businesses) {
		this.businesses = businesses;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}