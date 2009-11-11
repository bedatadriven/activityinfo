package com.google.gwt.gears.jdbc.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.DatabaseException;

import java.sql.*;

/**
 * @author Alex Bertram
 */
class StatementImpl implements Statement {

  private final ConnectionImpl conn;
  protected final Database database;

  protected String sql;
  protected ResultSet rs;
  private int updateCount;
  private int primaryKey;

  StatementImpl(ConnectionImpl connection, Database database) {
    this.database = database;
    this.conn = connection;
  }

  StatementImpl(ConnectionImpl connection, Database database, String sql) {
    this.conn = connection;
    this.database = database;
    this.sql = sql;
  }

  protected com.google.gwt.gears.client.database.ResultSet doExecute() throws DatabaseException {
    GWT.log("Statement: Executing SQL: " + sql, null);
    return database.execute(sql);
  }

  protected ResultSet doExecuteQuery() throws SQLException {
    try {
      this.updateCount = -1;
      this.primaryKey = 0;
      this.rs = new ResultSetImpl(this, doExecute());
      return rs;
    } catch (DatabaseException e) {
      throw makeSqlException(e);
    }
  }

  private ResultSet doExecuteQuery(String sql) throws SQLException {
    this.sql = sql;
    return doExecuteQuery();
  }

  public ResultSet executeQuery(String sql) throws SQLException {
    return doExecuteQuery(sql);
  }

  public int doExecuteUpdate() throws SQLException {
    this.rs = null;
    try {
      doExecute();
      this.updateCount = database.getRowsAffected();
      this.primaryKey = database.getLastInsertRowId();
      return this.updateCount;
    } catch (DatabaseException e) {
      throw makeSqlException(e);
    }
  }

  protected SQLException makeSqlException(DatabaseException e) {
    return new SQLException("Gears/Sqlite threw a DatabaseException on the following unprepared" +
        " statement SQL: '" + sql + "'", e);
  }

  public int doExecuteUpdate(String sql) throws SQLException {
    this.sql = sql;
    return doExecuteUpdate();
  }


  public int executeUpdate(String sql) throws SQLException {
    this.sql = sql;
    try {
      doExecute();
      return database.getRowsAffected();
    } catch (DatabaseException e) {
      throw makeSqlException(e);
    }
  }


  public void close() throws SQLException {
    if (rs != null) {
      rs.close();
      rs = null;
    }
  }


  public int getMaxFieldSize() throws SQLException {
    return 0;
  }


  public void setMaxFieldSize(int max) throws SQLException {

  }


  public int getMaxRows() throws SQLException {
    return 0;
  }


  public void setMaxRows(int max) throws SQLException {

  }


  public void setEscapeProcessing(boolean enable) throws SQLException {

  }


  public int getQueryTimeout() throws SQLException {
    return 0;
  }


  public void setQueryTimeout(int seconds) throws SQLException {

  }


  public void cancel() throws SQLException {

  }


  public SQLWarning getWarnings() throws SQLException {
    return null;
  }


  public void clearWarnings() throws SQLException {

  }


  public void setCursorName(String name) throws SQLException {

  }


  public boolean execute(String sql) throws SQLException {
    throw new SQLFeatureNotSupportedException("You should call executeQuery or executeUpdate instead of plain " +
        "execute(). For one, why decide which type of query you're going to do at runtime when you can " +
        "do it at design time, and for another I haven't really got around to implementing this. This " +
        "whole business with multiple ResultSets is confusing.");
  }


  public ResultSet getResultSet() throws SQLException {
    return rs;
  }


  public int getUpdateCount() throws SQLException {
    return updateCount;
  }


  public boolean getMoreResults() throws SQLException {
    return false;
  }


  public void setFetchDirection(int direction) throws SQLException {
    if (direction != ResultSet.FETCH_FORWARD)
      throw new SQLException();
  }


  public int getFetchDirection() throws SQLException {
    return ResultSet.FETCH_FORWARD;
  }


  public void setFetchSize(int rows) throws SQLException {

  }


  public int getFetchSize() throws SQLException {
    return 1;
  }


  public int getResultSetConcurrency() throws SQLException {
    return ResultSet.CONCUR_READ_ONLY;
  }


  public int getResultSetType() throws SQLException {
    return ResultSet.TYPE_FORWARD_ONLY;
  }


  public void addBatch(String sql) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }


  public void clearBatch() throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }


  public int[] executeBatch() throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }


  public Connection getConnection() throws SQLException {
    return null;
  }


  public boolean getMoreResults(int current) throws SQLException {
    return false;
  }


  public ResultSet getGeneratedKeys() throws SQLException {
    return new KeyResultSetImpl(this.primaryKey);
  }


  public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
    return doExecuteUpdate(sql);
  }


  public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
    if (columnIndexes.length > 1)
      throw new SQLException("Sqlite/Gears only supports one generated value per table,  you've asked for "
          + columnIndexes.length);
    return doExecuteUpdate();
  }

  public int executeUpdate(String sql, String[] columnNames) throws SQLException {
    if (columnNames.length > 1)
      throw new SQLException("Sqlite/Gears only supports one generated value per table, you've asked for "
          + columnNames.length);
    return 0;
  }


  public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
    return execute(sql);
  }


  public boolean execute(String sql, int[] columnIndexes) throws SQLException {
    return execute(sql);
  }


  public boolean execute(String sql, String[] columnNames) throws SQLException {
    return execute(sql);
  }


  public int getResultSetHoldability() throws SQLException {
    return 0;
  }


  public boolean isClosed() throws SQLException {
    return false;
  }


  public void setPoolable(boolean poolable) throws SQLException {
    // ignoring per spec
  }


  public boolean isPoolable() throws SQLException {
    return false;
  }


  public <T> T unwrap(Class<T> iface) throws SQLException {
    return null;
  }


  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return false;
  }

}
