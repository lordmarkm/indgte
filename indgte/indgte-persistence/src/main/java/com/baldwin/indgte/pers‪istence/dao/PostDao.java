package com.baldwin.indgte.pers‪istence.dao;

import java.util.Set;

import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.model.Post;

public interface PostDao {

	/**
	 * Will only ever find posts by businesses
	 * @deprecated use getById instead
	 */
	@Deprecated
	public Set<Post> getByDomain(String domain, int start, int howmany);

	public Set<Post> getById(long posterId, PostType type, int start, int howmany);

	public Post newPost(long posterId, PostType type, String title, String text);

}
