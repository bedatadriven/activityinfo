package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;

public class CreateSiteAttachment implements Command<VoidResult> {


	private static final long serialVersionUID = 1008206027004197062L;
	
	private int siteId;
    private String blobId;
    private String fileName;
    private String uploadedBy;
	
    public CreateSiteAttachment(){
    	
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
	public void setBlobId(String blodId) {
		this.blobId = blodId;
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
