package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class SiteAttachmentDTO extends BaseModelData implements DTO {


	private static final long serialVersionUID = 4834762767256920716L;

	private int siteId;
    private String blobId;
    private String fileName;
    private String uploadedBy;
	
	public SiteAttachmentDTO(){
		
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getBlobId() {
		return blobId;
	}

	public void setBlobId(String blobId) {
		this.blobId = blobId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	
	
	
	
}
