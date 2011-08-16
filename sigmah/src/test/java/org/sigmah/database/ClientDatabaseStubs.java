package org.sigmah.database;

import com.bedatadriven.rebar.sql.server.jdbc.JdbcDatabase;
import com.bedatadriven.rebar.sql.server.jdbc.SqliteStubDatabase;

public class ClientDatabaseStubs {

	public static JdbcDatabase sitesSimple() {
		String dbFile = ClientDatabaseStubs.class.getResource("/dbunit/sites-simple.sqlite").getFile();
		return new SqliteStubDatabase(dbFile);
	}
	
}
