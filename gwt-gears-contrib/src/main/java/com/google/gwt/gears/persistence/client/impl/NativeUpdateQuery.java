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

package com.google.gwt.gears.persistence.client.impl;

import javax.persistence.*;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * @author Alex Bertram
 */
public class NativeUpdateQuery extends AbstractNativeRuntimeQuery {

  private EntityManager em;
  private PreparedStatement stmt;

  public NativeUpdateQuery(EntityManager em, Connection conn, String sql) {
    super(conn, sql);
    this.em = em;
  }

  public List getResultList() {
    throw new PersistenceException("getResultList() cannot be called for update queries");
  }

  public Object getSingleResult() {
    throw new PersistenceException("getSingleResult() cannot be called for update queries");
  }

  public int executeUpdate() {
    try {
      if(stmt == null)
        stmt = prepareStatement();
      else
        fillParameters(stmt);

      return stmt.executeUpdate();

    } catch (SQLException e) {
      throw new PersistenceException(e);
    }
  }

  @Override
  protected void initColumnMapping(ResultSet rs) {

  }

  @Override
  protected Object newResultInstance(ResultSet rs) throws SQLException {
    return null;
  }
}
