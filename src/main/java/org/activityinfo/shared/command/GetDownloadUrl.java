package org.activityinfo.shared.command;


import org.activityinfo.shared.command.result.S3UrlResult;

public class GetDownloadUrl implements Command<S3UrlResult> {

	private String url;
	private String blobId;

	public GetDownloadUrl() {

	}

	public GetDownloadUrl(String blobId){
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
