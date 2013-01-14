package com.baldwin.indgte.persistence.model;

import java.util.Set;

import com.baldwin.indgte.persistence.constants.ReviewType;
import com.baldwin.indgte.persistence.dto.Summary;

public interface Review {
	public ReviewType getReviewType();
	public UserExtension getReviewer();
	public Summary getReviewerSummary();
	public Summary getRevieweeSummary();
	public Set<UserExtension> getAgreers();
	public Set<UserExtension> getDisagreers();
	public int getAgreeCount();
	public void setAgreeCount(int agreeCount);
	public int getDisagreeCount();
	public void setDisagreeCount(int disagreeCount);
	public int getScore();
	public long getId();
	public String getJustification();
}
