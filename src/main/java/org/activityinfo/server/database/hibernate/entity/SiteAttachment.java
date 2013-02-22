package org.activityinfo.server.database.hibernate.entity;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "siteattachment")
@NamedQueries({
    @NamedQuery(name = "findSiteAttachments", query = "select s from SiteAttachment s where s.blobId = :blobId") })
public class SiteAttachment implements java.io.Serializable {


	private static final long serialVersionUID = -619220161104158193L;
	
	private int siteId;
    private String blobId;
    private String fileName;
    private int uploadedBy;
    private double blobSize;
    private String contentType;
    
   
    public SiteAttachment() {
    }
    
    @Id
    @Column(name = "blobid", nullable = false, length = 255)
	public String getBlobId() {
		return blobId;
	}

	public void setBlobId(String blobId) {
		this.blobId = blobId;
	}

	@Column(name = "siteid", nullable = false, length = 11)
	public int getSiteId() {
		return siteId;
	}


	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

    @Column(name = "filename", nullable = false, length = 255)
	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

    @Column(name = "uploadedby", nullable = false, length = 11)
	public int getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(int uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

    @Column(name = "blobsize", nullable = false)
	public double getBlobSize() {
		return blobSize;
	}

	public void setBlobSize(double blobSize) {
		this.blobSize = blobSize;
	}

    @Column(name = "contenttype", nullable = false, length = 255)
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
    

}
