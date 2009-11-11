package com.google.gwt.gears.jdbc.client;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

/**
 * Provides (hopefully) specification-conformant implementation of methods that
 * <code>GearsResultSet</code>s do or cannot support.
 *
 * @author Alex Bertram
 */
abstract class AbstractResultSetImpl implements ResultSet {

  private static final String UPDATES_ARE_NOT_SUPPORTED = "The ResultSet is CONCUR_READONLY (Gears doesn't support cursors and we haven't tried to implement this yet!)";
  protected static final String RS_IS_FORWARD_ONLY = "The ResultSet is FORWARD_ONLY (Gears will likely not support this)";

  @SuppressWarnings("deprecation")
  public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public InputStream getAsciiStream(int columnIndex) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @SuppressWarnings("deprecation")
  public InputStream getUnicodeStream(int columnIndex) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public InputStream getBinaryStream(int columnIndex) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @SuppressWarnings("deprecation")
  public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public InputStream getAsciiStream(String columnLabel) throws SQLException {
    return getAsciiStream(findColumn(columnLabel));
  }

  @SuppressWarnings("deprecation")
  public InputStream getUnicodeStream(String columnLabel) throws SQLException {
    return getUnicodeStream(findColumn(columnLabel));
  }

  public InputStream getBinaryStream(String columnLabel) throws SQLException {
    return getBinaryStream(findColumn(columnLabel));
  }

  public SQLWarning getWarnings() throws SQLException {
    return null;
  }

  public void clearWarnings() throws SQLException {

  }

  public String getCursorName() throws SQLException {
    return null;
  }

  public Object getObject(int columnIndex) throws SQLException {
    return getString(columnIndex);
  }

  public Object getObject(String columnLabel) throws SQLException {
    return getObject(findColumn(columnLabel));
  }

  public Reader getCharacterStream(int columnIndex) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public Reader getCharacterStream(String columnLabel) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public boolean previous() throws SQLException {
    throw new SQLException(RS_IS_FORWARD_ONLY);
  }

  public void setFetchDirection(int direction) throws SQLException {
    throw new SQLException(RS_IS_FORWARD_ONLY);
  }

  public int getFetchDirection() throws SQLException {
    return ResultSet.FETCH_FORWARD;
  }

  public void setFetchSize(int rows) throws SQLException {

  }

  public int getFetchSize() throws SQLException {
    return 1;
  }

  public int getType() throws SQLException {
    return ResultSet.TYPE_FORWARD_ONLY;
  }

  public int getConcurrency() throws SQLException {
    return ResultSet.CONCUR_READ_ONLY;
  }

  public boolean rowUpdated() throws SQLException {
    return false;
  }

  public boolean rowInserted() throws SQLException {
    return false;
  }

  public boolean rowDeleted() throws SQLException {
    return false;
  }

  public void updateNull(int columnIndex) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBoolean(int columnIndex, boolean x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateByte(int columnIndex, byte x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateShort(int columnIndex, short x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateInt(int columnIndex, int x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateLong(int columnIndex, long x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateFloat(int columnIndex, float x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateDouble(int columnIndex, double x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateString(int columnIndex, String x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBytes(int columnIndex, byte[] x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateDate(int columnIndex, Date x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateTime(int columnIndex, Time x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateObject(int columnIndex, Object x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateNull(String columnLabel) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBoolean(String columnLabel, boolean x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateByte(String columnLabel, byte x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateShort(String columnLabel, short x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateInt(String columnLabel, int x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateLong(String columnLabel, long x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateFloat(String columnLabel, float x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateDouble(String columnLabel, double x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateString(String columnLabel, String x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBytes(String columnLabel, byte[] x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateDate(String columnLabel, Date x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateTime(String columnLabel, Time x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateObject(String columnLabel, Object x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void insertRow() throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateRow() throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void deleteRow() throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void refreshRow() throws SQLException {
    throw new SQLException(RS_IS_FORWARD_ONLY);
  }

  public void cancelRowUpdates() throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void moveToInsertRow() throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void moveToCurrentRow() throws SQLException {

  }

  public Statement getStatement() throws SQLException {
    return null;
  }

  public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
    return null;
  }

  public Ref getRef(int columnIndex) throws SQLException {
    return null;
  }

  public Blob getBlob(int columnIndex) throws SQLException {
    return null;
  }

  public Clob getClob(int columnIndex) throws SQLException {
    return null;
  }

  public Array getArray(int columnIndex) throws SQLException {
    return null;
  }

  public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public Ref getRef(String columnLabel) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public Blob getBlob(String columnLabel) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public Clob getClob(String columnLabel) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public Array getArray(String columnLabel) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public Date getDate(int columnIndex, Calendar cal) throws SQLException {
    return getDate(columnIndex);
  }

  public Date getDate(String columnLabel, Calendar cal) throws SQLException {
    return getDate(columnLabel);
  }

  public Time getTime(int columnIndex, Calendar cal) throws SQLException {
    return getTime(columnIndex);
  }

  public Time getTime(String columnLabel, Calendar cal) throws SQLException {
    return getTime(columnLabel);
  }

  public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
    return getTimestamp(columnIndex);
  }

  public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
    return getTimestamp(columnLabel);
  }

  public URL getURL(int columnIndex) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public URL getURL(String columnLabel) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void updateRef(int columnIndex, Ref x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateRef(String columnLabel, Ref x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBlob(int columnIndex, Blob x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBlob(String columnLabel, Blob x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateClob(int columnIndex, Clob x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateClob(String columnLabel, Clob x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateArray(int columnIndex, Array x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateArray(String columnLabel, Array x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public RowId getRowId(int columnIndex) throws SQLException {
    return null;
  }

  public RowId getRowId(String columnLabel) throws SQLException {
    return null;
  }

  public void updateRowId(int columnIndex, RowId x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateRowId(String columnLabel, RowId x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public int getHoldability() throws SQLException {
    return 0;
  }

  public boolean isClosed() throws SQLException {
    return false;
  }

  public void updateNString(int columnIndex, String nString) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateNString(String columnLabel, String nString) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public NClob getNClob(int columnIndex) throws SQLException {
    return null;
  }

  public NClob getNClob(String columnLabel) throws SQLException {
    return null;
  }

  public SQLXML getSQLXML(int columnIndex) throws SQLException {
    return null;
  }

  public SQLXML getSQLXML(String columnLabel) throws SQLException {
    return null;
  }

  public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public String getNString(int columnIndex) throws SQLException {
    return getString(columnIndex);
  }

  public String getNString(String columnLabel) throws SQLException {
    return getString(columnLabel);
  }

  public Reader getNCharacterStream(int columnIndex) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public Reader getNCharacterStream(String columnLabel) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
    throw new SQLFeatureNotSupportedException();

  }

  public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);

  }

  public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);

  }

  public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);

  }

  public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);

  }

  public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateClob(int columnIndex, Reader reader) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateClob(String columnLabel, Reader reader) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateNClob(int columnIndex, Reader reader) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public void updateNClob(String columnLabel, Reader reader) throws SQLException {
    throw new SQLException(UPDATES_ARE_NOT_SUPPORTED);
  }

  public <T> T unwrap(Class<T> iface) throws SQLException {
    return null;
  }

  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return false;
  }

  public boolean isFirst() throws SQLException {
    return false;
  }

  public boolean isLast() throws SQLException {
    return false;
  }

  public void beforeFirst() throws SQLException {
    throw new SQLException(RS_IS_FORWARD_ONLY);
  }

  public boolean first() throws SQLException {
    throw new SQLException(RS_IS_FORWARD_ONLY);
  }

  public boolean last() throws SQLException {
    throw new SQLException(RS_IS_FORWARD_ONLY);
  }

  public byte[] getBytes(int columnIndex) throws SQLException {
    throw new UnsupportedOperationException();
  }

  public String getString(String columnLabel) throws SQLException {
    return getString(findColumn(columnLabel));
  }

  public boolean getBoolean(String columnLabel) throws SQLException {
    return getBoolean(findColumn(columnLabel));
  }

  public byte getByte(String columnLabel) throws SQLException {
    return getByte(findColumn(columnLabel));
  }

  public short getShort(String columnLabel) throws SQLException {
    return getShort(findColumn(columnLabel));
  }

  public int getInt(String columnLabel) throws SQLException {
    return getInt(findColumn(columnLabel));
  }

  public long getLong(String columnLabel) throws SQLException {
    return getLong(findColumn(columnLabel));
  }

  public float getFloat(String columnLabel) throws SQLException {
    return getFloat(findColumn(columnLabel));
  }

  public double getDouble(String columnLabel) throws SQLException {
    return getFloat(findColumn(columnLabel));
  }

  public byte[] getBytes(String columnLabel) throws SQLException {
    return getBytes(findColumn(columnLabel));
  }

  public Date getDate(String columnLabel) throws SQLException {
    return getDate(findColumn(columnLabel));
  }

  public Time getTime(String columnLabel) throws SQLException {
    return getTime(findColumn(columnLabel));
  }

  public Timestamp getTimestamp(String columnLabel) throws SQLException {
    return getTimestamp(findColumn(columnLabel));
  }
}
