package org.activityinfo.server.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Provides a property set for the MySQL JDBC driver given a user
 * name and password.
 * 
 * <p>This is where the MySQL configuration should be specified, not 
 * in the external configuration (for example through query strings).
 *
 */
public final class MySqlConfig {

	private MySqlConfig() { }

	private static Logger LOGGER = Logger.getLogger(MySqlConfig.class);
	
	public static Properties get(String userName, String password) {
		Properties properties = new Properties();
		properties.setProperty("user", userName);
		properties.setProperty("password", password);
		properties.setProperty("useUnicode", "true");
		properties.setProperty("characterEncoding", "utf8");
		return properties;
	}
	
	public static Connection initConnection(Connection connection)  {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("SET NAMES 'utf8mb4'");
			stmt.close();
		} catch(SQLException e) {
			LOGGER.error( "Failed to execute init statements for SQL connection", e);
		}
		return connection;
	}
}
