package com.baldwin.indgte.pers‪istence.dao;

import static com.baldwin.indgte.persistence.constants.SearchAndSummaryConstants.luceneVersion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.constants.SearchAndSummaryConstants;
import com.baldwin.indgte.persistence.model.AuctionItem;
import com.baldwin.indgte.persistence.model.Bid;
import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.Tag;
import com.baldwin.indgte.persistence.model.UserExtension;

@Repository
@Transactional
public class TradeDaoImpl implements TradeDao {
	
	static Logger log = LoggerFactory.getLogger(TradeDaoImpl.class);
	
	@Autowired
	private SessionFactory sessions;
	
	@Autowired
	private UserDao users;
	
	@Override
	public BuyAndSellItem get(long itemId) {
		return (BuyAndSellItem) sessions.getCurrentSession().get(BuyAndSellItem.class, itemId);
	}
	
	@Override
	public BuyAndSellItem get(String name, long itemId) {
		BuyAndSellItem item = (BuyAndSellItem) sessions.getCurrentSession().get(BuyAndSellItem.class, itemId);
		if(null == item) {
			return null;
		}
		
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
	public Collection<BuyAndSellItem> getItems(UserExtension user) {
		Session session = sessions.getCurrentSession();
		session.refresh(user);
		Hibernate.initialize(user.getBuyAndSellItems());
		return user.getBuyAndSellItems();
	}

	@Override
	public void save(String name, BuyAndSellItem item) {
		UserExtension user = users.getExtended(name);
		item.setOwner(user);
		item.setTime(new Date());
		
		user.getBuyAndSellItems().add(item);
		
		String[] tagStrings = toTagStrings(item.getTags());
		for(String tagString : tagStrings) {
			Tag tag = getTag(tagString, true);
			tag.setItems(tag.getItems() + 1);
		}
		
		sessions.getCurrentSession().save(item);
	}

	@Override
	public Tag getTag(String tagString, boolean createIfAbsent) {
		Session session = sessions.getCurrentSession();
		
		Tag tag = (Tag) session.createCriteria(Tag.class)
					.add(Restrictions.eq(TableConstants.TAG, tagString))
					.uniqueResult();
		
		if(null == tag && createIfAbsent) {
			tag = new Tag(tagString);
			session.save(tag);
		}
		
		return tag;
	}
	
	@Override
	public double bid(String bidderName, long itemId, double amount, double minIncrementPercent) {
		Session session = sessions.getCurrentSession();
		
		UserExtension user = users.getExtended(bidderName);
		
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
	
	protected String[] toTagStrings(String tagString) {
		if(null == tagString) return new String[]{};
		return tagString.trim().split("\\s+");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<BuyAndSellItem> getItemsWithTag(String tag, int start, int howmany) {
		FullTextSession ftSession = Search.getFullTextSession(sessions.getCurrentSession());
		
		org.apache.lucene.search.Query lQuery = null;
		try {
			lQuery = new QueryParser(luceneVersion, TableConstants.BUYANDSELL_TAGS, new WhitespaceAnalyzer(SearchAndSummaryConstants.luceneVersion)).parse(tag);
			log.debug("Parsed lucene query [{}]", lQuery.toString());
		} catch (ParseException e) {
			log.error("Bad query", e);
		}
		
		Query query = ftSession.createFullTextQuery(lQuery, BuyAndSellItem.class)
				.setFirstResult(start).setMaxResults(howmany);

		return query.list();
	}

	
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<BuyAndSellItem> getWatchedTagItems(String name, String tagString, int start, int howmany) {
		log.debug("Searching for watched tags of {}", name);
		UserExtension user = users.getExtended(name);
		
		if(user.getWatchedTags().size() < 1) {
			log.debug("No watched tags. Returning empty arraylist.");
			return new ArrayList<BuyAndSellItem>();
		}
		
		String queryString = tagString; //get a particular tag
		if(null == tagString) { //get all instead
			StringBuilder queryStringBuilder = new StringBuilder();
			for(Tag tag : user.getWatchedTags()) {
				queryStringBuilder.append(tag.getTag()).append(" ");
			}
			queryString = queryStringBuilder.toString();
		}
		log.debug("Query string: " + queryString);
		
		FullTextSession ftSession = Search.getFullTextSession(sessions.getCurrentSession());
		org.apache.lucene.search.Query lQuery = null;
		try {
			lQuery = new QueryParser(luceneVersion, TableConstants.BUYANDSELL_TAGS, new WhitespaceAnalyzer(luceneVersion)).parse(queryString);
		} catch (ParseException e) {
			log.error("Bad search query", e);
		}
		ConstantScoreQuery csq = new ConstantScoreQuery(lQuery); //disable scores so we can sort exclusively by date
		
		Query query = ftSession.createFullTextQuery(csq,  BuyAndSellItem.class)
				.setSort(new Sort(new SortField(TableConstants.ID, SortField.LONG, true)))
				.setFirstResult(start).setMaxResults(howmany);
		return query.list();
	}

	@Override
	public void addToWatchedTags(String name, String tagString) {
		UserExtension user = users.getExtended(name);
		Tag tag = getTag(tagString, false);
		user.getWatchedTags().add(tag);
	}

	@Override
	public void sold(boolean moderator, String name, long itemId) throws IllegalAccessException {
		BuyAndSellItem item = get(itemId);
		
		if(moderator) {
			log.info("Moderator {} has deleted item with id itemId {}", name, itemId);
		}
		
		if(moderator || item.getOwner().getUsername().equals(name)) {
			item.setSoldout(true);
		} else {
			throw new IllegalAccessException(name + " does not own this item and may not mark it as sold");
		}
	}
	
	@Override
	public void available(boolean moderator, String name, long itemId) throws IllegalAccessException {
		BuyAndSellItem item = get(itemId);
		
		if(moderator) {
			log.info("Moderator {} has deleted item with id itemId {}", name, itemId);
		}
			
		if(moderator || item.getOwner().getUsername().equals(name)) {
			item.setSoldout(false);
		} else {
			throw new IllegalAccessException(name + " does not own this item and may not mark it as available");
		}
	}
	
	@Override
	public void delete(boolean moderator, String name, long itemId) throws IllegalAccessException {
		BuyAndSellItem item = get(itemId);
		UserExtension user = users.getExtended(name);
		
		if(moderator || item.getOwner().equals(user)) {
			item.getOwner().getBuyAndSellItems().remove(item);
			sessions.getCurrentSession().delete(item);
		} else {
			throw new IllegalAccessException(name + " does not own this item and may not delete it");
		}
	}
}
