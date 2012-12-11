package com.baldwin.indgte.persistence.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name="notifications_newbids")
public class NewBidNotification extends Notification {

	@Override
	public NotificationType getType() {
		return NotificationType.newbid;
	}
	
	@ManyToOne
	@JoinColumn(name="auctionitem_id", nullable=false, updatable=false)
	private AuctionItem item;

	public long getId() {
		return item.getId();
	}

	public String getName() {
		return item.getName();
	}
	
	public String imageUrl() {
		return item.getImgur() == null ? null : item.getImgur().getSmallSquare();
	}
	
	@JsonIgnore
	public AuctionItem getItem() {
		return item;
	}

	public void setItem(AuctionItem item) {
		this.item = item;
	}

}
