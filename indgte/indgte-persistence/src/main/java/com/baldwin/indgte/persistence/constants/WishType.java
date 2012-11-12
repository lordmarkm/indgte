package com.baldwin.indgte.persistence.constants;

import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.Product;

/**
 * TODO refactor to Attachable
 * @author mbmartinez
 *
 */
public enum WishType {
	/**
	 * Wish for a {@link Product}
	 */
	product,
	
	/**
	 * Wish for a {@link BuyAndSellItem}
	 */
	buyandsell,
	
	/**
	 * No associated Indumaguete entity
	 */
	noentity
}
