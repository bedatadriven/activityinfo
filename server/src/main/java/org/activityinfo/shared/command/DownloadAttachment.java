package org.activityinfo.shared.command;


import org.activityinfo.shared.command.result.UrlResult;

public class DownloadAttachment implements Command<UrlResult> {

	private String url;
	private String blobId;

	public DownloadAttachment() {

	}

	public DownloadAttachment(String blobId){
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
