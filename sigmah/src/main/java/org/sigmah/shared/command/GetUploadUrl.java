package org.sigmah.shared.command;


import org.sigmah.shared.command.result.UploadUrlResult;

public class GetUploadUrl implements Command<UploadUrlResult> {

	private String url;
	private String blobId;

	public GetUploadUrl() {

	}

	public GetUploadUrl(String blobId){
		this.blobId = blobId;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBlobId() {
		return blobId;
	}

	public void setBlobId(String blobId) {
		this.blobId = blobId;
	}
	
}
