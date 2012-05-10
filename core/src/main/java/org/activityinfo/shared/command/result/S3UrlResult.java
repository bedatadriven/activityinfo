package org.activityinfo.shared.command.result;


public class S3UrlResult implements CommandResult {

	private static final long serialVersionUID = -1012847054313132806L;

	private String url;

	public S3UrlResult() {

	}

	public S3UrlResult(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
