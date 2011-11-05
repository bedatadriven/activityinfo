package org.sigmah.client.page.entry;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class AttachmentModel extends BaseModelData {

	private int siteId;
    private String blobId;
    private String fileName;
    private String uploadedBy;
    
    public AttachmentModel (){
    	
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
