package org.activityinfo.server.util.monitoring;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.junit.Ignore;
import org.junit.Test;

public class DatadogTest {

	@Test
	@Ignore("learning test")
	public void test() throws IOException {
		
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File(System.getProperty("user.home"), "activityinfo.properties")));
		
		DeploymentConfiguration config = new DeploymentConfiguration(properties);
		
		ClientMetricsServlet servlet = new ClientMetricsServlet(null);
		SeriesCollection series = servlet.parse(ClientMetricsServletTest.PAYLOAD);
		
		DatadogClient datadog = new DatadogClient(null, config);
		datadog.postSeries(series);
	}
}
