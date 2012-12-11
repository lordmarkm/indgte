package com.baldwin.indgte.persistence.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.Tag;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.pers‪istence.dao.TableConstants;
import com.baldwin.indgte.pers‪istence.dao.TradeDao;

@Service
public class TradeService {

	@Value("${auction.minimum.increment}")
	private double bidIncrement;
	
	@Autowired
	private TradeDao dao;
	
	public Collection<BuyAndSellItem> getPopular(int start, int howmany) {
		return dao.getItems(start, howmany, TableConstants.BUYANDSELL_VIEWS);
	}

	public Collection<BuyAndSellItem> getRecent(int start, int howmany) {
		return dao.getItems(start, howmany, TableConstants.BUYANDSELL_TIME);
	}

	public Collection<BuyAndSellItem> getOwned(UserExtension user) {
		return dao.getItems(user);
	}

	public void save(String name, BuyAndSellItem item) {
		dao.save(name, item);
	}

	/**
	 * Will increment views, but not if item.owner.name = name!
	 */
	public BuyAndSellItem get(String name, long itemId) {
		return dao.get(name, itemId);
	}

	public double bid(String bidderName, long itemId, double amount) {
		return dao.bid(bidderName, itemId, amount, bidIncrement);
	}
	
	public double getBidIncrement() {
		return bidIncrement;
	}

	public Collection<BuyAndSellItem> getItemsWithTag(String tag, int start, int howmany) {
		return dao.getItemsWithTag(tag, start, howmany);
	}

	public Tag getTag(String tag) {
		return dao.getTag(tag, false);
	}
	
	public Collection<BuyAndSellItem> getWatchedTagItems(String name, int start, int howmany) {
		return dao.getWatchedTagItems(name, null, start, howmany);
	}

	public Collection<BuyAndSellItem> getWatchedTagItems(String name, String tagString, int start, int howmany) {
		return dao.getWatchedTagItems(name, tagString, start, howmany);
	}

	public void addToWatchedTags(String name, String tag) {
		dao.addToWatchedTags(name, tag);
	}
}
