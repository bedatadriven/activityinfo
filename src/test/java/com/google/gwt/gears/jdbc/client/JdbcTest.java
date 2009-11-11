package com.google.gwt.gears.jdbc.client;

import com.google.gwt.junit.client.GWTTestCase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class JdbcTest extends GWTTestCase {

  /**
   * Must refer to a valid module that sources this class.
   */
  public String getModuleName() {
    return "com.google.gwt.gears.jdbc.Jdbc";
  }


  public void testStatement() throws Throwable {

    Connection conn = DriverManager.getConnection("testStatement");

    Statement stmt = conn.createStatement();
    stmt.executeUpdate("drop table if exists TestStatementTable");
    int rowsAffected = stmt.executeUpdate("create table if not exists TestStatementTable (id integer primary key, " +
        "name text)");

    assertEquals("row count should be zero for DDL statements", 0, rowsAffected);
    stmt.close();

    for (int i = 0; i != 2; i++) {
      stmt = conn.createStatement();
      rowsAffected = stmt.executeUpdate("insert into TestStatementTable (id, name) values(" + i + ", 'foobar')");
      assertEquals("insert statement rows affected count", 1, rowsAffected);
      stmt.close();
    }

    stmt = conn.createStatement();
    rowsAffected = stmt.executeUpdate("delete from TestStatementTable where 1=1");
    assertEquals("deleted statement rows affected", 2, rowsAffected);
    stmt.close();

    conn.close();
  }

  public void testResultSet() throws Throwable {

    Connection conn = DriverManager.getConnection("testResultSet");
    Statement stmt = conn.createStatement();

    stmt.executeUpdate("drop table if exists ResultSetTest");
    stmt.executeUpdate("create table ResultSetTest (" +
        "integer_col integer, " +
        "text_col text, " +
        "real_col real)");
    stmt.executeUpdate("insert into ResultSetTest (integer_col, text_col, real_col) values (1, \"two\",  34.56) ");
    stmt.executeUpdate("insert into ResultSetTest (integer_col, text_col, real_col) values (7, \"eight\", 9.1112) ");

    ResultSet rs = stmt.executeQuery("select integer_col, text_col, real_col from ResultSetTest order by integer_col");
    assertEquals("integer_col index", 1, rs.findColumn("integer_col"));
    assertEquals("text_col index", 2, rs.findColumn("text_col"));
    assertEquals("real_col index", 3, rs.findColumn("real_col"));

    int rowsRead = 0;
    while (rs.next()) {
      rowsRead++;
      if (rowsRead == 1) {
        assertEquals(1, rs.getInt(1));
        assertEquals("two", rs.getString(2));
        assertEquals(34.56, rs.getDouble(3), 0.001);
      } else if (rowsRead == 2) {
        assertEquals(7, rs.getInt(1));
      }
    }
    rs.close();

    assertEquals("rows Read", 2, rowsRead);
    conn.close();
  }

  public void testWasNull() throws Throwable {

    Connection conn = DriverManager.getConnection("testWasNull");
    Statement stmt = conn.createStatement();

    stmt.executeUpdate("drop table if exists ResultSetTest");
    stmt.executeUpdate("create table ResultSetTest (name text, frequency integer)");
    stmt.executeUpdate("insert into ResultSetTest (name) values (\"bob\") ");

    ResultSet rs = stmt.executeQuery("select frequency from ResultSetTest");
    assertTrue(rs.next());
    rs.getInt(1);
    assertTrue(rs.wasNull());

    rs.close();
    conn.close();
  }


  public void testNullParam() throws Throwable {

    Connection conn = DriverManager.getConnection("testNullParam");
    Statement stmt = conn.createStatement();

    stmt.executeUpdate("drop table if exists ResultSetTest");
    stmt.executeUpdate("create table ResultSetTest (name text, frequency integer)");

    PreparedStatement pstmt = conn.prepareStatement("insert into ResultSetTest (name, frequency) values (?, ?) ");
    pstmt.setString(1, "bob's your uncle");
    pstmt.setNull(2, 0);
    pstmt.executeUpdate();

    ResultSet rs = stmt.executeQuery("select frequency from ResultSetTest");
    assertTrue(rs.next());
    rs.getInt(1);
    assertTrue(rs.wasNull());

    rs.close();
    conn.close();
  }


  public void testPreparedStatement() throws Throwable {

    Connection conn = DriverManager.getConnection("testPreparedStatement");
    Statement stmt = conn.createStatement();
    stmt.executeUpdate("drop table if exists PrepTable");
    stmt.executeUpdate("create table PrepTable (blue integer, red text)");
    stmt.close();

    PreparedStatement pstmt = conn.prepareStatement("insert into PrepTable (blue, red) values (?, ?)");
    pstmt.setString(2, "dog");
    pstmt.setInt(1, 42);
    int rowsAffected = pstmt.executeUpdate();

    assertEquals("rows affected by insert", 1, rowsAffected);
    pstmt.close();

    pstmt = conn.prepareStatement("select blue, red from PrepTable where red = ?");
    pstmt.setString(1, "dog");

    ResultSet rs = pstmt.executeQuery();

    assertTrue("row is valid", rs.next());
    assertEquals(42, rs.getInt(1));
    assertEquals("dog", rs.getString(2));

  }
}
