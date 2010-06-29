/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report;

import org.sigmah.server.auth.SecureTokenGenerator;
import org.sigmah.server.report.renderer.html.ImageStorage;
import org.sigmah.server.report.renderer.html.ImageStorageProvider;

import java.io.FileOutputStream;
import java.io.IOException;

public class ServletImageStorageProvider implements ImageStorageProvider {

    private String urlBase;
    private String tempPath;

    public ServletImageStorageProvider(String urlBase, String tempPath) {
        this.urlBase = urlBase;
        this.tempPath = tempPath + "/";
    }

    @Override
    public ImageStorage getImageUrl(String suffix) throws IOException {

        String filename = SecureTokenGenerator.generate() + suffix;

        String path = tempPath + filename;
        String url = urlBase + filename;

        FileOutputStream os = new FileOutputStream(path);

        return new ImageStorage(url, os);
    }
}
