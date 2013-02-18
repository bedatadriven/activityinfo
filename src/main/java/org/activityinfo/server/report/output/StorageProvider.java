/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.output;

import java.io.IOException;


public interface StorageProvider {

    /**
     * Creates a web-accessible temporary file
     * @param mimeType the mime type of the file
     * @param filename The name of the user-facing file 
     *
     * @return
     * @throws IOException
     */
	TempStorage allocateTemporaryFile(String mimeType, String filename) throws IOException;
	
}
