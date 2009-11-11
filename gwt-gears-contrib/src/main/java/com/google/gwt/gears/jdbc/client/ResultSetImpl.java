package com.google.gwt.gears.jdbc.client;


import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.gears.client.database.DatabaseException;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex Bertram
 */
class ResultSetImpl extends AbstractResultSetImpl {

  private StatementImpl statement;
  private com.google.gwt.gears.client.database.ResultSet rs;
  private boolean beforeFirst = true;
  private boolean wasNull = false;
  private Map<String, Integer> map = null;
  private int rowIndex = 0;


  ResultSetImpl(StatementImpl statement, com.google.gwt.gears.client.database.ResultSet rs) {
    this.statement = statement;
    this.rs = rs;
  }

  public boolean next() throws SQLException {
    if (!beforeFirst) {
      rs.next();
    } else {
      beforeFirst = false;
    }
    rowIndex++;
    return rs.isValidRow();
  }


  public void close() throws SQLException {
    try {
      rs.close();
    } catch (DatabaseException e) {
      throw new SQLException(e);
    }
  }


  public boolean wasNull() throws SQLException {
    return wasNull;
  }


  public String getString(int columnIndex) throws SQLException {
    try {
      String value = rs.getFieldAsString(columnIndex - 1);
      wasNull = (value == null);
      return value;
    } catch (DatabaseException e) {
      throw new SQLException(e);
    }
  }


  public boolean getBoolean(int columnIndex) throws SQLException {
    return getInt(columnIndex) != 0;
  }


  public byte getByte(int columnIndex) throws SQLException {
    return (byte) getDouble(columnIndex);
  }


  public short getShort(int columnIndex) throws SQLException {
    return (short) getDouble(columnIndex);
  }


  public int getInt(int columnIndex) throws SQLException {
    return (int) getDouble(columnIndex);
  }


  public long getLong(int columnIndex) throws SQLException {
    return (long) getDouble(columnIndex);
  }


  public float getFloat(int columnIndex) throws SQLException {
    return (float) getFloat(columnIndex);
  }


  public double getDouble(int columnIndex) throws SQLException {
    try {
      if (uncheckedFieldIsNull(rs, columnIndex - 1)) {
        wasNull = true;
        return 0;
      } else {
        wasNull = false;
        return uncheckedGetFieldAsDouble(rs, columnIndex - 1);
      }
    } catch (JavaScriptException e) {
      throw new SQLException(e.getMessage(), e);
    }
  }


  public Date getDate(int columnIndex) throws SQLException {
    try {
      java.util.Date date = rs.getFieldAsDate(columnIndex - 1);
      wasNull = (date == null);
      return date == null ? null : new Date(date.getTime());
    } catch (DatabaseException e) {
      throw new SQLException(e);
    }
  }


  public Time getTime(int columnIndex) throws SQLException {
    try {
      java.util.Date date = rs.getFieldAsDate(columnIndex - 1);
      wasNull = (date == null);
      return date == null ? null : new Time(date.getTime());
    } catch (DatabaseException e) {
      throw new SQLException(e);
    }

  }


  public Timestamp getTimestamp(int columnIndex) throws SQLException {
    try {
      java.util.Date date = rs.getFieldAsDate(columnIndex - 1);
      wasNull = (date == null);
      return date == null ? null : new Timestamp(date.getTime());
    } catch (DatabaseException e) {
      throw new SQLException(e);
    }
  }


  public int findColumn(String columnLabel) throws SQLException {
    if (map == null) {
      try {
        Map<String, Integer> tmp = new HashMap<String, Integer>();
        for (int i = 0; i != rs.getFieldCount(); ++i) {
          tmp.put(rs.getFieldName(i), i + 1);
        }
        map = tmp;
      } catch (DatabaseException e) {
        throw new SQLException(e);
      }
    }
    Integer index = map.get(columnLabel);
    if (index == 0) {
      throw new SQLException("ResultSet has no column named" + columnLabel);
    }
    return index;
  }


  public ResultSetMetaData getMetaData() throws SQLException {
    return new ResultSetMetadataImpl(rs);
  }


  public boolean isBeforeFirst() throws SQLException {
    return beforeFirst;
  }


  public boolean isAfterLast() throws SQLException {
    return !rs.isValidRow();
  }


  public void afterLast() throws SQLException {
    while (rs.isValidRow()) {
      rs.next();
    }
  }


  public int getRow() throws SQLException {
    if (rs.isValidRow()) {
      return rowIndex + 1;
    } else {
      return 0;
    }
  }


  public boolean absolute(int row) throws SQLException {
    return false;
  }


  public boolean relative(int rows) throws SQLException {
    if (rows < 0) {
      throw new SQLException(RS_IS_FORWARD_ONLY);
    }
    while (rows != 0) {
      if (!next())
        return false;
    }
    return rs.isValidRow();
  }

  private native boolean uncheckedFieldIsNull(com.google.gwt.gears.client.database.ResultSet rs, int fieldIndex) /*-{
      return rs.field(fieldIndex) == null;
    }-*/;

  /**
   * This must be called only if uncheckedFieldIsNull return false
   *
   * @param rs
   * @param fieldIndex
   * @return
   */
  private native double uncheckedGetFieldAsDouble(com.google.gwt.gears.client.database.ResultSet rs, int fieldIndex) /*-{
        return Number(rs.field(fieldIndex));
    }-*/;


}
