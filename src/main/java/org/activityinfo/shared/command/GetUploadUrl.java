package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.UrlResult;

public class GetUploadUrl implements Command<UrlResult> {
	
	private String blobId;	

	public GetUploadUrl() {
		
	}
	
	public GetUploadUrl(String blobId) {
		this.blobId = blobId;
	}

	public String getBlobId() {
		return blobId;
	}

	public void setBlobId(String blobId) {
		this.blobId = blobId;
	}
	
	

}
