package com.baldwin.indgte.persistence.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="notifications_comments")
public class CommentNotification extends Notification {

	@Column
	@Lob @Basic
	private String commenters;
	
	@Column
	private String lastCommenterId;
	
	@Column
	@Enumerated
	private InteractableType commentableType;
	
	@Column
	private String targetTitle;
	
	@Column
	private long targetId;
	
	@Override
	public NotificationType getType() {
		return NotificationType.comment;
	}

	public String getCommenters() {
		return commenters;
	}

	public void setCommenters(String commenters) {
		this.commenters = commenters;
	}

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public InteractableType getCommentableType() {
		return commentableType;
	}

	public void setCommentableType(InteractableType commentableType) {
		this.commentableType = commentableType;
	}

	public String getTargetTitle() {
		return targetTitle;
	}

	public void setTargetTitle(String targetTitle) {
		this.targetTitle = targetTitle;
	}

	public String getLastCommenterId() {
		return lastCommenterId;
	}

	public void setLastCommenterId(String lastCommenterId) {
		this.lastCommenterId = lastCommenterId;
	}

}
