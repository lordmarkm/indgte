package com.baldwin.indgte.persistence.dto;

import com.baldwin.indgte.persistence.model.Imgur;


public class Summary {
	public static final String urlBusiness = "/";
	public static final String urlCategory = "/b/categories/";
	public static final String urlBas = "/t/";
	public static final String urlProduct = "/b/products/";
	
	public enum SummaryType {
		none,
		
		imgur,
		video,
		link,
		
		/* types below fall under 'entity' in the front end */
		user,
		business,
		product, 
		category, 
		buyandsellitem
	}
	
	private Long id;
	private SummaryType type;
	private String title;
	private String description;
	private String thumbnailHash;
	private String rank;
	private Imgur imgur;
	
	/**
	 * User: username
	 * BusinessProfile : domain
	 * Category : id
	 * Product : id
	 */
	private String identifier;

	public Summary(SummaryType type, Long id, String title, String description,	String identifier, String thumbnailHash) {
		super();
		this.type = type;
		this.id = id;
		this.title = title;
		this.description = description;
		this.identifier = identifier;
		this.thumbnailHash = thumbnailHash;
	}

	public Summary() {
		// TODO Auto-generated constructor stub
	}
	
	public SummaryType getType() {
		return type;
	}

	public void setType(SummaryType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getThumbnailHash() {
		return thumbnailHash;
	}

	public void setThumbnailHash(String thumbnailHash) {
		this.thumbnailHash = thumbnailHash;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public Imgur getImgur() {
		return imgur;
	}

	public void setImgur(Imgur imgur) {
		this.imgur = imgur;
	}

	public String getUrl() {
		switch(type) {
		case business:
			return urlBusiness + identifier;
		case category:
			return urlCategory + identifier;
		case product:
			return urlProduct + identifier;
		case buyandsellitem:
			return urlBas + identifier;
		default: 
			return "";
		}
	}

}
