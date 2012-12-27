package com.baldwin.indgte.persistence.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Indexed
@Entity
@Table(name="toptenlists")
public class TopTenList {
	@Id 
	@GeneratedValue
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "creatorId")
	private UserExtension creator;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date time;
	
	@Field(boost=@Boost(value=2f))
	@Column
	private String title;
	
	@Field
	@Column
	@Lob @Basic(fetch=FetchType.EAGER)
	private String description;
	
	@OneToMany(
		cascade = CascadeType.ALL,
		mappedBy = "list",
		orphanRemoval = true
	)
	private Set<TopTenCandidate> candidates;

	@Column
	private int totalVotes;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonIgnore
	public Set<TopTenCandidate> getCandidates() {
		if(null == candidates) {
			candidates = new LinkedHashSet<TopTenCandidate>();
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
	public UserExtension getCreator() {
		return creator;
	}

	public void setCreator(UserExtension creator) {
		this.creator = creator;
	}

	@JsonIgnore
	public List<TopTenCandidate> getOrdered() {
		List<TopTenCandidate> ordered = new ArrayList<TopTenCandidate>(candidates);
		Collections.sort(ordered);
		return ordered;
	}
	
	public TopTenCandidate getLeader() {
		List<TopTenCandidate> ordered = getOrdered();
		TopTenCandidate leader = ordered.size() > 0 ? ordered.get(0) : null;
		return leader;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
