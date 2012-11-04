package com.baldwin.indgte.persistence.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.pers‪istence.dao.TableConstants;
import com.baldwin.indgte.pers‪istence.dao.TradeDao;

@Service
public class TradeService {

	@Autowired
	private TradeDao dao;
	
	public Collection<BuyAndSellItem> getPopular(int start, int howmany) {
		return dao.getItems(start, howmany, TableConstants.BUYANDSELL_VIEWS);
	}

	public Collection<BuyAndSellItem> getRecent(int start, int howmany) {
		return dao.getItems(start, howmany, TableConstants.BUYANDSELL_TIME);
	}

	public Collection<BuyAndSellItem> getOwned(User user) {
		return dao.getItems(user);
	}

	public void save(User user, BuyAndSellItem item) {
		dao.save(user, item);
	}

	/**
	 * Will increment views, but not if item.owner.name = name!
	 */
	public BuyAndSellItem get(String name, long itemId) {
		return dao.get(name, itemId);
	}
}
