package com.google.gwt.gears.jdbc.client;

import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.DatabaseException;

import java.sql.*;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * @author Alex Bertram
 */
class ConnectionImpl implements Connection {

  private final Database database;
  private boolean closed = true;

  /**
   * @param database An open Gears <code>Database</code>
   */
  ConnectionImpl(Database database) {
    this.database = database;
    this.closed = false;
  }

  public Statement createStatement() throws SQLException {
    return new StatementImpl(this, database);
  }

  public PreparedStatement prepareStatement(String sql) throws SQLException {
    return new PreparedStatementImpl(this, database, sql);
  }


  public void close() throws SQLException {
    try {
      database.close();

    } catch (DatabaseException e) {
      throw new SQLException(e);
    }
    closed = true;
  }


  public boolean isClosed() throws SQLException {
    return closed;
  }


  public DatabaseMetaData getMetaData() throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }


  public void setReadOnly(boolean readOnly) throws SQLException {
    if (readOnly)
      throw new IllegalArgumentException();
  }


  public boolean isReadOnly() throws SQLException {
    return true;
  }


  public void setCatalog(String catalog) throws SQLException {
    // silently ignoring, according to spec
  }


  public String getCatalog() throws SQLException {
    // returning null, according to spec
    return null;
  }


  public void setTransactionIsolation(int level) throws SQLException {
    // TODO: what isolation level does Sqlite Support?
  }


  public int getTransactionIsolation() throws SQLException {
    // TODO: what isolation level does Sqlite Support?
    return 0;
  }


  public SQLWarning getWarnings() throws SQLException {
    return null;
  }


  public void clearWarnings() throws SQLException {

  }


  public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
    if (resultSetType != ResultSet.TYPE_FORWARD_ONLY) {
      throw new SQLFeatureNotSupportedException("The Gears database supports only TYPE_FORWARD_ONLY recordsets.");
    }
    if (resultSetConcurrency != ResultSet.CONCUR_READ_ONLY)
      throw new SQLFeatureNotSupportedException("The Gears database supports only CONCUR_READ_ONLY");

    return new StatementImpl(this, database);
  }


  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
    if (resultSetType != ResultSet.TYPE_FORWARD_ONLY) {
      throw new SQLFeatureNotSupportedException("The Gears database supports only TYPE_FORWARD_ONLY recordsets.");
    }
    if (resultSetConcurrency != ResultSet.CONCUR_READ_ONLY)
      throw new SQLFeatureNotSupportedException("The Gears database supports only CONCUR_READ_ONLY");

    return new PreparedStatementImpl(this, database, sql);
  }


  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }


  public Map<String, Class<?>> getTypeMap() throws SQLException {
    return Collections.emptyMap();
  }


  public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }


  public void setHoldability(int holdability) throws SQLException {
    // uhhh....
  }


  public int getHoldability() throws SQLException {
    // uhhh.. not sure I know what "holdability" is
    return ResultSet.CLOSE_CURSORS_AT_COMMIT;
  }


  public Savepoint setSavepoint() throws SQLException {
    throw new SQLFeatureNotSupportedException("Not implemented by the Gears JDBC wrapper...");
  }


  public Savepoint setSavepoint(String name) throws SQLException {
    throw new SQLFeatureNotSupportedException("Not implemented by the Gears JDBC wrapper...");
  }


  public void rollback(Savepoint savepoint) throws SQLException {
    throw new SQLFeatureNotSupportedException("Not yet implemented by the Gears JDBC wrapper...");
  }


  public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    throw new SQLFeatureNotSupportedException("Not implemented by the Gears JDBC wrapper...");
  }


  public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    if (resultSetType != ResultSet.TYPE_FORWARD_ONLY) {
      throw new SQLFeatureNotSupportedException("The Gears database supports only TYPE_FORWARD_ONLY recordsets.");
    }
    if (resultSetConcurrency != ResultSet.CONCUR_READ_ONLY)
      throw new SQLFeatureNotSupportedException("The Gears database supports only CONCUR_READ_ONLY");

    return new StatementImpl(this, database);
  }


  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    if (resultSetType != ResultSet.TYPE_FORWARD_ONLY) {
      throw new SQLFeatureNotSupportedException("The Gears database supports only TYPE_FORWARD_ONLY recordsets.");
    }
    if (resultSetConcurrency != ResultSet.CONCUR_READ_ONLY)
      throw new SQLFeatureNotSupportedException("The Gears database supports only CONCUR_READ_ONLY");

    return new PreparedStatementImpl(this, database, sql);
  }


  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }


  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
    return new PreparedStatementImpl(this, database, sql);
  }


  public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
    return new PreparedStatementImpl(this, database, sql);
  }

  public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
    return new PreparedStatementImpl(this, database, sql);
  }

  public Clob createClob() throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public Blob createBlob() throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public NClob createNClob() throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public SQLXML createSQLXML() throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public boolean isValid(int timeout) throws SQLException {
    try {
      database.execute("select * from sqlite_master");
      return true;
    } catch (DatabaseException e) {
      return false;
    }
  }

  public void setClientInfo(String name, String value) throws SQLClientInfoException {
    throw new UnsupportedOperationException();
  }

  public void setClientInfo(Properties properties) throws SQLClientInfoException {
    throw new UnsupportedOperationException();
  }

  public String getClientInfo(String name) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public Properties getClientInfo() throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public <T> T unwrap(Class<T> iface) throws SQLException {
    return null;
  }

  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return false;
  }

  public CallableStatement prepareCall(String sql) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public String nativeSQL(String sql) throws SQLException {
    return sql;
  }

  public void setAutoCommit(boolean autoCommit) throws SQLException {
    // TODO
  }

  public boolean getAutoCommit() throws SQLException {
    // TODO
    return true;
  }

  /**
   * Does nothing yet
   *
   * @throws SQLException
   */
  public void commit() throws SQLException {
    // TODO
  }

  /**
   * Does nothing yet
   *
   * @throws SQLException
   */
  public void rollback() throws SQLException {
    // TODO
  }
}
