package org.activityinfo.server.endpoint.refine;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonProperty;

public class PropertyValue {
	@JsonProperty("p")
	private String propertyName;
	
	@JsonProperty("pid")
	private String propertyId;
	
	@JsonProperty("v")
	private JsonNode value;

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public JsonNode getValue() {
		return value;
	}

	public void setValue(JsonNode value) {
		this.value = value;
	}
	
	public String stringValue() {
		return value.getTextValue();
	}
}
