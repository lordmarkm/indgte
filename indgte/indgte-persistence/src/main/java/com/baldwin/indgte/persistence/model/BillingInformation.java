package com.baldwin.indgte.persistence.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BillingInformation {

	@Column
	private int coconuts = 25;

	public int getCoconuts() {
		return coconuts;
	}

	public void setCoconuts(int coconuts) {
		this.coconuts = coconuts;
	}
	
}
