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


import javax.persistence.FlushModeType;
import javax.persistence.Query;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alex Bertram
 */
public abstract class AbstractNativeQuery<T> implements Query {

  protected final Connection conn;

  /**
   * The first row to return
   */
  protected int firstResult = 0;

  /**
   * The maximum number of results to return, or -1
   * if setMaxResults has not been called.
   */
  protected int maxResults = -1;

  protected Map<Integer, Object> positionalParameters = new HashMap<Integer, Object>(0);

  protected Map<String, Object> namedParameters = new HashMap<String, Object>(0);

  public AbstractNativeQuery(Connection conn) {
    this.conn = conn;
  }

  /**
   * @return a prepared statement ready to be executed.
   */
  protected abstract PreparedStatement prepareStatement();

  protected abstract void initColumnMapping(ResultSet rs);

  @Override
  public List<T> getResultList() {
    ResultSet rs = null;
    try {
      List<T> results = new ArrayList<T>();
      PreparedStatement stmt = prepareStatement();
      rs = stmt.executeQuery();
      initColumnMapping(rs);
      while (rs.next()) {
        results.add(newResultInstance(rs));
      }
      rs.close();
      return results;

    } catch (SQLException e) {
      if (rs != null) {
        try {
          rs.close();
        } catch (Throwable ignored) {
        }
      }
      throw new RuntimeException(e);
    }
  }


  @Override
  public T getSingleResult() {
    ResultSet rs = null;
    try {
      T result = null;
      PreparedStatement stmt = prepareStatement();
      rs = stmt.executeQuery();
      if (rs.next()) {
        result = newResultInstance(rs);
      }
      rs.close();
      return result;

    } catch (SQLException e) {
      if (rs != null) {
        try {
          rs.close();
        } catch (Throwable ignored) {
        }
      }
      throw new RuntimeException(e);
    }
  }

  protected abstract T newResultInstance(ResultSet rs) throws SQLException;

  @Override
  public int executeUpdate() {
    try {
      PreparedStatement stmt = prepareStatement();
      return stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException();
    }
  }

  @Override
  public Query setMaxResults(int maxResult) {
    this.maxResults = maxResult;
    return this;
  }

  @Override
  public Query setFirstResult(int startPosition) {
    this.firstResult = startPosition;
    return this;
  }

  @Override
  public Query setHint(String hintName, Object value) {
    return this;
  }

  @Override
  public Query setFlushMode(FlushModeType flushMode) {
    return this;
  }
}
