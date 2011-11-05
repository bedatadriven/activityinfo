package org.sigmah.shared.command;


import org.sigmah.shared.command.result.UploadUrlResult;

public class GetUploadUrl implements Command<UploadUrlResult> {

	private String url;
	private String blobid;

	public GetUploadUrl() {

	}

	public GetUploadUrl(String blobid){
		this.blobid = blobid;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBlobid() {
		return blobid;
	}

	public void setBlobid(String blobid) {
		this.blobid = blobid;
	}
	
}
