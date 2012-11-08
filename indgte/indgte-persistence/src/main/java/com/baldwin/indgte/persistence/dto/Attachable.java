package com.baldwin.indgte.persistence.dto;

import com.baldwin.indgte.persistence.constants.AttachmentType;
import com.baldwin.indgte.persistence.model.Imgur;

public interface Attachable extends Summarizable {
	public long getId();
	public AttachmentType getAttachmentType();
	public String getName();
	public String getDescription();
	public Imgur getImgur();
}
