/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package com.google.gwt.gears.persistence.client;

import com.google.gwt.gears.persistence.client.ConnectionProvider;
import com.google.gwt.gears.jdbc.client.DriverManager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Alex Bertram
 */
public class GearsConnectionProvider implements ConnectionProvider {

  private String databaseName;

  public GearsConnectionProvider(String databaseName) {
    this.databaseName = databaseName;
  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(databaseName);
  }

  public void closeConnection(Connection conn) throws SQLException {
    conn.close();
  }

  public void close() {

  }
}
