package org.activityinfo.server.endpoint.refine;

import org.codehaus.jackson.annotate.JsonProperty;

public class Property {

	@JsonProperty("p")
	private String propertyName;
	
	@JsonProperty("pid")
	private String propertyId;

}
