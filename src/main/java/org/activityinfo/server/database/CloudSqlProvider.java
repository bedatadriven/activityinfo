package org.activityinfo.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.google.inject.Provider;

public class CloudSqlProvider implements Provider<Connection> {

	@Override
	public Connection get() {
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			return DriverManager.getConnection("jdbc:google:rdbms://bedatadriven.com:activityinfo:activityinfo/activityinfo");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
