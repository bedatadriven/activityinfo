package org.activityinfo.server.domain;

public enum AggregationMethod {

	Sum(0),
	Average(1),
	SiteCount(2);
	
	private int code;
	
	private AggregationMethod(int code) {
		this.code = code;
	}
	
	public final Integer code() {
		return code;
	}
	
}
