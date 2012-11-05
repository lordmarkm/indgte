package com.baldwin.indgte.pers‪istence.dao;

import java.util.Collection;

import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.User;

public interface TradeDao {

	public Collection<BuyAndSellItem> getItems(int start, int howmany, String orderColumn);
	public Collection<BuyAndSellItem> getItems(User user);
	public void save(User user, BuyAndSellItem item);
	public BuyAndSellItem get(String name, long itemId);
	
	/**
	 * Will return 0 on successful bid, -1 if auction is over, and item.winning on failed bid
	 * @return  0    : successful bid <br>
	 * 			-1    : auction over <br>
	 * 		    number: minimum bid <br>
	 */
	public double bid(User user, long itemId, double amount, double minIncrementPercent);
}
