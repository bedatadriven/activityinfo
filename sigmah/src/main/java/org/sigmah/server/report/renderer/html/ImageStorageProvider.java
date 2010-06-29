package org.sigmah.server.report.renderer.html;

import java.io.IOException;

public interface ImageStorageProvider {

    /**
     * Creates a web-accessible temporary file
     *
     * @param suffix The suffix to be appended to the temporary file (should include necessary ".")
     * @return
     * @throws IOException
     */
	ImageStorage getImageUrl(String suffix) throws IOException;
	
}
