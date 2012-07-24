package org.activityinfo.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.google.inject.Provider;

/**
 * Provides a connection to the test database.
 * 
 * By default, we connect to the local 'activityinfo-test' database, 
 * with username 'root' and password 'adminpwd'.
 *  
 * This can be override by with the system variables 'testDatabaseUrl', 
 * 'testDatabaseUsername' and 'testDatabasePassword'
 */
public class TestConnectionProvider implements Provider<Connection>{

	private static final String PASSWORD_PROPERTY = "testDatabasePassword";
	private static final String USERNAME_PROPERTY = "testDatabaseUsername";
	private static final String URL_PROPERTY = "testDatabaseUrl";
	
	private static final String DEFAULT_PASSWORD = "adminpwd";
	private static final String DEFAULT_USER_NAME = "root";
	private static final String DEFAULT_URL = "jdbc:mysql://localhost/activityinfo-test";

	@Override
	public Connection get() {
		try {
			System.err.println("Opening test database at " + getUrl());
			Class.forName("com.mysql.jdbc.Driver");

			return MySqlConfig.initConnection(DriverManager.getConnection(getUrl(), MySqlConfig.get(getUsername(), getPassword())));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getUrl() {
		return System.getProperty(URL_PROPERTY, DEFAULT_URL );
	}

	public static String getUsername() {
		return System.getProperty(USERNAME_PROPERTY, DEFAULT_USER_NAME);
	}

	public static String getPassword() {
		return System.getProperty(PASSWORD_PROPERTY, DEFAULT_PASSWORD);	
	}
}
