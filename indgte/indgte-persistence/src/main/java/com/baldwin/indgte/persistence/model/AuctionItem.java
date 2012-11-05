package com.baldwin.indgte.persistence.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name="buyandsell_bidding")
public class AuctionItem extends BuyAndSellItem {
	@Column
	private Double start;
	
	@Column
	private Double buyout;

	@Column(name="increment")
	private Double biddingMinimumIncrement;
	
	@Column(name="ends")
	@Temporal(TemporalType.DATE)
	private Date biddingEnds;

	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="item")
	private List<Bid> bids;

	public Bid getWinning() {
		if(getBids().size() == 0) {
			return null;
		}
		return bids.get(bids.size() - 1);
	}
	
	public List<Bid> getReversedBids() {
		List<Bid> reversed = new ArrayList<Bid>(bids);
		Collections.reverse(reversed);
		return reversed;
	}
	
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

	@JsonIgnore
	public List<Bid> getBids() {
		if(null == bids) bids = new Stack<Bid>();
		return bids;
	}

	public void setBids(Stack<Bid> bids) {
		this.bids = bids;
	}
}