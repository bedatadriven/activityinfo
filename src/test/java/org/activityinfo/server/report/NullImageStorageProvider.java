package org.activityinfo.server.report;

import java.io.IOException;
import java.io.OutputStream;

import org.activityinfo.server.report.renderer.html.ImageStorage;
import org.activityinfo.server.report.renderer.html.ImageStorageProvider;

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
