package com.baldwin.indgte.persistence.dto;

public class SearchResult {
	public enum ResultType {
		business,
		product
	}
	
	private ResultType type;
	private String title;
	private String description;
	private String thumbnailHash;
	
	public SearchResult(ResultType type, String title, String description,	String identifier, String thumbnailHash) {
		super();
		this.type = type;
		this.title = title;
		this.description = description;
		this.identifier = identifier;
		this.thumbnailHash = thumbnailHash;
	}

	public SearchResult() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * User: username
	 * BusinessProfile : domain
	 * Category : id
	 * Product : id
	 */
	private String identifier;

	public ResultType getType() {
		return type;
	}

	public void setType(ResultType type) {
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
}
