package org.activityinfo.server.endpoint.refine;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.Lists;



public class Query {
	private String query;
	private int limit;
	private String type;
	private List<PropertyValue> properties = Lists.newArrayList();
	
	@JsonProperty("type_strict")
	private String strict;
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	
	public boolean hasLimit() {
		return limit > 0;
	}
	
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public List<PropertyValue> getProperties() {
		return properties;
	}
	public void setProperties(List<PropertyValue> properties) {
		this.properties = properties;
	}
	public String getStrict() {
		return strict;
	}
	public void setStrict(String strict) {
		this.strict = strict;
	}
	
}
