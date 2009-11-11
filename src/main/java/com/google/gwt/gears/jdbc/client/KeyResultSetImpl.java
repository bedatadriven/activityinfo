package com.google.gwt.gears.jdbc.client;

import java.sql.*;

/**
 * @author Alex Bertram
 */
class KeyResultSetImpl extends AbstractResultSetImpl {

  private final int primaryKey;
  private int rowIndex;

  KeyResultSetImpl(int primaryKey) {
    this.primaryKey = primaryKey;
    this.rowIndex = 0;
  }

  public boolean next() throws SQLException {
    return rowIndex++ == 0;
  }

  public int getInt(int columnIndex) throws SQLException {
    if (columnIndex != 1)
      throw new SQLException("The key ResultSet has only one column (index = 1).");
    return primaryKey;
  }


  public void close() throws SQLException {

  }

  public boolean wasNull() throws SQLException {
    return false;
  }

  public String getString(int columnIndex) throws SQLException {
    return Integer.toString(getInt(columnIndex));
  }

  public boolean getBoolean(int columnIndex) throws SQLException {
    return true;
  }

  public byte getByte(int columnIndex) throws SQLException {
    return (byte) getInt(columnIndex);
  }

  public short getShort(int columnIndex) throws SQLException {
    return (short) getInt(columnIndex);
  }

  public long getLong(int columnIndex) throws SQLException {
    return getInt(columnIndex);
  }

  public float getFloat(int columnIndex) throws SQLException {
    return getInt(columnIndex);
  }

  public double getDouble(int columnIndex) throws SQLException {
    return getInt(columnIndex);
  }

  public Date getDate(int columnIndex) throws SQLException {
    // uuhhh
    return new Date(getInt(columnIndex));
  }

  public Time getTime(int columnIndex) throws SQLException {
    return new Time(getInt(columnIndex));
  }

  public Timestamp getTimestamp(int columnIndex) throws SQLException {
    return new Timestamp(getInt(columnIndex));
  }

  public ResultSetMetaData getMetaData() throws SQLException {
    return null;
  }

  public int findColumn(String columnLabel) throws SQLException {
    return 1;
  }

  public boolean isBeforeFirst() throws SQLException {
    return rowIndex < 1;
  }

  public boolean isAfterLast() throws SQLException {
    return rowIndex > 1;
  }

  public void afterLast() throws SQLException {
    rowIndex = 2;
  }

  public int getRow() throws SQLException {
    return rowIndex;
  }

  public boolean absolute(int row) throws SQLException {
    if (row == 1) {
      rowIndex = 1;
      return true;
    }
    return false;
  }

  public boolean relative(int rows) throws SQLException {
    rowIndex += rows;
    return rowIndex == 1;
  }
}
