/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.output;

import java.io.IOException;


public interface ImageStorageProvider {

    /**
     * Creates a web-accessible temporary file
     * @param mimeType TODO
     * @param suffix The suffix to be appended to the temporary file (should include necessary ".")
     *
     * @return
     * @throws IOException
     */
	ImageStorage getImageUrl(String mimeType, String suffix) throws IOException;
	
}
