package com.google.gwt.gears.jdbc.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.DatabaseException;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex Bertram
 */
class PreparedStatementImpl extends StatementImpl implements PreparedStatement {

  private Map<Integer, String> parameters = new HashMap<Integer, String>();
  private int numParameters = 0;

  public PreparedStatementImpl(ConnectionImpl connection, Database database) {
    super(connection, database);
  }

  public PreparedStatementImpl(ConnectionImpl connection, Database database, String sql) {
    super(connection, database, sql);
  }


  public void setString(int parameterIndex, String x) throws SQLException {
    parameters.put(parameterIndex, x);
    if (parameterIndex > numParameters) {
      numParameters = parameterIndex;
    }
  }

  private String[] getParameters() {
    String[] pa = new String[numParameters];
    for (int i = 0; i != numParameters; ++i) {
      pa[i] = parameters.get(i + 1);
    }
    return pa;
  }

  @Override
  protected com.google.gwt.gears.client.database.ResultSet doExecute() throws DatabaseException {
    GWT.log("Executing prepared statement: " + sql, null);
    return database.execute(sql, getParameters());
  }


  public ParameterMetaData getParameterMetaData() throws SQLException {
    return null;
  }

  @Override
  protected SQLException makeSqlException(DatabaseException e) {
    try {
      // try to assemble the parameters:
      StringBuilder sb = new StringBuilder();
      sb.append("Gears/Sqlite has thrown an exception on the a PreparedStateent with the following SQL: \n").append(sql);
      sb.append("\nParameters: ");
      String[] pa = getParameters();
      for (int i = 0; i != pa.length; ++i) {
        sb.append("\n").append(i + 1).append("=[").append(pa[i]).append("]");
      }
      return new SQLException(sb.toString(), e);
    } catch (Throwable caught) {
      return new SQLException("Gears/sqlite has thrown an exception on the SQL: \n" + sql + " and threw " +
          "antoher exception when I tried to list the parameters", e);
    }

  }

  /*
  *
  * Everything below makes reference to the base class or the
  * methods above.
  *
  */

  public ResultSet executeQuery() throws SQLException {
    return doExecuteQuery();
  }

  public int executeUpdate() throws SQLException {
    return doExecuteUpdate();
  }

  public void setNull(int parameterIndex, int sqlType) throws SQLException {
    setString(parameterIndex, null);
  }

  public void setBoolean(int parameterIndex, boolean x) throws SQLException {
    setString(parameterIndex, x ? "1" : "0");
  }

  public void setByte(int parameterIndex, byte x) throws SQLException {
    setString(parameterIndex, Byte.toString(x));
  }

  public void setShort(int parameterIndex, short x) throws SQLException {
    setString(parameterIndex, Short.toString(x));
  }

  public void setInt(int parameterIndex, int x) throws SQLException {
    setString(parameterIndex, Integer.toString(x));
  }

  public void setLong(int parameterIndex, long x) throws SQLException {
    setString(parameterIndex, Long.toString(x));
  }

  public void setFloat(int parameterIndex, float x) throws SQLException {
    setString(parameterIndex, Float.toString(x));
  }

  public void setDouble(int parameterIndex, double x) throws SQLException {
    setString(parameterIndex, Double.toString(x));
  }

  public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }


  public void setBytes(int parameterIndex, byte[] x) throws SQLException {
    // TODO
    throw new SQLFeatureNotSupportedException();
  }

  public void setDate(int parameterIndex, Date x) throws SQLException {
    // TODO: is this correct?
    setString(parameterIndex, x == null ? null : Long.toString(x.getTime()));
  }

  public void setTime(int parameterIndex, Time x) throws SQLException {
    setString(parameterIndex, x == null ? null : x.toString());
  }

  public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
    setString(parameterIndex, x == null ? null : x.toString());
  }

  public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  @Deprecated
  public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void clearParameters() throws SQLException {
    parameters.clear();
  }

  public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
    setObject(parameterIndex, x);
  }

  public void setObject(int parameterIndex, Object x) throws SQLException {
    if (x instanceof Date) {
      setDate(parameterIndex, (Date) x);
    } else {
      setString(parameterIndex, x.toString());
    }
  }

  public boolean execute() throws SQLException {
    throw new SQLFeatureNotSupportedException("You must call executeQuery or executeUpdate for the time being.");
  }

  public void addBatch() throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setRef(int parameterIndex, Ref x) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setBlob(int parameterIndex, Blob x) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setClob(int parameterIndex, Clob x) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setArray(int parameterIndex, Array x) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public ResultSetMetaData getMetaData() throws SQLException {
    return null;
  }

  public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
    setDate(parameterIndex, x);
  }

  public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
    setTime(parameterIndex, x);
  }

  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
    setTimestamp(parameterIndex, x);
  }

  public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
    setString(parameterIndex, null);
  }

  public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
    setObject(parameterIndex, x);
  }

  /*
  * Unimplemented parameter setters below
  */

  public void setURL(int parameterIndex, URL x) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setRowId(int parameterIndex, RowId x) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setNString(int parameterIndex, String value) throws SQLException {
    setString(parameterIndex, value);
  }

  public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setNClob(int parameterIndex, NClob value) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setClob(int parameterIndex, Reader reader) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void setNClob(int parameterIndex, Reader reader) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }
}
