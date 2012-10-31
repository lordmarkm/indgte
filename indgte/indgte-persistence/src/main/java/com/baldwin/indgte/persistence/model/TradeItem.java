package com.baldwin.indgte.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="buyandsell_trade")
public class TradeItem extends BuyAndSellItem {
	@Column
	private String tradefor;

	public String getTradefor() {
		return tradefor;
	}

	public void setTradefor(String tradefor) {
		this.tradefor = tradefor;
	}
}
