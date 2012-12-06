package com.baldwin.indgte.webapp.dto;

public class Preview {

	public static enum PreviewType {
		user,
		business
	}
	
	public static class Cover {
		private String source;
		private int offsetY = 0;
		
		@Override
		public String toString() {
			return source;
		}
		
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		public int getOffsetY() {
			return offsetY;
		}
		public void setOffsetY(int offsetY) {
			this.offsetY = offsetY;
		}
	}
	
	private PreviewType type;
	private String image;
	private Cover cover;
	private String title;
	private String description;
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Cover getCover() {
		return cover;
	}
	public void setCover(Cover cover) {
		this.cover = cover;
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
	public PreviewType getType() {
		return type;
	}
	public void setType(PreviewType type) {
		this.type = type;
	}
	
}
