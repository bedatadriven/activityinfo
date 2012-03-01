/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.sigmah.server.report.generator.MapIconPath;
import org.sigmah.server.report.renderer.html.ImageStorageProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ReportModule extends AbstractModule {

	public ReportModule() {
		super();
	}

	@Override
    protected void configure() {
        bind(String.class)
	        .annotatedWith(MapIconPath.class)
	        .toProvider(MapIconPathProvider.class)
	        .in(Singleton.class);
    }
    
    @Provides
    public ImageStorageProvider provideImageStorageProvider(ServletContext context, Provider<HttpServletRequest> requestProvider) {
    	File tempPath = new File(context.getRealPath("/temp/"));
        if(!tempPath.exists()) {
            tempPath.mkdir();
        }
        ServletImageStorageProvider isp = new ServletImageStorageProvider("temp/",
                tempPath.getAbsolutePath(), requestProvider);
        return isp;
    }

}
