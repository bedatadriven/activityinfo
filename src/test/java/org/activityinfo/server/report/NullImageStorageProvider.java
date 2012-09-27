/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report;

import java.io.IOException;
import java.io.OutputStream;

import org.activityinfo.server.report.output.ImageStorage;
import org.activityinfo.server.report.output.ImageStorageProvider;

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
