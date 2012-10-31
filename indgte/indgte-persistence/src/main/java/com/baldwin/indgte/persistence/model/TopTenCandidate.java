package com.baldwin.indgte.persistence.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name="toptencandidates")
public class TopTenCandidate implements Comparable<TopTenCandidate> {
	@Id
	@GeneratedValue
	@Column(name = "itemId")
	private long id;
	
	@ManyToOne
	@JoinColumn(
		name = "listId", 
		nullable = false,
		updatable = false
	)
	private TopTenList list;
	
	@ManyToOne
	@JoinColumn(
		name = "creatorId", 
		nullable=false, 
		updatable=false
	)
	private User creator;
	
	@Column
	private int votes;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
		name="toptenvotes",
		joinColumns = {@JoinColumn(name = "userId")},
		inverseJoinColumns = {@JoinColumn(name = "candidateId")}
	)
	private Set<User> voters;
	
	@Column
	private String title;
	
	@Column
	private String link;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="imgurId")
	private Imgur imgur;
	
	@Override
	public int compareTo(TopTenCandidate competition) {
		return competition.votes - this.votes;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TopTenList getList() {
		return list;
	}

	public void setList(TopTenList list) {
		this.list = list;
	}

	@JsonIgnore
	public Set<User> getVoters() {
		if(null == voters) {
			voters = new HashSet<User>();
		}
		return voters;
	}

	public void setVoters(Set<User> voters) {
		this.voters = voters;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Imgur getImgur() {
		return imgur;
	}

	public void setImgur(Imgur imgur) {
		this.imgur = imgur;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}
}
