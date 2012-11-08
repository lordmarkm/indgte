package com.baldwin.indgte.persistence.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import com.baldwin.indgte.persistence.dto.Searchable;
import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.Summary.SummaryType;

@Indexed
@Entity
@Table(name="UserConnection")
public class User implements Searchable {
	
	public static final String[] searchableFields = new String[]{"username"};
	
	@Id 
	@GeneratedValue 
	@Column(name="connection_id")
	private long id;
	
	@Field
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

	@ElementCollection
	@CollectionTable(
		name = "businessSubs",
		joinColumns = {@JoinColumn(name = "userId")}
	)
	@OrderColumn(name="order")
	@Column(name="businessId")
	private Set<Long> businessSubscriptions;
	
	@ElementCollection
	@CollectionTable(
		name = "userSubs",
		joinColumns = {@JoinColumn(name = "subscriberId")}
	)
	@OrderColumn(name="order")
	@Column(name="userId")
	private Set<Long> userSubscriptions;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<BuyAndSellItem> buyAndSellItems;
	
	@OneToOne(mappedBy="user")
	private UserExtension extension;
	
	@Override
	public String toString() {
		return username;
	}

	@Override
	public Summary summarize() {
		return new Summary(SummaryType.user, id, username, null, username, imageUrl);
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

	@JsonIgnore
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

	@JsonIgnore
	public Set<Long> getBusinessSubscriptions() {
		if(null == businessSubscriptions) {
			businessSubscriptions = new HashSet<Long>();
		}
		return businessSubscriptions;
	}

	public void setBusinessSubscriptions(Set<Long> businessSubscriptions) {
		this.businessSubscriptions = businessSubscriptions;
	}

	@JsonIgnore
	public Set<Long> getUserSubscriptions() {
		return userSubscriptions;
	}

	public void setUserSubscriptions(Set<Long> userSubscriptions) {
		this.userSubscriptions = userSubscriptions;
	}
	
	@Override
	@JsonIgnore
	public String[] getSearchableFields() {
		return searchableFields;
	}

	@JsonIgnore
	public Set<BuyAndSellItem> getBuyAndSellItems() {
		if(null == buyAndSellItems) {
			this.buyAndSellItems = new HashSet<BuyAndSellItem>();
		}
		return buyAndSellItems;
	}

	public void setBuyAndSellItems(Set<BuyAndSellItem> buyAndSellItems) {
		this.buyAndSellItems = buyAndSellItems;
	}

	@JsonIgnore
	public UserExtension getExtension() {
		return extension;
	}

	public void setExtension(UserExtension extension) {
		this.extension = extension;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}