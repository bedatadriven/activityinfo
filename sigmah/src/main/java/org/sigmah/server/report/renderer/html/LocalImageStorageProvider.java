/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.html;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class LocalImageStorageProvider implements ImageStorageProvider {

	private String folder;
	
	public LocalImageStorageProvider(String folder) {
		this.folder = folder.replace('\\', '/');
	}
	
	@Override
	public ImageStorage getImageUrl(String suffix) throws IOException {
		String path = folder + "/img" + Long.toString((new Date()).getTime()) + suffix;
		OutputStream stream = new FileOutputStream(path);
		
		return new ImageStorage("file://" + path, stream);
	}

}
