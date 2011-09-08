/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report;

import org.sigmah.server.auth.SecureTokenGenerator;
import org.sigmah.server.report.renderer.html.ImageStorage;
import org.sigmah.server.report.renderer.html.ImageStorageProvider;

import com.google.inject.Provider;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ServletImageStorageProvider implements ImageStorageProvider {

    private String urlBase;
    private String tempPath;
	private Provider<HttpServletRequest> provider;

	public ServletImageStorageProvider(String urlBase, String tempPath, Provider<HttpServletRequest> httpRequestProvider) {
        this.urlBase = urlBase;
        this.tempPath = tempPath;
        this.provider = httpRequestProvider;
    }

    @Override
    public ImageStorage getImageUrl(String suffix) throws IOException {
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

        return new ImageStorage(sb.toString(), os);
    }
}
