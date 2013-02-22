package org.activityinfo.server.endpoint.refine;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
