package com.baldwin.indgte.persistence.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.User;

@Service
public class BuyAndSellService {

	public Collection<BuyAndSellItem> getPopular(int start, int howmany) {
		return null;
	}

	public Collection<BuyAndSellItem> getRecent(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<BuyAndSellItem> getOwned(User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
