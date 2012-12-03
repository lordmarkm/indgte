package com.baldwin.indgte.persistence.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="notifications_likes")
public class LikeNotification extends Notification {

	@Column
	@Lob @Basic
	private String likers;
	
	@Column
	private String lastLikerId;
	
	@Column
	@Enumerated
	private InteractableType likeableType;
	
	@Column
	private String targetTitle;
	
	@Column
	private long targetId;
	
	@Override
	public NotificationType getType() {
		return NotificationType.like;
	}

	public String getLikers() {
		return likers;
	}

	public void setLikers(String likers) {
		this.likers = likers;
	}

	public String getLastLikerId() {
		return lastLikerId;
	}

	public void setLastLikerId(String lastLikerId) {
		this.lastLikerId = lastLikerId;
	}

	public InteractableType getLikeableType() {
		return likeableType;
	}

	public void setLikeableType(InteractableType likeableType) {
		this.likeableType = likeableType;
	}

	public String getTargetTitle() {
		return targetTitle;
	}

	public void setTargetTitle(String targetTitle) {
		this.targetTitle = targetTitle;
	}

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

}
