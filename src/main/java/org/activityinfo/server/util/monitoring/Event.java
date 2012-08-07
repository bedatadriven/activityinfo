package org.activityinfo.server.util.monitoring;

public class Event {

	public static final String ERROR = "error";
	public static final String WARNING = "warning";
	public static final String INFO = "info";
	public static final String SUCCESS = "success";

	String title;
	String text;
	private Long dateHappened;
	private String[] tags;
	String alertType;
	private String sourceTypeName;
	private String aggregationKey;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public String getAggregationKey() {
		return aggregationKey;
	}
	public void setAggregationKey(String aggregationKey) {
		this.aggregationKey = aggregationKey;
	}

}