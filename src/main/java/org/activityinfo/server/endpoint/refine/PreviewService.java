package org.activityinfo.server.endpoint.refine;

import java.net.URI;

/**
 * Describes the endpoint of the preview service which provides
 * an html iframe for describing entities
 *
 */
public class PreviewService {

	private String url;
	private int width;
	private int height;


	public String getUrl() {
		return url;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
}
