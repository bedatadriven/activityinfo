package org.sigmah.shared.command;


import org.sigmah.shared.command.result.UploadUrlResult;

public class GetUploadUrl implements Command<UploadUrlResult> {

	private String url;

	public GetUploadUrl() {

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
