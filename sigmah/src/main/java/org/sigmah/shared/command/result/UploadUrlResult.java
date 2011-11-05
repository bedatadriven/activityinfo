package org.sigmah.shared.command.result;


public class UploadUrlResult implements CommandResult {

	private static final long serialVersionUID = -1012847054313132806L;

	private String url;

	public UploadUrlResult() {

	}

	public UploadUrlResult(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
