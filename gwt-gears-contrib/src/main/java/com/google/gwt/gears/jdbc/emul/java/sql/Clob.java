package java.sql;

import java.io.Reader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

public interface Clob {
  long length() throws SQLException;
  String getSubString(long pos, int length) throws SQLException;
  Reader getCharacterStream() throws SQLException;
  InputStream getAsciiStream() throws SQLException;
  long position(String searchstr, long start) throws SQLException;
  long position(Clob searchstr, long start) throws SQLException;
  int setString(long pos, String str) throws SQLException;
  int setString(long pos, String str, int offset, int len) throws SQLException;
  OutputStream setAsciiStream(long pos) throws SQLException;
  Writer setCharacterStream(long pos) throws SQLException;
  void truncate(long len) throws SQLException;
  void free() throws SQLException;
  Reader getCharacterStream(long pos, long length) throws SQLException;

}

