package com.baldwin.indgte.persistence.model;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.baldwin.indgte.persistence.constants.ReviewType;


@Entity
@Table(name="notifications_reviewreact")
public class ReviewReactNotification extends Notification {

	public enum ReactionMode {
		agree,
		disagree
	}
	
	@Column
	@Enumerated
	private ReactionMode mode;

	@Column
	@Enumerated
	private ReviewType reviewType;
	
	@Column
	@Lob @Basic
	private String reactors;
	
	@Column
	private String revieweeIdentifier;
	
	@Column
	private String revieweeTitle;
	
	@Column
	private String lastReactorImageUrl;
	
	@Override
	public NotificationType getType() {
		return NotificationType.reviewreaction;
	}

	public ReactionMode getMode() {
		return mode;
	}

	public void setMode(ReactionMode mode) {
		this.mode = mode;
	}

	public String getReactors() {
		return reactors;
	}

	public void setReactors(String reactors) {
		this.reactors = reactors;
	}

	public ReviewType getReviewType() {
		return reviewType;
	}

	public void setReviewType(ReviewType reviewType) {
		this.reviewType = reviewType;
	}

	public String getRevieweeTitle() {
		return revieweeTitle;
	}

	public void setRevieweeTitle(String revieweeTitle) {
		this.revieweeTitle = revieweeTitle;
	}

	public String getLastReactorImageUrl() {
		return lastReactorImageUrl;
	}

	public void setLastReactorImageUrl(String lastReactorImageUrl) {
		this.lastReactorImageUrl = lastReactorImageUrl;
	}

	public String getRevieweeIdentifier() {
		return revieweeIdentifier;
	}

	public void setRevieweeIdentifier(String revieweeIdentifier) {
		this.revieweeIdentifier = revieweeIdentifier;
	}


}
