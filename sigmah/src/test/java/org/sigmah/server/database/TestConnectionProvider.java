package org.sigmah.server.database;

import java.sql.Connection;
import java.sql.DriverManager;

import com.google.inject.Provider;

public class TestConnectionProvider implements Provider<Connection>{

	@Override
	public Connection get() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://localhost/activityinfo-test", "root", "adminpwd");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
}
