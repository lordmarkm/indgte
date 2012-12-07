package com.baldwin.indgte.pers‪istence.dao;

import com.baldwin.indgte.persistence.dto.Fame;

/**
 * <p>A Dao for assigning and computing User "Fame"
 * <ol type="I">
 *  <li>Businesses and Products
 *   <ol>
 *    <li>Somebody comments on your Business, category, or product (entity) +1
 *    <li>Entity is liked +10
 *    <li>Entity is sent +10
 *    <li>Business, Category, or Product is featured by mod +500
 *   </ol>
 *  <li>Posts (has absorbed Article)
 * 	 <ol>
 * 	  <li>Somebody comments +1
 * 	  <li>Post is liked +10
 *    <li>Post is 'sent' +10
 * 	  <li>Post is featured by mod +500
 * 	 </ol>
 *  <li>Reviews
 *   <ol>
 *    <li>Somebody comments on a review you wrote +1
 *    <li>Somebody agrees with a review you wrote +10
 *    <li>Somebody reviews you or an entity you own favorably (score 3 or more) +50
 *   </ol>
 *  <li>Friendships
 *   <ol>
 *    <li>User subscribes to your page (BusinessProfile) +10
 *    <li>User subscribes to your personal account +10
 *   </ol>
 */

public interface FameDao {
	
	Fame computeFame(String username);
	
}
