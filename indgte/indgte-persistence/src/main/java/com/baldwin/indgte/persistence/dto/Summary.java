package com.baldwin.indgte.persistence.dto;


public class Summary {
	public enum SummaryType {
		none,
		
		imgur,
		video,
		link,
		
		/* types below fall under 'entity' in the front end */
		user,
		business,
		product, 
		category
	}
	
	private Long id;
	private SummaryType type;
	private String title;
	private String description;
	private String thumbnailHash;
	
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
}