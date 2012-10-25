package org.activityinfo.server.geo;

import com.google.inject.servlet.ServletModule;

public class GeometryModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(AdminGeometryProvider.class).to(CloudStorageGeometryProvider.class);
		serve(GeometryServlet.END_POINT).with(GeometryServlet.class);
	}

}
