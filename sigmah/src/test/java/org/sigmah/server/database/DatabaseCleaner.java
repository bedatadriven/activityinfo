/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Cleans the MySQL test database
 * 
 */
public class DatabaseCleaner {

	private final Provider<Connection> connectionProvider;
	
	private static final String LIQUIBASE_TABLE_PREFIX = "databasechangelog";
	
	
    @Inject
    public DatabaseCleaner(Provider<Connection> connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public void clean() {
    	Connection connection = connectionProvider.get();

    	try {
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			statement.execute("SET foreign_key_checks = 0");
			
			ResultSet tables = connection.getMetaData().getTables(null, null, null, new String[] {"TABLE"});
			try {
				while(tables.next()) {
					String tableName = tables.getString(3);
					if(!tableName.startsWith(LIQUIBASE_TABLE_PREFIX)) {
						statement.execute("DELETE FROM " + tableName);
					}
				}
			} finally {
				tables.close();
			}
			statement.execute("SET foreign_key_checks = 1");
			statement.close();
			connection.commit();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
			
		} finally {
			try {
				connection.close();
			} catch (SQLException ignored) {
			}			
		}
    }
}

