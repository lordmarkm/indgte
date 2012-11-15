package com.baldwin.indgte.pers‪istence.dao;

import java.util.Collection;

import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.Tag;
import com.baldwin.indgte.persistence.model.User;

public interface TradeDao {
	BuyAndSellItem get(long itemId);
	Collection<BuyAndSellItem> getItems(int start, int howmany, String orderColumn);
	Collection<BuyAndSellItem> getItems(User user);
	void save(User user, BuyAndSellItem item);
	BuyAndSellItem get(String name, long itemId);
	
	/**
	 * Will return 0 on successful bid, -1 if auction is over, and item.winning on failed bid
	 * @return  0    : successful bid <br>
	 * 			-1    : auction over <br>
	 * 		    number: minimum bid <br>
	 */
	double bid(User user, long itemId, double amount, double minIncrementPercent);
	Tag getTag(String tagString, boolean createIfAbsent);
	Collection<BuyAndSellItem> getItemsWithTag(String tag, int start, int howmany);
	Collection<BuyAndSellItem> getWatchedTagItems(String name, int start, int howmany);
}
