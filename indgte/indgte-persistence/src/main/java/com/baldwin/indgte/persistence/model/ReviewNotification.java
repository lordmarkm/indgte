package com.baldwin.indgte.persistence.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.baldwin.indgte.persistence.constants.ReviewType;
import com.baldwin.indgte.persistence.dto.Summary;

@Entity
@Table(name="notifications_review")
public class ReviewNotification extends Notification {

	@Column
	@Enumerated
	private ReviewType reviewType;
	
	@OneToOne
	@JoinColumn(name="userReviewId")
	private UserReview userReview;

	@OneToOne
	@JoinColumn(name="businessReviewId")
	private BusinessReview businessReview;
	
	@Override
	public NotificationType getType() {
		return NotificationType.review;
	}
	
	public Summary getRevieweeSummary() {
		switch(reviewType) {
		case business:
			return businessReview.getRevieweeSummary();
		case user:
			return userReview.getRevieweeSummary();
		}
		return null;
	}
	
	public Summary getReviewerSummary() {
		switch(reviewType) {
		case business:
			return businessReview.getReviewerSummary();
		case user:
			return userReview.getReviewerSummary();
		}
		return null;
	}
	
	public int getScore() {
		switch(reviewType) {
		case business:
			return businessReview.getScore();
		case user:
			return userReview.getScore();
		}
		return 0;
	}

	@JsonIgnore
	public UserReview getUserReview() {
		return userReview;
	}

	public void setUserReview(UserReview userReview) {
		this.userReview = userReview;
	}

	@JsonIgnore
	public BusinessReview getBusinessReview() {
		return businessReview;
	}

	public void setBusinessReview(BusinessReview businessReview) {
		this.businessReview = businessReview;
	}

	public ReviewType getReviewType() {
		return reviewType;
	}

	public void setReviewType(ReviewType reviewType) {
		this.reviewType = reviewType;
	}

}
