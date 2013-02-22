package org.activityinfo.server.database;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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

            ResultSet tables = connection.getMetaData().getTables(null, null,
                null, new String[] { "TABLE" });
            try {
                while (tables.next()) {
                    String tableName = tables.getString(3);
                    if (!tableName.toLowerCase().startsWith(
                        LIQUIBASE_TABLE_PREFIX)) {
                        statement.execute("DELETE FROM " + tableName);
                        System.err.println("Dropped all from " + tableName);
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
                ignored.printStackTrace();
            }
        }
    }
}
