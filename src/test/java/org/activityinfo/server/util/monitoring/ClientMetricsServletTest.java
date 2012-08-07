package org.activityinfo.server.util.monitoring;

import org.junit.Test;

import com.google.gson.Gson;

public class ClientMetricsServletTest {

	public static final String PAYLOAD = " startup.selectingPermutation:1344330916836:1 startup.end:1344330916837:0 " +
				  "startup.end:1344330916837:84 startup.moduleEvalStart:1344330917146:379 " +
				  "startup.moduleEvalEnd:1344330917525:2 startup.onModuleLoadStart:1344330917527:23020 " +
				  "startup.onModuleLoadStart:1344330940547:43 startup.end:1344330940590:7294 ";

	@Test
	public void parse() {
		ClientMetricsServlet servlet = new ClientMetricsServlet(null);
		SeriesCollection collection = servlet.parse(PAYLOAD);
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(collection));
	}
	
}
