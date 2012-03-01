/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report;

import java.io.IOException;
import java.io.OutputStream;

import org.sigmah.server.report.renderer.html.ImageStorage;
import org.sigmah.server.report.renderer.html.ImageStorageProvider;

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
