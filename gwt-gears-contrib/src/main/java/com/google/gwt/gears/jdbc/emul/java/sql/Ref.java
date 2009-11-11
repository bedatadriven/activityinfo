package java.sql;

import java.util.Map;

public interface Ref {

  /**
   * Retrieves the fully-qualified SQL name of the SQL structured type that
   * this <code>Ref</code> object references.
   *
   * @return the fully-qualified SQL name of the referenced SQL structured type
   * @throws SQLException if a database access error occurs
   * @throws SQLFeatureNotSupportedException
   *                      if the JDBC driver does not support
   *                      this method
   * @since 1.2
   */
  String getBaseTypeName() throws SQLException;

  /**
   * Retrieves the referenced object and maps it to a Java type
   * using the given type map.
   *
   * @param map a <code>java.util.Map</code> object that contains
   *            the mapping to use (the fully-qualified name of the SQL
   *            structured type being referenced and the class object for
   *            <code>SQLData</code> implementation to which the SQL
   *            structured type will be mapped)
   * @return a Java <code>Object</code> that is the custom mapping for
   *         the SQL structured type to which this <code>Ref</code>
   *         object refers
   * @throws SQLException if a database access error occurs
   * @throws SQLFeatureNotSupportedException
   *                      if the JDBC driver does not support
   *                      this method
   * @see #setObject
   * @since 1.4
   */
  Object getObject(Map<String, Class<?>> map) throws SQLException;


  /**
   * Retrieves the SQL structured type instance referenced by
   * this <code>Ref</code> object.  If the connection's type map has an entry
   * for the structured type, the instance will be custom mapped to
   * the Java class indicated in the type map.  Otherwise, the
   * structured type instance will be mapped to a <code>Struct</code> object.
   *
   * @return a Java <code>Object</code> that is the mapping for
   *         the SQL structured type to which this <code>Ref</code>
   *         object refers
   * @throws SQLException if a database access error occurs
   * @throws SQLFeatureNotSupportedException
   *                      if the JDBC driver does not support
   *                      this method
   * @see #setObject
   * @since 1.4
   */
  Object getObject() throws SQLException;

  /**
   * Sets the structured type value that this <code>Ref</code>
   * object references to the given instance of <code>Object</code>.
   * The driver converts this to an SQL structured type when it
   * sends it to the database.
   *
   * @param value an <code>Object</code> representing the SQL
   *              structured type instance that this
   *              <code>Ref</code> object will reference
   * @throws SQLException if a database access error occurs
   * @throws SQLFeatureNotSupportedException
   *                      if the JDBC driver does not support
   *                      this method
   * @see #getObject()
   * @see #getObject(java.util.Map)
   * @see PreparedStatement#setObject(int, Object)
   * @see CallableStatement#setObject(String, Object)
   * @since 1.4
   */
  void setObject(Object value) throws SQLException;

}


