package com.baldwin.indgte.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
	public static String urlImgurPage = "http://imgur.com/";
	public static String urlDelete = "http://imgur.com/delete/";
	public static String urlSmallSquare = "http://i.imgur.com/";
	public static String urlLargeThumbnail = "http://i.imgur.com/";
	
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
}
