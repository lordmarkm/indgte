package com.baldwin.indgte.pers‪istence.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.model.Post;

@Repository
@Transactional
public class PostDaoImpl implements PostDao {

	static Logger log = LoggerFactory.getLogger(PostDaoImpl.class);
	
	@Autowired
	private SessionFactory sessions;
	
	/**
	 * @deprecated use getById instead
	 */
	@Deprecated
	public Set<Post> getByDomain(String domain, int start, int howmany) {
		Session session = sessions.getCurrentSession();
		
		long posterId = (Long) session.createQuery("select id from BusinessProfile where domain = :domain")
						.setString("domain", domain)
						.uniqueResult();
		
		log.debug("Poster id: {}", posterId);
		
		@SuppressWarnings("unchecked")
		List<Post> posts = session.createCriteria(Post.class)
				.add(Restrictions.eq("posterId", posterId))
				.add(Restrictions.eq("type", PostType.business))
				.list();
		
		return new HashSet<Post>(posts);
	}

	@Override
	public Set<Post> getById(long posterId, PostType type, int start, int howmany) {
		@SuppressWarnings("unchecked")
		List<Post> posts = sessions.getCurrentSession().createCriteria(Post.class)
				.add(Restrictions.eq("posterId", posterId))
				.add(Restrictions.eq("type", type))
				.list();
		
		return new LinkedHashSet<Post>(posts);
	}

	@Override
	public Post newPost(long posterId, PostType type, String title, String text) {
		Post post = new Post();
		post.setPosterId(posterId);
		post.setType(type);
		post.setTitle(title);
		post.setText(text);
		post.setPostTime(new Date());
		
		sessions.getCurrentSession().save(post);
		return post;
	}
}
