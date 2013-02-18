package org.activityinfo.server.endpoint.refine;

public class SchemaDescription {
	private String id;
	private String name;
	
	public SchemaDescription(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
