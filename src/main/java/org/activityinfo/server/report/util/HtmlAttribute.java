package org.activityinfo.server.report.util;

public class HtmlAttribute {

	private String name;
	private StringBuilder value = new StringBuilder();
	
	public HtmlAttribute(String name, String value) {
		this.name = name;
		this.value.append(value);
	}

	public HtmlAttribute(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value.toString();
	}

	protected void clear() { 
		if(value.length()!=0) {
			value = new StringBuilder();
		}
	}
	
	public void setValue(String value) {
		clear();
		this.value.append(value);
	}
	
	public void setValue(int value) {
		clear();
		this.value.append(value);
	}
	
	public HtmlAttribute append(String value) {
		this.value.append(value);
		return this;
	}
	
	public HtmlAttribute append(String value, char delimeter) {
		if(this.value.length() != 0) {
			this.value.append(delimeter);
		}
		this.value.append(value);
		return this;
	}
}
