/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.PropertiesBasedJdbcDatabaseTester;

import com.bedatadriven.rebar.persistence.client.ConnectionProvider;

/**
 * @author JDH
 */
public class UnitConnectionProvider implements ConnectionProvider {

	private Connection connection = null;
	private String name = "testdb";


	public Connection getConnection() throws SQLException {
		if (connection == null) {
			try {
				Class.forName("org.h2.Driver");
				connection = DriverManager.getConnection("jdbc:h2:mem:" + name + ";IGNORECASE=TRUE");
			} catch (Exception e) {
				throw new Error(e);
			}
		}
		return connection;
	}

	public void closeConnection(Connection conn) throws SQLException {
		conn.close();
	}

	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
