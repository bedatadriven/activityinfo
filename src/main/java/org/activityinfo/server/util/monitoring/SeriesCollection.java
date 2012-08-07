package org.activityinfo.server.util.monitoring;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

public class SeriesCollection {

	private List<Series> series;

	public SeriesCollection(Collection<Series> series) {
		this.series = Lists.newArrayList(series);
	}

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}
}
