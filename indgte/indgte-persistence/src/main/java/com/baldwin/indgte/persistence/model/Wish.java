package com.baldwin.indgte.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.baldwin.indgte.persistence.constants.WishType;

@Entity
@Table(name="wishes")
public class Wish {
	@Id
	@GeneratedValue
	private long id;
	
	@Column(name="wishOrder")
	private int wishOrder;
	
	@Enumerated
	@Column
	private WishType type;

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date time;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="userId", nullable=false, updatable=false)
	private UserExtension wisher;
	
	@ManyToOne(optional=true)
	@JoinColumn(name="productId")
	private Product product;
	
	@ManyToOne(optional=true)
	@JoinColumn(name="buyAndSellId")
	private BuyAndSellItem buyAndSellItem;
	
	/*
	 * Following fields are only for no-entity wishes
	 */
	@OneToOne(optional=true)
	@JoinColumn(name="imgurId")
	private Imgur imgur;
	
	@Column
	private String wishText;
	
	public String getText() {
		switch(type) {
		case buyandsell:
			return buyAndSellItem.getName(); //BuyAndSellItem must always have an imgur
		case product:
			return product.getName();
		case noentity:
			return wishText;
		}
		return null;		
	}
	
	public Imgur getActiveImgur() {
		switch(type) {
		case buyandsell:
			return buyAndSellItem.getImgur(); //BuyAndSellItem must always have an imgur
		case product:
			return product.getMainpic();
		case noentity:
			return imgur;
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((buyAndSellItem == null) ? 0 : buyAndSellItem.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Wish other = (Wish) obj;
		if (buyAndSellItem == null) {
			if (other.buyAndSellItem != null)
				return false;
		} else if (!buyAndSellItem.equals(other.buyAndSellItem))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public WishType getType() {
		return type;
	}

	public void setType(WishType type) {
		this.type = type;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public BuyAndSellItem getBuyAndSellItem() {
		return buyAndSellItem;
	}

	public void setBuyAndSellItem(BuyAndSellItem buyAndSellItem) {
		this.buyAndSellItem = buyAndSellItem;
	}

	public Imgur getImgur() {
		return imgur;
	}

	public void setImgur(Imgur imgur) {
		this.imgur = imgur;
	}

	public String getWishText() {
		return wishText;
	}

	public void setWishText(String wishText) {
		this.wishText = wishText;
	}

	public void setWishOrder(int wishOrder) {
		this.wishOrder = wishOrder;
	}

	public int getWishOrder() {
		return wishOrder;
	}

	public UserExtension getWisher() {
		return wisher;
	}

	public void setWisher(UserExtension wisher) {
		this.wisher = wisher;
	}
}