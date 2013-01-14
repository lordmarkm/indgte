package com.baldwin.indgte.persistence.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.baldwin.indgte.persistence.constants.ReviewType;
import com.baldwin.indgte.persistence.constants.ReviewerType;
import com.baldwin.indgte.persistence.dto.Summary;

@Entity
@Table(name="businessReviews")
public class BusinessReview implements Review {
	@Id
	@GeneratedValue
	private long id;

	@OneToOne(optional=true, mappedBy="businessReview", cascade={CascadeType.ALL})
	private ReviewNotification notification;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="reviewerId", nullable=false, updatable=false)
	private UserExtension reviewer;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="reviewedId", nullable=false, updatable=false)
	private BusinessProfile reviewed;
	
	@Column
	private int agreeCount = 0;
	
	@Column
	private int disagreeCount = 0;
	
	@ManyToMany
	@JoinTable(
		name="businessreviewagreers",
		joinColumns = {@JoinColumn(name="reviewId")},
		inverseJoinColumns = {@JoinColumn(name="userId")}
	)
	private Set<UserExtension> agreers;
	
	@ManyToMany
	@JoinTable(
		name="businessreviewdisagreers",
		joinColumns = {@JoinColumn(name="reviewId")},
		inverseJoinColumns = {@JoinColumn(name="userId")}
	)
	private Set<UserExtension> disagreers;
	
	@Column
	private int score;
	
	@Column
	@Lob @Basic
	private String justification;
	
	@Column
	@Enumerated
	private ReviewerType type = ReviewerType.user;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date time;
	
	@Column
	private int comments = 0;
	
	@Override
	public ReviewType getReviewType() {
		return ReviewType.business;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public Summary getReviewerSummary() {
		return reviewer.summarize();
	}
	
	@JsonIgnore
	@Override
	public UserExtension getReviewer() {
		return reviewer;
	}

	public void setReviewer(UserExtension reviewer) {
		this.reviewer = reviewer;
	}

	@JsonIgnore
	public BusinessProfile getReviewed() {
		return reviewed;
	}

	public void setReviewed(BusinessProfile reviewed) {
		this.reviewed = reviewed;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public ReviewerType getType() {
		return type;
	}

	public void setType(ReviewerType type) {
		this.type = type;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public Summary getRevieweeSummary() {
		return reviewed.summarize();
	}

	@Override
	@JsonIgnore
	public Set<UserExtension> getAgreers() {
		if(null == agreers) {
			this.agreers = new HashSet<UserExtension>();
		}
		return agreers;
	}

	public void setAgreers(Set<UserExtension> agreers) {
		this.agreers = agreers;
	}
	
	@Override
	@JsonIgnore
	public Set<UserExtension> getDisagreers() {
		if(null == disagreers) {
			this.disagreers = new HashSet<UserExtension>();
		}
		return disagreers;
	}

	public void setDisagreers(Set<UserExtension> disagreers) {
		this.disagreers = disagreers;
	}
	
	@Override
	public int getAgreeCount() {
		return agreeCount;
	}

	@Override
	public void setAgreeCount(int agreeCount) {
		this.agreeCount = agreeCount;
	}

	@Override
	public int getDisagreeCount() {
		return disagreeCount;
	}

	@Override
	public void setDisagreeCount(int disagreeCount) {
		this.disagreeCount = disagreeCount;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	@JsonIgnore
	public ReviewNotification getNotification() {
		return notification;
	}

	public void setNotification(ReviewNotification notification) {
		this.notification = notification;
	}
}
