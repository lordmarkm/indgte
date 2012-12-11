package com.baldwin.indgte.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name="notifications_toptenvote")
public class TopTenVoteNotification extends Notification {
	
	@OneToOne
	private TopTenList topten;
	
	@Column
	private String imageUrl;
	
	@Override
	public NotificationType getType() {
		return NotificationType.toptenvote;
	}

	public String getTitle() {
		return topten.getTitle();
	}

	public long getTopTenId() {
		return topten.getId();
	}
	
	@JsonIgnore
	public TopTenList getTopten() {
		return topten;
	}
	
	public void setTopten(TopTenList topten) {
		this.topten = topten;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
