package com.baldwin.indgte.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;

@Indexed
@Entity
@Table(name="buyandsell_fixedprice")
public class FixedPriceItem extends BuyAndSellItem {
	@Column
	private double price;
	
	@Column
	private boolean negotiable;

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isNegotiable() {
		return negotiable;
	}

	public void setNegotiable(boolean negotiable) {
		this.negotiable = negotiable;
	}
}
