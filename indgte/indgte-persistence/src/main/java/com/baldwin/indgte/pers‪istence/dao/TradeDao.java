package com.baldwin.indgte.pers‪istence.dao;

import java.util.Collection;

import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.User;

public interface TradeDao {

	public Collection<BuyAndSellItem> getItems(int start, int howmany, String orderColumn);
	public Collection<BuyAndSellItem> getItems(User user);
	public void save(User user, BuyAndSellItem item);
	public BuyAndSellItem get(String name, long itemId);

}
