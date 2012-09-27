/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.output;

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
