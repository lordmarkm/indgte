package com.baldwin.indgte.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="buyandsell_bidding")
public class AuctionItem extends BuyAndSellItem {
	/**
	 * Bidding mode buyout
	 */
	@Column
	private Double start;
	
	@Column
	private Double buyout;
	
	@Column(name="increment")
	private Double biddingMinimumIncrement;
	
	@Column(name="ends")
	@Temporal(TemporalType.DATE)
	private Date biddingEnds;

	public Double getBuyOut() {
		return buyout;
	}

	public void setBuyout(Double buyout) {
		this.buyout = buyout;
	}

	public Double getBiddingMinimumIncrement() {
		return biddingMinimumIncrement;
	}

	public void setBiddingMinimumIncrement(Double biddingMinimumIncrement) {
		this.biddingMinimumIncrement = biddingMinimumIncrement;
	}

	public Date getBiddingEnds() {
		return biddingEnds;
	}

	public void setBiddingEnds(Date biddingEnds) {
		this.biddingEnds = biddingEnds;
	}

	public Double getBuyout() {
		return buyout;
	}

	public Double getStart() {
		return start;
	}

	public void setStart(Double start) {
		this.start = start;
	}
}
