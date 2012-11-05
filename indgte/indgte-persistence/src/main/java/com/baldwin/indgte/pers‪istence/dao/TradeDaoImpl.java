package com.baldwin.indgte.pers‪istence.dao;

import java.util.Collection;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.model.AuctionItem;
import com.baldwin.indgte.persistence.model.Bid;
import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.User;

@Repository
@Transactional
public class TradeDaoImpl implements TradeDao {

	@Autowired
	private SessionFactory sessions;
	
	@Override
	public BuyAndSellItem get(String name, long itemId) {
		BuyAndSellItem item = (BuyAndSellItem) sessions.getCurrentSession().get(BuyAndSellItem.class, itemId);
		
		//do not increment if it's the owner viewing his own item
		if(!item.getOwner().getUsername().equals(name)) {
			item.setViews(item.getViews() + 1);
		}
		
		if(item instanceof AuctionItem) {
			Hibernate.initialize(((AuctionItem)item).getBids());
		}
		
		return item;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<BuyAndSellItem> getItems(int start, int howmany, String orderColumn) {
		Session session = sessions.getCurrentSession();
		Criteria criteria = session.createCriteria(BuyAndSellItem.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(null != orderColumn) criteria.addOrder(Order.desc(orderColumn));
		if(start > -1) criteria.setFirstResult(start);
		if(howmany > 0)	criteria.setMaxResults(howmany);
		
		return criteria.list();
	}

	@Override
	public Collection<BuyAndSellItem> getItems(User user) {
		Session session = sessions.getCurrentSession();
		session.refresh(user);
		Hibernate.initialize(user.getBuyAndSellItems());
		return user.getBuyAndSellItems();
	}

	@Override
	public void save(User user, BuyAndSellItem item) {
		item.setOwner(user);
		item.setTime(new Date());
		
		user.getBuyAndSellItems().add(item);
		
		sessions.getCurrentSession().save(item);
	}

	@Override
	public double bid(User user, long itemId, double amount, double minIncrementPercent) {
		Session session = sessions.getCurrentSession();
		
		AuctionItem item = (AuctionItem) session.get(AuctionItem.class, itemId);
		
		//if auction is finished, return -1
		if(System.currentTimeMillis() - item.getBiddingEnds().getTime() > 0) return -1d;
		
		//if bid is less than current winning bid, return that info to user
		Bid winning = item.getWinning();
		if(null != winning) {
			double minBid = minIncrementPercent * item.getStart() + winning.getAmount();
			minBid = Math.ceil(minBid);
			if(minBid > amount) {
				return Math.ceil(minBid);
			}
		} else if(amount < item.getStart()) {
			//no other bids, but bid is less than the starting price of the item
			return item.getStart();
		}
		
		Bid bid = new Bid();
		bid.setAmount(amount);
		bid.setBidder(user);
		bid.setItem(item);
		bid.setTime(new Date());
		
		item.getBids().add(bid);
		
		session.save(bid);
		return 0;
	}
}
