package com.baldwin.indgte.persistence.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
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

import org.codehaus.jackson.annotate.JsonIgnoreType;
import org.hibernate.annotations.IndexColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baldwin.indgte.persistence.dto.Summarizable;
import com.baldwin.indgte.persistence.dto.Summary;

/**
 * A class to hold auxiliary user info
 * @author mbmartinez
 */

@Entity
@Table(name="userextensions")
@JsonIgnoreType
public class UserExtension implements Summarizable {
	static Logger log = LoggerFactory.getLogger(UserExtension.class);
	
	@Id
	private long id;
	
	@OneToOne(cascade=CascadeType.ALL, optional=false)
	@MapsId
	@JoinColumn(name="userId", nullable=false, unique=true)
	private User user;
	
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
	
	@Embedded
	private BillingInformation billingInfo;
	
	@Embedded
	private AppearanceSettings appearanceSettings;
	
	@Embedded
	private Rank rank;
	
	public boolean inWishlist(Product product) {
		for(Wish wish : wishlist) {
			if(product.equals(wish.getProduct())) return true;
		}
		return false;
	}
	
	public boolean inWishlist(BuyAndSellItem item) {
		if(null == item) return false;
		for(Wish wish : wishlist) {	
			if(null == wish) continue;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Wish> getWishlist() {
		if(null == wishlist) {
			wishlist = new ArrayList<Wish>();
		}
		return wishlist;
	}

	public void setWishlist(List<Wish> wishlist) {
		this.wishlist = wishlist;
	}

	public List<BusinessProfile> getForReview() {
		return forReview;
	}

	public void setForReview(List<BusinessProfile> forReview) {
		this.forReview = forReview;
	}

	public void setNeverReview(Set<Long> neverReview) {
		this.neverReview = neverReview;
	}
	
	public Set<Long> getNeverReview() {
		if(null == neverReview) {
			neverReview = new HashSet<Long>();
		}
		return neverReview;
	}

	public Set<UserReview> getReviewsReceived() {
		if(null == reviewsReceived) {
			reviewsReceived = new HashSet<UserReview>();
		}
		return reviewsReceived;
	}

	public void setReviewsReceived(Set<UserReview> reviewsReceived) {
		this.reviewsReceived = reviewsReceived;
	}

	public Set<UserReview> getReviewsWritten() {
		if(null == reviewsWritten) {
			reviewsWritten = new HashSet<UserReview>();
		}
		return reviewsWritten;
	}

	public void setReviewsWritten(Set<UserReview> reviewsWritten) {
		this.reviewsWritten = reviewsWritten;
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
	
	public Set<BusinessReview> getBusinessReviews() {
		if(null == businessReviews) {
			businessReviews = new HashSet<BusinessReview>();
		}
		return businessReviews;
	}

	public void setBusinessReviews(Set<BusinessReview> businessReviews) {
		this.businessReviews = businessReviews;
	}
	
	public Set<TopTenCandidate> getVotes() {
		if(null == votes) {
			votes = new HashSet<TopTenCandidate>();
		}
		return votes;
	}

	public void setVotes(Set<TopTenCandidate> votes) {
		this.votes = votes;
	}

	public Set<TopTenList> getCreatedToptens() {
		if(null == createdToptens) {
			createdToptens = new HashSet<TopTenList>();
		}
		return createdToptens;
	}

	public void setCreatedToptens(Set<TopTenList> createdToptens) {
		this.createdToptens = createdToptens;
	}

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
	
	public Set<BuyAndSellItem> getBuyAndSellItems() {
		if(null == buyAndSellItems) {
			this.buyAndSellItems = new HashSet<BuyAndSellItem>();
		}
		return buyAndSellItems;
	}

	public void setBuyAndSellItems(Set<BuyAndSellItem> buyAndSellItems) {
		this.buyAndSellItems = buyAndSellItems;
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
	
	public Set<Long> getBusinessSubscriptions() {
		if(null == businessSubscriptions) {
			businessSubscriptions = new HashSet<Long>();
		}
		return businessSubscriptions;
	}

	public void setBusinessSubscriptions(Set<Long> businessSubscriptions) {
		this.businessSubscriptions = businessSubscriptions;
	}
	
	public Set<Long> getUserSubscriptions() {
		if(null == userSubscriptions) {
			this.userSubscriptions = new HashSet<>();
		}
		return userSubscriptions;
	}

	public void setUserSubscriptions(Set<Long> userSubscriptions) {
		this.userSubscriptions = userSubscriptions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		UserExtension other = (UserExtension) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public BillingInformation getBillingInfo() {
		if(null == billingInfo) {
			billingInfo = new BillingInformation();
		}
		return billingInfo;
	}

	public void setBillingInfo(BillingInformation billingInfo) {
		this.billingInfo = billingInfo;
	}

	public AppearanceSettings getAppearanceSettings() {
		if(null == appearanceSettings) {
			appearanceSettings = new AppearanceSettings();
		}
		return appearanceSettings;
	}

	public void setAppearanceSettings(AppearanceSettings appearanceSettings) {
		this.appearanceSettings = appearanceSettings;
	}

	public Rank getRank() {
		if(null == rank) {
			rank = new Rank();
		}
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}
}