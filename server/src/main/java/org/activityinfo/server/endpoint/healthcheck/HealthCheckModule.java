package org.activityinfo.server.endpoint.healthcheck;

import com.google.inject.servlet.ServletModule;

public class HealthCheckModule extends ServletModule {

	@Override
	protected void configureServlets() {
		serve("/healthCheck").with(HealthCheckServlet.class);
	}
}
