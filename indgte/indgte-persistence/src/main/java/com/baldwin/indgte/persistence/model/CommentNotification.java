package com.baldwin.indgte.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name="notifications_comments")
public class CommentNotification extends Notification {

	public enum CommentableType {
		post,
		review,
		business,
		category,
		product,
		fixedpriceitem,
		auctionitem,
		tradeitem
	}
	
	@Column
	private String commenters;
	
	@Column
	@Enumerated
	private CommentableType commentableType;
	
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

	public CommentableType getCommentableType() {
		return commentableType;
	}

	public void setCommentableType(CommentableType commentableType) {
		this.commentableType = commentableType;
	}

}
