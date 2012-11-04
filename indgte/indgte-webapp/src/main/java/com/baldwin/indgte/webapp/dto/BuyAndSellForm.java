package com.baldwin.indgte.webapp.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baldwin.indgte.persistence.constants.BuyAndSellMode;
import com.baldwin.indgte.persistence.model.AuctionItem;
import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.FixedPriceItem;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.TradeItem;

public class BuyAndSellForm {
	static Logger log = LoggerFactory.getLogger(BuyAndSellForm.class);
	
	private String name;
	private String description;
	private BuyAndSellMode sellMode;
	private String hash;
	private String deletehash;
	
	//trade item
	private String tradefor;

	//fixedprice item
	private Double fixedprice;
	private Boolean negotiable = Boolean.FALSE;
	
	//auction item
	private Double startingprice;
	private Double buyout;
	private String enddate;
	
	@Override
	public String toString() {
		return "BuyAndSellForm [name=" + name + ", description=" + description
				+ ", hash=" + hash + ", sellMode=" + sellMode 
				+ ", tradefor=" + tradefor + ", fixedprice="
				+ fixedprice + ", negotiable=" + negotiable
				+ ", startingprice=" + startingprice + ", buyout=" + buyout
				+ ", enddate=" + enddate + "]";
	}
	
	public BuyAndSellItem getItem() {
		BuyAndSellItem item = null;
		switch(sellMode) {
		case auction:
			AuctionItem biddingItem = new AuctionItem();
			biddingItem.setStart(startingprice);
			biddingItem.setBuyout(buyout);
			try {
				biddingItem.setBiddingEnds(new SimpleDateFormat("MM/dd/yyyy").parse(enddate));
			} catch (ParseException e) {
				log.error("Exception parsing end date", e);
				return null;
			}
			item = biddingItem;
			break;
		case fixed:
			FixedPriceItem fixedItem = new FixedPriceItem();
			fixedItem.setPrice(fixedprice);
			fixedItem.setNegotiable(negotiable);
			item = fixedItem;
			break;
		case trade:
			TradeItem tradeItem = new TradeItem();
			tradeItem.setTradefor(tradefor);
			item = tradeItem;
			break;
		default:
			log.error("Unsupported sellmode: {}", sellMode);
			return null;
		}
		
		item.setName(name);
		item.setDescription(description);
		item.setBuyAndSellMode(sellMode);
		
		Imgur imgur = new Imgur();
		imgur.setHash(hash);
		imgur.setDeletehash(deletehash);
		item.setImgur(imgur);
		
		return item;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public BuyAndSellMode getSellMode() {
		return sellMode;
	}
	public void setSellMode(BuyAndSellMode sellMode) {
		this.sellMode = sellMode;
	}
	public String getTradefor() {
		return tradefor;
	}
	public void setTradefor(String tradefor) {
		this.tradefor = tradefor;
	}
	public Double getFixedprice() {
		return fixedprice;
	}
	public void setFixedprice(Double fixedprice) {
		this.fixedprice = fixedprice;
	}
	public Boolean isNegotiable() {
		return negotiable;
	}
	public void setNegotiable(Boolean negotiable) {
		this.negotiable = negotiable;
	}
	public Double getStartingprice() {
		return startingprice;
	}
	public void setStartingprice(Double startingprice) {
		this.startingprice = startingprice;
	}
	public Double getBuyout() {
		return buyout;
	}
	public void setBuyout(Double buyout) {
		this.buyout = buyout;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getDeletehash() {
		return deletehash;
	}

	public void setDeletehash(String deletehash) {
		this.deletehash = deletehash;
	}
}
