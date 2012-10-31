package com.baldwin.indgte.persistence.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name="toptenlists")
public class TopTenList {
	@Id 
	@GeneratedValue
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "creatorId")
	private User creator;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date time;
	
	@Column
	private String title;
	
	@OneToMany(
		cascade = CascadeType.ALL,
		mappedBy = "list",
		orphanRemoval = true,
		fetch = FetchType.EAGER
	)
	private Set<TopTenCandidate> candidates;

	@Column
	private int totalVotes;
	
	@Transient
	private TopTenCandidate leader;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonIgnore
	public Set<TopTenCandidate> getCandidates() {
		if(null == candidates) {
			candidates = new HashSet<TopTenCandidate>();
		}
		return candidates;
	}

	public void setCandidates(Set<TopTenCandidate> candidates) {
		this.candidates = candidates;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getTotalVotes() {
		return totalVotes;
	}

	public void setTotalVotes(int totalVotes) {
		this.totalVotes = totalVotes;
	}

	@JsonIgnore
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public TopTenCandidate getLeader() {
		return leader;
	}

	public void setLeader(TopTenCandidate leader) {
		this.leader = leader;
	}
}
