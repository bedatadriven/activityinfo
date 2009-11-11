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

import javax.persistence.TemporalType;
import javax.persistence.Query;
import java.util.Date;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implements the query interface for queries that are known only at run time.
 *
 * @author Alex Bertram
 */
public abstract class AbstractNativeRuntimeQuery<T, K> extends AbstractNativeQuery {

  private final SQLStatement sql;
  private Map<String, Object> namedParameters = new HashMap<String, Object>();
  private Map<Integer, Object> posParameters = new HashMap<Integer, Object>();

  public AbstractNativeRuntimeQuery(Connection conn, String sql) {
    super(conn);
    this.sql = new SQLStatement(sql);
  }

  @Override
  protected PreparedStatement prepareStatement() {
    try {
      PreparedStatement stmt;
      if(sql.getQueryType() != SQLStatement.TYPE_SELECT)
        throw new RuntimeException("You tried to call createNativeQuery(sql, entityClass) with something else " +
            "besides a select query. Your sql was: " + sql.toString());

      // trade in the named/positional parameters for garden
      // variety jdbc params ( "?" )
      StringBuilder nativeSql = new StringBuilder();
      nativeSql.append(sql.toNativeSQL());
      if(this.maxResults != -1) {
        nativeSql.append(" LIMIT ").append(this.maxResults);
      }
      if(this.firstResult != 0) {
        nativeSql.append(" OFFSET ").append(this.firstResult);
      }
      stmt = conn.prepareStatement(nativeSql.toString());

      // set query parameters
      fillParameters(stmt);
      return stmt;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  protected void fillParameters(PreparedStatement stmt) throws SQLException {
    for(SQLStatement.Token token : sql.getTokens()) {
      if(token.isParameter()) {
        if(token.getParameterName() != null) {
          stmt.setObject(token.getParameterIndex(), namedParameters.get(token.getParameterName()));
        } else {
          stmt.setObject(token.getParameterPosition(), posParameters.get(token.getParameterPosition()));
        }
      }
    }
  }

  @Override
  public Query setParameter(String name, Object value) {
    assert sql.hasParameter(name) : "No such parameter '" + name + "'";
    namedParameters.put(name, value);
    return this;
  }

  @Override
  public Query setParameter(String name, Calendar value, TemporalType temporalType) {
    throw new UnsupportedOperationException("Calendar stuff not yet implemented");
  }

  @Override
  public Query setParameter(String name, Date value, TemporalType temporalType) {
    namedParameters.put(name, value);
    return this;
  }

  @Override
  public Query setParameter(int position, Object value) {
    posParameters.put(position, value);
    return this;
  }

  @Override
  public Query setParameter(int position, Date value, TemporalType temporalType) {
    posParameters.put(position, value);
    return this;
  }

  @Override
  public Query setParameter(int position, Calendar value, TemporalType temporalType) {
    throw new UnsupportedOperationException("Calendar stuff not yet implemented");
  }
}
