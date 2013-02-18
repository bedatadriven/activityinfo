/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.output;

import java.io.OutputStream;

public class TempStorage {

	private String url;
	private OutputStream outputStream;
	
	public TempStorage(String url, OutputStream stream) {
		this.url = url;
		this.outputStream = stream;
	}
	
	/**
	 * 
	 * @return the publicly-accessible URL for this temporary resource
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * 
	 * @return the output stream to which the contents of the file should be written.
	 * The stream MUST be closed by the caller.
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}
}
