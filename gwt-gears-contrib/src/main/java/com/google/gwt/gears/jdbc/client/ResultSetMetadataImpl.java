package com.google.gwt.gears.jdbc.client;

import com.google.gwt.gears.client.database.DatabaseException;
import com.google.gwt.gears.client.database.ResultSet;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;


/**
 * @author Alex Bertram
 */
class ResultSetMetadataImpl implements ResultSetMetaData {

  private ResultSet rs;

  public ResultSetMetadataImpl(ResultSet rs) {
    this.rs = rs;
  }


  public int getColumnCount() throws SQLException {
    return rs.getFieldCount();
  }


  public boolean isAutoIncrement(int column) throws SQLException {
    // todo
    throw new UnsupportedOperationException();
  }


  public boolean isCaseSensitive(int column) throws SQLException {
    return false;
  }


  public boolean isSearchable(int column) throws SQLException {
    return true;
  }


  public boolean isCurrency(int column) throws SQLException {
    throw new UnsupportedOperationException();
  }


  public int isNullable(int column) throws SQLException {
    return columnNullableUnknown;
  }


  public boolean isSigned(int column) throws SQLException {
    throw new UnsupportedOperationException();
  }


  public int getColumnDisplaySize(int column) throws SQLException {
    throw new UnsupportedOperationException();
  }


  public String getColumnLabel(int column) throws SQLException {
    return getColumnName(column);
  }


  public String getColumnName(int column) throws SQLException {
    try {
      return rs.getFieldName(column - 1);
    } catch (DatabaseException e) {
      throw new SQLException(e);
    }
  }


  public String getSchemaName(int column) throws SQLException {
    return "";
  }


  public int getPrecision(int column) throws SQLException {
    return 0;
  }


  public int getScale(int column) throws SQLException {
    return 0;
  }


  public String getTableName(int column) throws SQLException {
    return "";
  }


  public String getCatalogName(int column) throws SQLException {
    return "";
  }


  public int getColumnType(int column) throws SQLException {
    throw new UnsupportedOperationException();
  }


  public String getColumnTypeName(int column) throws SQLException {
    throw new UnsupportedOperationException();
  }


  public boolean isReadOnly(int column) throws SQLException {
    return false;
  }


  public boolean isWritable(int column) throws SQLException {
    return false;
  }


  public boolean isDefinitelyWritable(int column) throws SQLException {
    return false;
  }


  public String getColumnClassName(int column) throws SQLException {
    throw new UnsupportedOperationException();
  }


  public <T> T unwrap(Class<T> iface) throws SQLException {
    return null;
  }


  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return false;
  }
}
