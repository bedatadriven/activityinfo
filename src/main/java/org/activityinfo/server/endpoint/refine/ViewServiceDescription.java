package org.activityinfo.server.endpoint.refine;


public class ViewServiceDescription {
	private String url;

	public ViewServiceDescription(String uri) {
		super();
		this.url = uri.toString();
	}

	public String getUrl() {
		return url;
	}
	
}
