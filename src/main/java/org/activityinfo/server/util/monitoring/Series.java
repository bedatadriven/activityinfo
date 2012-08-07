package org.activityinfo.server.util.monitoring;

import java.util.List;

import com.google.common.collect.Lists;

public class Series {
	
	private String metricName;
	private List<double[]> points = Lists.newArrayList();
	private String type;
	private String host;
	private String device;
	private String[] tags;
	
	public String getMetricName() {
		return metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public void addPoint(double timestamp, double value) {
		points.add(new double[] { timestamp, value } );
	}
}
