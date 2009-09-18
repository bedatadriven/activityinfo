package org.activityinfo.shared.command.result;

import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.Schema;

public class SchemaResult implements CommandResult {

	public SchemaResult(Schema schema) {
		super();
		this.schema = schema;
	}

	private Schema schema;

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	
	
}
