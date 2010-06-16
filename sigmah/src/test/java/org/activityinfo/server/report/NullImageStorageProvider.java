package org.activityinfo.server.report;

import org.activityinfo.server.report.renderer.html.ImageStorage;
import org.activityinfo.server.report.renderer.html.ImageStorageProvider;

import java.io.IOException;
import java.io.OutputStream;

public class NullImageStorageProvider implements ImageStorageProvider {

	@Override
	public ImageStorage getImageUrl(String suffix) throws IOException {
		
		return new ImageStorage("http://", new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				// NOOOP!
			}
		});
	}
}
