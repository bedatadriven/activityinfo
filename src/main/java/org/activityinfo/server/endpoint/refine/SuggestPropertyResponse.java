package org.activityinfo.server.endpoint.refine;

import java.util.List;

import com.google.common.collect.Lists;


public class SuggestPropertyResponse {
	private String prefix;
	private List<PropertyDescription> result = Lists.newArrayList();
	
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public List<PropertyDescription> getResult() {
		return result;
	}

	public void addResult(PropertyDescription property) {
		this.result.add(property);
	}

	
}
