package org.activityinfo.server.report;

import org.activityinfo.login.shared.AuthenticatedUser;
import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.server.report.output.StorageProvider;
import org.activityinfo.server.report.output.LocalStorageProvider;

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
    public StorageProvider provideImageStorageProvider() {
        return new LocalStorageProvider("target");
    }
    
    @Provides
    public AuthenticatedUser provideUser() {
    	return new AuthenticatedUser("XYZ", 1, "alex@bedatadriven.com");
    }
}
