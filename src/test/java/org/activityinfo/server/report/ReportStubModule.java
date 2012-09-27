package org.activityinfo.server.report;

import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.server.report.output.ImageStorageProvider;
import org.activityinfo.server.report.output.LocalImageStorageProvider;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ReportStubModule extends AbstractModule {

	@Override
	protected void configure() {
	        bind(String.class)
		        .annotatedWith(MapIconPath.class)
		        .toInstance("");
	}
    
    @Provides
    public ImageStorageProvider provideImageStorageProvider() {
        return new LocalImageStorageProvider("target");
    }
    
    @Provides
    public AuthenticatedUser provideUser() {
    	return new AuthenticatedUser("XYZ", 1, "alex@bedatadriven.com");
    }
}
