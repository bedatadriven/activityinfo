package org.sigmah.server.report;

import org.sigmah.server.report.generator.MapIconPath;
import org.sigmah.server.report.renderer.html.ImageStorageProvider;
import org.sigmah.server.report.renderer.html.LocalImageStorageProvider;

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
}
