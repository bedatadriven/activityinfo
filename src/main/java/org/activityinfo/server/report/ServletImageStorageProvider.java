package org.activityinfo.server.report;

import java.io.FileOutputStream;
import java.io.IOException;


import org.activityinfo.server.domain.util.EntropicToken;
import org.activityinfo.server.report.renderer.html.ImageStorage;
import org.activityinfo.server.report.renderer.html.ImageStorageProvider;

public class ServletImageStorageProvider implements ImageStorageProvider {

	private String urlBase;
	private String tempPath;
	
	public ServletImageStorageProvider(String urlBase, String tempPath) {
		this.urlBase = urlBase;
		this.tempPath = tempPath + "/";
	}
	
	@Override
	public ImageStorage getImageUrl(String suffix) throws IOException {
	
		String filename = EntropicToken.generate() + suffix;
		
		String path = tempPath + filename;
		String url = urlBase + filename;
 		
		FileOutputStream os = new FileOutputStream(path);
		
		return new ImageStorage(url, os);
	}
}
