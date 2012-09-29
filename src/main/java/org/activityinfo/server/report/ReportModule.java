/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.activityinfo.client.page.report.json.ReportJsonFactory;
import org.activityinfo.client.page.report.json.ReportSerializer;
import org.activityinfo.server.endpoint.gwtrpc.MapIconServlet;
import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.server.report.output.AppEngineStorageProvider;
import org.activityinfo.server.report.output.AppEngineStorageServlet;
import org.activityinfo.server.report.output.ImageStorageProvider;
import org.activityinfo.server.report.output.ServletImageStorageProvider;

import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

public class ReportModule extends ServletModule {

	public ReportModule() {
		super();
	}

	@Override
    protected void configureServlets() {
		
		bind(ReportSerializer.class).to(ReportJsonFactory.class);
		
		bind(String.class)
	        .annotatedWith(MapIconPath.class)
	        .toProvider(MapIconPathProvider.class)
	        .in(Singleton.class);
		
		serve("/icon*").with(MapIconServlet.class);
		serve("/generated/*").with(AppEngineStorageServlet.class);
    }
    
    @Provides
    public ImageStorageProvider provideImageStorageProvider(ServletContext context, Provider<HttpServletRequest> requestProvider) {
    
    	if(!Strings.isNullOrEmpty(System.getProperty("com.google.appengine.runtime.environment"))) {
    		return new AppEngineStorageProvider();
    	}
    	
    	File tempPath = new File(context.getRealPath("/temp/"));
        if(!tempPath.exists()) {
            tempPath.mkdir();
        }
        ServletImageStorageProvider isp = new ServletImageStorageProvider("temp/",
                tempPath.getAbsolutePath(), requestProvider);
        return isp;
    }
}
