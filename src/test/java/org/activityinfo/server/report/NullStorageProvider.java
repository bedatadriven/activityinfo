/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report;

import java.io.IOException;
import java.io.OutputStream;

import org.activityinfo.server.report.output.TempStorage;
import org.activityinfo.server.report.output.StorageProvider;

public class NullStorageProvider implements StorageProvider {

	@Override
	public TempStorage allocateTemporaryFile(String mimeType, String suffix) throws IOException {
		
		return new TempStorage("http://", new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				// NOOOP!
			}
		});
	}
}
