package org.activityinfo.server.util.monitoring;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ClientMetricsServlet extends HttpServlet {

	private static Logger LOGGER = Logger.getLogger(ClientMetricsServlet.class);
	
	private final DatadogClient datadog;
	
	@Inject
	public ClientMetricsServlet(DatadogClient datadog) {
		super();
		this.datadog = datadog;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String body = new String(ByteStreams.toByteArray(req.getInputStream()));

		LOGGER.info("UA: " + req.getHeader("User-agent") + "\n" +
					"Country: " + req.getHeader("CF-IPCountry") + "\n" + 
					body.replace(' ', '\n'));
		
//		SeriesCollection series = parse(body);
//		
//		datadog.postSeries(series);
	}

	@VisibleForTesting
	public SeriesCollection parse(String body) {
		String metrics[] = body.split(" ");
		
		Map<String, Series> seriesMap = Maps.newHashMap();
		
		for(String metric : metrics) {
			String[] fields = metric.split(":");
			if(fields.length >= 3) {
				String metricName = fields[0];
				Series series = seriesMap.get(metricName);
				if(series == null) {
					series = new Series();
					series.setMetricName("client." + metricName);
					series.setType("histogram");
					seriesMap.put(metricName, series);
				}
				series.addPoint(Double.parseDouble(fields[1]), Double.parseDouble(fields[2]));
			}
		}
		
		return new SeriesCollection(seriesMap.values());
	}
	
	
}
