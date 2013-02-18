/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.output;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.activityinfo.server.authentication.SecureTokenGenerator;

import com.google.inject.Provider;

public class ServletStorageProvider implements StorageProvider {

    private String urlBase;
    private String tempPath;
	private Provider<HttpServletRequest> provider;

	public ServletStorageProvider(String urlBase, String tempPath, Provider<HttpServletRequest> httpRequestProvider) {
        this.urlBase = urlBase;
        this.tempPath = tempPath;
        this.provider = httpRequestProvider;
    }

    @Override
    public TempStorage allocateTemporaryFile(String mimeType, String suffix) throws IOException {
        String filename = SecureTokenGenerator.generate() + suffix;
        HttpServletRequest req = provider.get();
        
        StringBuilder sb = new StringBuilder()
	        .append("http://")
	        .append(req.getServerName())
	        .append(":")
	        .append(req.getServerPort())
	        .append("/")
	        .append(urlBase)
	        .append(filename);

        String path = tempPath + "/" + filename;

        FileOutputStream os = new FileOutputStream(path);

        return new TempStorage(sb.toString(), os);
    }
}
