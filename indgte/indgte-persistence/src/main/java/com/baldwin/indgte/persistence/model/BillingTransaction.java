package com.baldwin.indgte.persistence.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="transactions")
public class BillingTransaction {
	public static enum BillingOperation {
		grant, 
		feature_post,
		advertise_business,
		advertise_category,
		advertise_product,
		advertise_buyandsell, 
	}
	
	@Embeddable
	public static class BillingTransactionDetails {
		@ManyToOne
		@JoinColumn(name="adminId")
		private UserExtension grantingAdmin;
		
		@ManyToOne
		@JoinColumn(name="postId")
		private Post featuredPost;
		
		@ManyToOne
		@JoinColumn(name="businessId")
		private BusinessProfile advertisedBusiness;
		
		@ManyToOne
		@JoinColumn(name="categoryId")
		private Category advertisedCategory;

		@ManyToOne
		@JoinColumn(name="productId")
		private Product advertisedProduct;
		
		@ManyToOne
		@JoinColumn(name="buyAndSellId")
		private BuyAndSellItem advertisedItem;
		
		public UserExtension getGrantingAdmin() {
			return grantingAdmin;
		}
		public void setGrantingAdmin(UserExtension grantingAdmin) {
			this.grantingAdmin = grantingAdmin;
		}
		public Post getFeaturedPost() {
			return featuredPost;
		}
		public void setFeaturedPost(Post featuredPost) {
			this.featuredPost = featuredPost;
		}
		public BusinessProfile getAdvertisedBusiness() {
			return advertisedBusiness;
		}
		public void setAdvertisedBusiness(BusinessProfile advertisedBusiness) {
			this.advertisedBusiness = advertisedBusiness;
		}
		public Category getAdvertisedCategory() {
			return advertisedCategory;
		}
		public void setAdvertisedCategory(Category advertisedCategory) {
			this.advertisedCategory = advertisedCategory;
		}
		public Product getAdvertisedProduct() {
			return advertisedProduct;
		}
		public void setAdvertisedProduct(Product advertisedProduct) {
			this.advertisedProduct = advertisedProduct;
		}
		public BuyAndSellItem getAdvertisedItem() {
			return advertisedItem;
		}
		public void setAdvertisedItem(BuyAndSellItem advertisedItem) {
			this.advertisedItem = advertisedItem;
		}
	}
	
	@Id @GeneratedValue
	private long id;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="userId")
	private UserExtension user;
	
	/**
	 * GRANT: admin name
	 * ADVERISE: entity name
	 * FEATURE: post title, entity name (if entity is attached)
	 */
	@Embedded
	private BillingTransactionDetails details;
	
	@Column
	@Enumerated
	private BillingOperation operation;
	
	@Column
	private int amount = 0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserExtension getUser() {
		return user;
	}

	public void setUser(UserExtension user) {
		this.user = user;
	}

	public BillingOperation getOperation() {
		return operation;
	}

	public void setOperation(BillingOperation operation) {
		this.operation = operation;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public BillingTransactionDetails getDetails() {
		if(null == details) {
			details = new BillingTransactionDetails();
		}
		return details;
	}

	public void setDetails(BillingTransactionDetails details) {
		this.details = details;
	}
}
