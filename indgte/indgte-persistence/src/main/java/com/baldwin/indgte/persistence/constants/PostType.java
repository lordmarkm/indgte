package com.baldwin.indgte.persistence.constants;

/**
 * the Feed needs to know what kind of post a post is in order to render it correctly
 * @author mbmartinez
 */
public enum PostType {
	/**
	 * Posts by a user, similar to Facebook feed status posts - username will 
	 * appear and link to that user's page I suppose
	 * <b>Post.posterId = User.id</b>
	 */
	user, 
	
	/**
	 * Posts by a business - will link to business page
	 * <b>Post.posterId = BusinessProfile.id</b>
	 */
	business,
	
	/**
	 * Posts promoting a new product - will link to product page
	 * <b>Post.posterId = Product.id</b>
	 */
	product,
	
	/**
	 * For future use
	 */
	article
}