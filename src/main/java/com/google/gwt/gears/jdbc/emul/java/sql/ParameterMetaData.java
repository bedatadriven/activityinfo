package java.sql;

public interface ParameterMetaData extends Wrapper {

  /**
   * Retrieves the number of parameters in the <code>PreparedStatement</code>
   * object for which this <code>ParameterMetaData</code> object contains
   * information.
   *
   * @return the number of parameters
   * @throws SQLException if a database access error occurs
   * @since 1.4
   */
  int getParameterCount() throws SQLException;

  /**
   * Retrieves whether null values are allowed in the designated parameter.
   *
   * @param param the first parameter is 1, the second is 2, ...
   * @return the nullability status of the given parameter; one of
   *         <code>ParameterMetaData.parameterNoNulls</code>,
   *         <code>ParameterMetaData.parameterNullable</code>, or
   *         <code>ParameterMetaData.parameterNullableUnknown</code>
   * @throws SQLException if a database access error occurs
   * @since 1.4
   */
  int isNullable(int param) throws SQLException;

  /**
   * The constant indicating that a
   * parameter will not allow <code>NULL</code> values.
   */
  int parameterNoNulls = 0;

  /**
   * The constant indicating that a
   * parameter will allow <code>NULL</code> values.
   */
  int parameterNullable = 1;

  /**
   * The constant indicating that the
   * nullability of a parameter is unknown.
   */
  int parameterNullableUnknown = 2;

  /**
   * Retrieves whether values for the designated parameter can be signed numbers.
   *
   * @param param the first parameter is 1, the second is 2, ...
   * @return <code>true</code> if so; <code>false</code> otherwise
   * @throws SQLException if a database access error occurs
   * @since 1.4
   */
  boolean isSigned(int param) throws SQLException;

  /**
   * Retrieves the designated parameter's specified column size.
   * <p/>
   * <P>The returned value represents the maximum column size for the given parameter.
   * For numeric data, this is the maximum precision.  For character data, this is the length in characters.
   * For datetime datatypes, this is the length in characters of the String representation (assuming the
   * maximum allowed precision of the fractional seconds component). For binary data, this is the length in bytes.  For the ROWID datatype,
   * this is the length in bytes. 0 is returned for data types where the
   * column size is not applicable.
   *
   * @param param the first parameter is 1, the second is 2, ...
   * @return precision
   * @throws SQLException if a database access error occurs
   * @since 1.4
   */
  int getPrecision(int param) throws SQLException;

  /**
   * Retrieves the designated parameter's number of digits to right of the decimal point.
   * 0 is returned for data types where the scale is not applicable.
   *
   * @param param the first parameter is 1, the second is 2, ...
   * @return scale
   * @throws SQLException if a database access error occurs
   * @since 1.4
   */
  int getScale(int param) throws SQLException;

  /**
   * Retrieves the designated parameter's SQL type.
   *
   * @param param the first parameter is 1, the second is 2, ...
   * @return SQL type from <code>java.sql.Types</code>
   * @throws SQLException if a database access error occurs
   * @see Types
   * @since 1.4
   */
  int getParameterType(int param) throws SQLException;

  /**
   * Retrieves the designated parameter's database-specific type name.
   *
   * @param param the first parameter is 1, the second is 2, ...
   * @return type the name used by the database. If the parameter type is
   *         a user-defined type, then a fully-qualified type name is returned.
   * @throws SQLException if a database access error occurs
   * @since 1.4
   */
  String getParameterTypeName(int param) throws SQLException;


  /**
   * Retrieves the fully-qualified name of the Java class whose instances
   * should be passed to the method <code>PreparedStatement.setObject</code>.
   *
   * @param param the first parameter is 1, the second is 2, ...
   * @return the fully-qualified name of the class in the Java programming
   *         language that would be used by the method
   *         <code>PreparedStatement.setObject</code> to set the value
   *         in the specified parameter. This is the class name used
   *         for custom mapping.
   * @throws SQLException if a database access error occurs
   * @since 1.4
   */
  String getParameterClassName(int param) throws SQLException;

  /**
   * The constant indicating that the mode of the parameter is unknown.
   */
  int parameterModeUnknown = 0;

  /**
   * The constant indicating that the parameter's mode is IN.
   */
  int parameterModeIn = 1;

  /**
   * The constant indicating that the parameter's mode is INOUT.
   */
  int parameterModeInOut = 2;

  /**
   * The constant indicating that the parameter's mode is  OUT.
   */
  int parameterModeOut = 4;

  /**
   * Retrieves the designated parameter's mode.
   *
   * @param param the first parameter is 1, the second is 2, ...
   * @return mode of the parameter; one of
   *         <code>ParameterMetaData.parameterModeIn</code>,
   *         <code>ParameterMetaData.parameterModeOut</code>, or
   *         <code>ParameterMetaData.parameterModeInOut</code>
   *         <code>ParameterMetaData.parameterModeUnknown</code>.
   * @throws SQLException if a database access error occurs
   * @since 1.4
   */
  int getParameterMode(int param) throws SQLException;
}
