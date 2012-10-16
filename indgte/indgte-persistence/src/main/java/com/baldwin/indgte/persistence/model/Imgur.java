package com.baldwin.indgte.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This is the imgur upload response:
 * {"upload":
 * 		{"image":
 * 			{"name":null,
 * 			 "title":null,
 * 			 "caption":null,
 * 			 "hash":"Ax4S2",
 * 			 "deletehash":"x8ezFIPFkZvgMVM",
 * 			 "datetime":"2012-10-09 09:16:40",
 * 			 "type":"image\/jpeg",
 * 			 "animated":"false",
 * 			 "width":688,
 * 			 "height":725,
 * 			 "size":77257,
 * 			 "views":0,
 * 			 "bandwidth":0},
 * 		 "links":
 * 			{"original":"http:\/\/i.imgur.com\/Ax4S2.jpg",
 * 			 "imgur_page":"http:\/\/imgur.com\/Ax4S2",
 * 			 "delete_page":"http:\/\/imgur.com\/delete\/x8ezFIPFkZvgMVM",
 * 			 "small_square":"http:\/\/i.imgur.com\/Ax4S2s.jpg",
 * 			 "large_thumbnail":"http:\/\/i.imgur.com\/Ax4S2l.jpg"}
 * 		}
 * 	}
 * @author mbmartinez
 */

@Entity
@Table(name="imgurs")
public class Imgur {
	public static String urlOriginal = "http://i.imgur.com/";
	public static String urlSmallSquare = "http://i.imgur.com/";
	public static String urlLargeThumbnail = "http://i.imgur.com/";
	public static String urlImgurPage = "http://imgur.com/";
	public static String urlDelete = "http://imgur.com/delete/";

	
	@Column(name="imageId")
	@Id 
	@GeneratedValue
	private long imageId;
	
	@Column(nullable=false)
	private String hash;
	
	@Column
	private String deletehash;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date uploaded;

	@Column(length=25)
	private String title;
	
	@Column(length=140)
	private String description;

	@Column
	private boolean hidden = false;
	
	@Override
	public String toString() {
		return hash;
	}
	
	@PrePersist
	protected void onCreate() {
		uploaded = new Date();
	}
	
	public String getOriginal() {
		return urlOriginal + hash + ".jpg";
	}
	
	public String getSmallSquare() {
		return urlSmallSquare + hash + "s.jpg";
	}
	
	public String getLargeThumbnail() {
		return urlLargeThumbnail + hash + "l.jpg";
	}
	
	public String getImgurPage() {
		return urlImgurPage + hash;
	}
	
	public String getDelete() {
		return urlDelete + hash;
	}
	
	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getDeletehash() {
		return deletehash;
	}

	public void setDeletehash(String deletehash) {
		this.deletehash = deletehash;
	}

	public Date getUploaded() {
		return uploaded;
	}

	public void setUploaded(Date uploaded) {
		this.uploaded = uploaded;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
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
}
