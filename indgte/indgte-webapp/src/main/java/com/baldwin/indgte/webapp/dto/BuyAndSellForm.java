package com.baldwin.indgte.webapp.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
	private String tags;
	
	//trade item
	private String tradefor;

	//fixedprice item
	private Double fixedprice = 0d;
	private Boolean negotiable = Boolean.FALSE;
	
	//auction item
	private Double startingprice = 0d;
	private Double buyout = 0d;
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
		
		String[] tagArray = tags.trim().toLowerCase().split("\\s+");
		if(tagArray.length > 5) {
			tagArray = Arrays.copyOfRange(tagArray, 0, 5);
		}
		Set<String> tagset = new HashSet<String>(Arrays.asList(tagArray));
		StringBuilder tagBuilder = new StringBuilder();
		for(Iterator<String> i = tagset.iterator(); i.hasNext();) {
			tagBuilder.append(i.next());
			if(i.hasNext()) tagBuilder.append(' ');
		}
		item.setTags(tagBuilder.toString());
		
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
		this.fixedprice = Math.ceil(fixedprice);
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
		this.startingprice = Math.ceil(startingprice);
	}
	public Double getBuyout() {
		return buyout;
	}
	public void setBuyout(Double buyout) {
		this.buyout = Math.ceil(buyout);
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

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Boolean getNegotiable() {
		return negotiable;
	}
}
