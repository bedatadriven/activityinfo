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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {


  /**
   * Grab a connection, with the autocommit mode specified by
   * <tt>hibernate.connection.autocommit</tt>.
   * @return a JDBC connection
   * @throws SQLException
   */
  Connection getConnection() throws SQLException;

  /**
   * Dispose of a used connection.
   * @param conn a JDBC connection
   * @throws SQLException
   */
  public void closeConnection(Connection conn) throws SQLException;

  /**
   * Release all resources held by this provider. JavaDoc requires a second sentence.
   */
  public void close();

  
}
