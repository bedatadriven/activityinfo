package org.activityinfo.server.util.monitoring;

import com.google.inject.servlet.ServletModule;

public class MonitoringModule extends ServletModule {

	@Override
	protected void configureServlets() {
		serve("/ActivityInfo/clientMetrics").with(ClientMetricsServlet.class);
	}
}
