package java.sql;

public interface Savepoint {

  /**
   * Retrieves the generated ID for the savepoint that this
   * <code>Savepoint</code> object represents.
   *
   * @return the numeric ID of this savepoint
   * @throws SQLException if this is a named savepoint
   * @since 1.4
   */
  int getSavepointId() throws SQLException;

  /**
   * Retrieves the name of the savepoint that this <code>Savepoint</code>
   * object represents.
   *
   * @return the name of this savepoint
   * @throws SQLException if this is an un-named savepoint
   * @since 1.4
   */
  String getSavepointName() throws SQLException;
}


