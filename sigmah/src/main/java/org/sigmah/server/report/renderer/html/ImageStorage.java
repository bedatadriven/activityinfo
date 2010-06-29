package org.sigmah.server.report.renderer.html;

import java.io.OutputStream;

public class ImageStorage {

	private String url;
	private OutputStream outputStream;
	
	public ImageStorage(String url, OutputStream stream) {
		this.url = url;
		this.outputStream = stream;
	}
	
	public String getUrl() {
		return url;
	}
	public OutputStream getOutputStream() {
		return outputStream;
	}
	
	
	
}
