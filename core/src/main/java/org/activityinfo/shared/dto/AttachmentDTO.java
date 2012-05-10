package org.activityinfo.shared.dto;

import java.util.Date;

public class AttachmentDTO implements DTO {
	private String name;
	private int id;
	private Date createdDate;
	private int sizeInKb;
	private String extension;
	private int siteId;
	
	public AttachmentDTO() {
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public int getSizeInKb() {
		return sizeInKb;
	}
	public void setSizeInKb(int sizeInKb) {
		this.sizeInKb = sizeInKb;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
}
