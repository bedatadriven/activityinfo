
package java.sql;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;
import java.io.Reader;
import java.io.InputStream;
import java.net.URL;


public interface CallableStatement extends PreparedStatement {
  void registerOutParameter(int parameterIndex, int sqlType)
      throws SQLException;

  void registerOutParameter(int parameterIndex, int sqlType, int scale)
      throws SQLException;

  boolean wasNull() throws SQLException;

  String getString(int parameterIndex) throws SQLException;

  boolean getBoolean(int parameterIndex) throws SQLException;

  byte getByte(int parameterIndex) throws SQLException;

  short getShort(int parameterIndex) throws SQLException;

  int getInt(int parameterIndex) throws SQLException;

  long getLong(int parameterIndex) throws SQLException;

  float getFloat(int parameterIndex) throws SQLException;

  double getDouble(int parameterIndex) throws SQLException;

  BigDecimal getBigDecimal(int parameterIndex, int scale)
      throws SQLException;

  byte[] getBytes(int parameterIndex) throws SQLException;

  Date getDate(int parameterIndex) throws SQLException;

  Time getTime(int parameterIndex) throws SQLException;

  Timestamp getTimestamp(int parameterIndex)
      throws SQLException;


  Object getObject(int parameterIndex) throws SQLException;

  BigDecimal getBigDecimal(int parameterIndex) throws SQLException;

  Object getObject(int parameterIndex, Map<String, Class<?>> map)
      throws SQLException;

  Ref getRef(int parameterIndex) throws SQLException;

  Blob getBlob(int parameterIndex) throws SQLException;

  Clob getClob(int parameterIndex) throws SQLException;

  Array getArray(int parameterIndex) throws SQLException;

  Date getDate(int parameterIndex, Calendar cal)
      throws SQLException;

  Time getTime(int parameterIndex, Calendar cal)
      throws SQLException;

  Timestamp getTimestamp(int parameterIndex, Calendar cal)
      throws SQLException;


  void registerOutParameter(int parameterIndex, int sqlType, String typeName)
      throws SQLException;

  void registerOutParameter(String parameterName, int sqlType)
      throws SQLException;

  void registerOutParameter(String parameterName, int sqlType, int scale)
      throws SQLException;

  void registerOutParameter(String parameterName, int sqlType, String typeName)
      throws SQLException;

  URL getURL(int parameterIndex) throws SQLException;

  void setURL(String parameterName, URL val) throws SQLException;

  void setNull(String parameterName, int sqlType) throws SQLException;

  void setBoolean(String parameterName, boolean x) throws SQLException;

  void setByte(String parameterName, byte x) throws SQLException;

  void setShort(String parameterName, short x) throws SQLException;

  void setInt(String parameterName, int x) throws SQLException;

  void setLong(String parameterName, long x) throws SQLException;

  void setFloat(String parameterName, float x) throws SQLException;

  void setDouble(String parameterName, double x) throws SQLException;

  void setBigDecimal(String parameterName, BigDecimal x) throws SQLException;

  void setString(String parameterName, String x) throws SQLException;

  void setBytes(String parameterName, byte x[]) throws SQLException;

  void setDate(String parameterName, Date x)
      throws SQLException;

  void setTime(String parameterName, Time x)
      throws SQLException;

  void setTimestamp(String parameterName, Timestamp x)
      throws SQLException;

  void setAsciiStream(String parameterName, InputStream x, int length)
      throws SQLException;

  void setBinaryStream(String parameterName, InputStream x,
                       int length) throws SQLException;

  void setObject(String parameterName, Object x, int targetSqlType, int scale)
      throws SQLException;

  void setObject(String parameterName, Object x, int targetSqlType)
      throws SQLException;

  void setObject(String parameterName, Object x) throws SQLException;


  void setCharacterStream(String parameterName,
                          Reader reader,
                          int length) throws SQLException;

  void setDate(String parameterName, Date x, Calendar cal)
      throws SQLException;

  void setTime(String parameterName, Time x, Calendar cal)
      throws SQLException;

  void setTimestamp(String parameterName, Timestamp x, Calendar cal)
      throws SQLException;

  void setNull(String parameterName, int sqlType, String typeName)
      throws SQLException;

  String getString(String parameterName) throws SQLException;

  boolean getBoolean(String parameterName) throws SQLException;

  byte getByte(String parameterName) throws SQLException;

  short getShort(String parameterName) throws SQLException;

  int getInt(String parameterName) throws SQLException;

  long getLong(String parameterName) throws SQLException;

  float getFloat(String parameterName) throws SQLException;

  double getDouble(String parameterName) throws SQLException;

  byte[] getBytes(String parameterName) throws SQLException;

  Date getDate(String parameterName) throws SQLException;

  Time getTime(String parameterName) throws SQLException;

  Timestamp getTimestamp(String parameterName) throws SQLException;

  Object getObject(String parameterName) throws SQLException;

  BigDecimal getBigDecimal(String parameterName) throws SQLException;

  Object getObject(String parameterName, Map<String, Class<?>> map)
      throws SQLException;

  Ref getRef(String parameterName) throws SQLException;

  Blob getBlob(String parameterName) throws SQLException;

  Clob getClob(String parameterName) throws SQLException;

  Array getArray(String parameterName) throws SQLException;

  Date getDate(String parameterName, Calendar cal)
      throws SQLException;

  Time getTime(String parameterName, Calendar cal)
      throws SQLException;

  Timestamp getTimestamp(String parameterName, Calendar cal)
      throws SQLException;

  URL getURL(String parameterName) throws SQLException;

  RowId getRowId(int parameterIndex) throws SQLException;

  RowId getRowId(String parameterName) throws SQLException;

  void setRowId(String parameterName, RowId x) throws SQLException;

  void setNString(String parameterName, String value)
      throws SQLException;

  void setNCharacterStream(String parameterName, Reader value, long length)
      throws SQLException;

  void setNClob(String parameterName, NClob value) throws SQLException;

  void setClob(String parameterName, Reader reader, long length)
      throws SQLException;

  void setBlob(String parameterName, InputStream inputStream, long length)
      throws SQLException;

  void setNClob(String parameterName, Reader reader, long length)
      throws SQLException;

  NClob getNClob(int parameterIndex) throws SQLException;


  NClob getNClob(String parameterName) throws SQLException;

  void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException;

  SQLXML getSQLXML(int parameterIndex) throws SQLException;

  SQLXML getSQLXML(String parameterName) throws SQLException;

  String getNString(int parameterIndex) throws SQLException;


  String getNString(String parameterName) throws SQLException;

  Reader getNCharacterStream(int parameterIndex) throws SQLException;

  Reader getNCharacterStream(String parameterName) throws SQLException;

  Reader getCharacterStream(int parameterIndex) throws SQLException;

  Reader getCharacterStream(String parameterName) throws SQLException;

  void setBlob(String parameterName, Blob x) throws SQLException;

  void setClob(String parameterName, Clob x) throws SQLException;

  void setAsciiStream(String parameterName, InputStream x, long length)
      throws SQLException;

  void setBinaryStream(String parameterName, InputStream x,
                       long length) throws SQLException;

  void setCharacterStream(String parameterName,
                          Reader reader,
                          long length) throws SQLException;


  void setAsciiStream(String parameterName, InputStream x)
      throws SQLException;

  void setBinaryStream(String parameterName, InputStream x)
      throws SQLException;

  void setCharacterStream(String parameterName,
                          Reader reader) throws SQLException;

  void setNCharacterStream(String parameterName, Reader value) throws SQLException;

  void setClob(String parameterName, Reader reader)
      throws SQLException;

  void setBlob(String parameterName, InputStream inputStream)
      throws SQLException;

  void setNClob(String parameterName, Reader reader)
      throws SQLException;
}




       
