package com.baldwin.indgte.persistence.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.IndexColumn;

import com.baldwin.indgte.persistence.constants.Theme;
import com.baldwin.indgte.persistence.dto.Summarizable;
import com.baldwin.indgte.persistence.dto.Summary;

/**
 * A class to hold auxiliary user info
 * @author mbmartinez
 */

@Entity
@Table(name="userextensions")
public class UserExtension implements Summarizable {
	@Id
	private long id;
	
	@OneToOne(cascade=CascadeType.ALL, optional=false)
	@MapsId
	@JoinColumn(name="userId", nullable=false, unique=true)
	private User user;
	
	@Column
	private String rank = "Dumagueteño";
	
	@Enumerated
	@Column
	private Theme theme = Theme.flick;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "reviewer")
	private Set<BusinessReview> businessReviews;
	
	@ManyToMany
	@JoinTable(
		name="forreview",
		joinColumns={@JoinColumn(name="userId")},
		inverseJoinColumns={@JoinColumn(name="businessId")}
	)
	@OrderColumn(name="reviewOrder")
	private List<BusinessProfile> forReview;
	
	@ElementCollection
	@CollectionTable(
		name="neverreview",
		joinColumns={@JoinColumn(name="userId")}
	)
	private Set<Long> neverReview;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="reviewee")
	private Set<UserReview> reviewsReceived;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="reviewer")
	private Set<UserReview> reviewsWritten;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="wisher")
	@IndexColumn(name="wishOrder")
	private List<Wish> wishlist;
	
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "voters")
	private Set<TopTenCandidate> votes;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "creator", orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<TopTenList> createdToptens;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<BuyAndSellItem> buyAndSellItems;
	
	@ManyToMany
	@JoinTable(
		name="watchedTags",
		joinColumns = @JoinColumn(name="userId"),
		inverseJoinColumns = @JoinColumn(name="tagId")
	)
	private Set<Tag> watchedTags;
	
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
	
	public boolean inWishlist(Product product) {
		for(Wish wish : wishlist) {
			if(product.equals(wish.getProduct())) return true;
		}
		return false;
	}
	
	public boolean inWishlist(BuyAndSellItem item) {
		for(Wish wish : wishlist) {
			if(item.equals(wish.getBuyAndSellItem())) return true;
		}
		return false;
	}
	
	public UserExtension(){
		//
	}
	
	public UserExtension(User user) {
		this.user = user;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@JsonIgnore
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JsonIgnore
	public List<Wish> getWishlist() {
		if(null == wishlist) {
			wishlist = new ArrayList<Wish>();
		}
		return wishlist;
	}

	public void setWishlist(List<Wish> wishlist) {
		this.wishlist = wishlist;
	}

	@JsonIgnore
	public List<BusinessProfile> getForReview() {
		return forReview;
	}

	public void setForReview(List<BusinessProfile> forReview) {
		this.forReview = forReview;
	}

	public void setNeverReview(Set<Long> neverReview) {
		this.neverReview = neverReview;
	}
	
	@JsonIgnore
	public Set<Long> getNeverReview() {
		if(null == neverReview) {
			neverReview = new HashSet<Long>();
		}
		return neverReview;
	}

	@JsonIgnore
	public Set<UserReview> getReviewsReceived() {
		if(null == reviewsReceived) {
			reviewsReceived = new HashSet<UserReview>();
		}
		return reviewsReceived;
	}

	public void setReviewsReceived(Set<UserReview> reviewsReceived) {
		this.reviewsReceived = reviewsReceived;
	}

	@JsonIgnore
	public Set<UserReview> getReviewsWritten() {
		if(null == reviewsWritten) {
			reviewsWritten = new HashSet<UserReview>();
		}
		return reviewsWritten;
	}

	public void setReviewsWritten(Set<UserReview> reviewsWritten) {
		this.reviewsWritten = reviewsWritten;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getUsername() {
		return user.getUsername();
	}

	public String getImageUrl() {
		return user.getImageUrl();
	}
	
	public String getProfileUrl() {
		return user.getProfileUrl();
	}
	
	@JsonIgnore
	public Set<BusinessReview> getBusinessReviews() {
		if(null == businessReviews) {
			businessReviews = new HashSet<BusinessReview>();
		}
		return businessReviews;
	}

	public void setBusinessReviews(Set<BusinessReview> businessReviews) {
		this.businessReviews = businessReviews;
	}
	
	@JsonIgnore
	public Set<TopTenCandidate> getVotes() {
		if(null == votes) {
			votes = new HashSet<TopTenCandidate>();
		}
		return votes;
	}

	public void setVotes(Set<TopTenCandidate> votes) {
		this.votes = votes;
	}

	@JsonIgnore
	public Set<TopTenList> getCreatedToptens() {
		if(null == createdToptens) {
			createdToptens = new HashSet<TopTenList>();
		}
		return createdToptens;
	}

	public void setCreatedToptens(Set<TopTenList> createdToptens) {
		this.createdToptens = createdToptens;
	}

	@JsonIgnore
	public Set<Tag> getWatchedTags() {
		if(null == watchedTags) {
			watchedTags = new HashSet<Tag>();
		}
		return watchedTags;
	}

	public void setWatchedTags(Set<Tag> watchedTags) {
		this.watchedTags = watchedTags;
	}

	@Override
	public Summary summarize() {
		return user.summarize();
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
	public Set<BusinessProfile> getBusinesses() {
		if(null == businesses) {
			businesses = new HashSet<BusinessProfile>();
		}
		return businesses;
	}

	public void setBusinesses(Set<BusinessProfile> businesses) {
		this.businesses = businesses;
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
}