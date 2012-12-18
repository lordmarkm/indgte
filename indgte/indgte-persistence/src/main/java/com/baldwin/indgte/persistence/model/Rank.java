package com.baldwin.indgte.persistence.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Rank {

	@Column
	private int postFame = 0;
	
	@Column
	private int reviewFame = 0;
	
	@Column
	private int friendshipFame = 0;
	
	@Column
	private int entityFame = 0;
	
	@Column
	private int totalFame = 0;
	
	@Column
	private String prefix = "";
	
	@Column
	private String rank = "Dumagueteño";
	
	@Override
	public String toString() {
		return prefix.length() == 0 ? rank : prefix + " " + rank;
	}
	
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public int getPostFame() {
		return postFame;
	}

	public void setPostFame(int postFame) {
		this.postFame = postFame;
	}

	public int getReviewFame() {
		return reviewFame;
	}

	public void setReviewFame(int reviewFame) {
		this.reviewFame = reviewFame;
	}

	public int getFriendshipFame() {
		return friendshipFame;
	}

	public void setFriendshipFame(int friendshipFame) {
		this.friendshipFame = friendshipFame;
	}

	public int getEntityFame() {
		return entityFame;
	}

	public void setEntityFame(int entityFame) {
		this.entityFame = entityFame;
	}

	public void setTotalFame(int total) {
		this.totalFame = total;		
	}
	
	public int getTotalFame() {
		return totalFame;
	}
}
