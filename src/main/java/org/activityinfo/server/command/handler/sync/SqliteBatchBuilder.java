package org.activityinfo.server.command.handler.sync;

import java.io.IOException;
import java.io.StringWriter;

import javax.persistence.EntityManager;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gson.stream.JsonWriter;

public class SqliteBatchBuilder {

	private StringWriter stringWriter;
	private JsonWriter jsonWriter;
	
	public SqliteBatchBuilder() throws IOException {
		super();
		this.stringWriter = new StringWriter();
		this.jsonWriter = new JsonWriter(stringWriter);
		this.jsonWriter.beginArray();
	}

	public void addStatement(String sqlState) throws IOException {
		jsonWriter.beginObject();
		jsonWriter.name("statement");
		jsonWriter.value(sqlState);
		jsonWriter.endObject();
	}
	
	public SqliteInsertBuilder insert() {
		return new SqliteInsertBuilder(this);
	}
	
	public String build() throws IOException {
		jsonWriter.endArray();
		jsonWriter.flush();
		return stringWriter.toString();
	}

	public SqliteDeleteBuilder delete() {
		return new SqliteDeleteBuilder(this);
	}
}
