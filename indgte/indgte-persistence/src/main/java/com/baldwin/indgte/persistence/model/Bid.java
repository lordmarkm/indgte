package com.baldwin.indgte.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Bid on an {@link AuctionItem}
 * @author mbmartinez
 */

@Entity
@Table(name="buyandsell_bids")
public class Bid implements Comparable<Bid> {
	@Id
	@GeneratedValue
	private long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date time;
	
	@ManyToOne
	@JoinColumn(name="itemId", nullable=false, updatable=false)
	private AuctionItem item;
	
	@ManyToOne
	@JoinColumn(name="userId", nullable=false, updatable=false)
	private User bidder;
	
	@Column
	private Double amount;

	@Override
	public int compareTo(Bid bid) {
		return (int) (bid.amount - this.amount);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public AuctionItem getItem() {
		return item;
	}

	public void setItem(AuctionItem item) {
		this.item = item;
	}

	public User getBidder() {
		return bidder;
	}

	public void setBidder(User bidder) {
		this.bidder = bidder;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
