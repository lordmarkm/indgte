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

/**
 * A class to hold auxiliary user info
 * @author mbmartinez
 */

@Entity
@Table(name="userextensions")
public class UserExtension {
	@Id
	private long id;
	
	@OneToOne(cascade=CascadeType.ALL, optional=false)
	@MapsId
	@JoinColumn(name="userId", nullable=false, unique=true)
	private User user;
	
	@Column
	private String rank = "Dumagueteño";
	
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
}