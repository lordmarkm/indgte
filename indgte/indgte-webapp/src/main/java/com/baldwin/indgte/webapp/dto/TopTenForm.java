package com.baldwin.indgte.webapp.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.jboss.logging.Logger;

import com.baldwin.indgte.persistence.model.TopTenCandidate;
import com.baldwin.indgte.persistence.model.TopTenList;

public class TopTenForm {
	static Logger log = Logger.getLogger(TopTenForm.class);
	
	private TopTenList list;
	private List<TopTenCandidate> candidates;
	
	public TopTenForm() {
		list = new TopTenList();
		candidates = new ArrayList<TopTenCandidate>();
	}
	
	public TopTenList getList() {
		list.setCandidates(new HashSet<TopTenCandidate>(candidates));
		return list;
	}

	public void setList(TopTenList list) {
		this.list = list;
	}

	public String getTitle() {
		return list.getTitle();
	}

	public void setTitle(String title) {
		list.setTitle(title);
	}

	public List<TopTenCandidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<TopTenCandidate> candidates) {
		this.candidates = candidates;
	}

	public long getId() {
		return list.getId();
	}

	public void setId(long id) {
		list.setId(id);
	}

	public Date getTime() {
		return list.getTime();
	}

	public void setTime(Date time) {
		list.setTime(time);
	}

	public int getTotalVotes() {
		return list.getTotalVotes();
	}

	public void setTotalVotes(int totalVotes) {
		list.setTotalVotes(totalVotes);
	}

	public String toString() {
		return list.toString();
	}
}