
package java.sql;

import java.util.Iterator;

public class SQLException extends Exception
    implements Iterable<Throwable> {

  /**
   * Constructs a <code>SQLException</code> object with a given
   * <code>reason</code>, <code>SQLState</code>  and
   * <code>vendorCode</code>.
   * <p/>
   * The <code>cause</code> is not initialized, and may subsequently be
   * initialized by a call to the
   * {@link Throwable#initCause(Throwable)} method.
   * <p/>
   *
   * @param reason     a description of the exception
   * @param SQLState   an XOPEN or SQL:2003 code identifying the exception
   * @param vendorCode a database vendor-specific exception code
   */
  public SQLException(String reason, String SQLState, int vendorCode) {
    super(reason);
    this.SQLState = SQLState;
    this.vendorCode = vendorCode;
  }


  /**
   * Constructs a <code>SQLException</code> object with a given
   * <code>reason</code> and <code>SQLState</code>.
   * <p/>
   * The <code>cause</code> is not initialized, and may subsequently be
   * initialized by a call to the
   * {@link Throwable#initCause(Throwable)} method. The vendor code
   * is initialized to 0.
   * <p/>
   *
   * @param reason   a description of the exception
   * @param SQLState an XOPEN or SQL:2003 code identifying the exception
   */
  public SQLException(String reason, String SQLState) {
    super(reason);
    this.SQLState = SQLState;
    this.vendorCode = 0;

  }

  /**
   * Constructs a <code>SQLException</code> object with a given
   * <code>reason</code>. The  <code>SQLState</code>  is initialized to
   * <code>null</code> and the vender code is initialized to 0.
   * <p/>
   * The <code>cause</code> is not initialized, and may subsequently be
   * initialized by a call to the
   * {@link Throwable#initCause(Throwable)} method.
   * <p/>
   *
   * @param reason a description of the exception
   */
  public SQLException(String reason) {
    super(reason);
    this.SQLState = null;
    this.vendorCode = 0;

  }

  /**
   * Constructs a <code>SQLException</code> object.
   * The <code>reason</code>, <code>SQLState</code> are initialized
   * to <code>null</code> and the vendor code is initialized to 0.
   * <p/>
   * The <code>cause</code> is not initialized, and may subsequently be
   * initialized by a call to the
   * {@link Throwable#initCause(Throwable)} method.
   * <p/>
   */
  public SQLException() {
    super();
    this.SQLState = null;
    this.vendorCode = 0;

  }

  /**
   * Constructs a <code>SQLException</code> object with a given
   * <code>cause</code>.
   * The <code>SQLState</code> is initialized
   * to <code>null</code> and the vendor code is initialized to 0.
   * The <code>reason</code>  is initialized to <code>null</code> if
   * <code>cause==null</code> or to <code>cause.toString()</code> if
   * <code>cause!=null</code>.
   * <p/>
   *
   * @param cause the underlying reason for this <code>SQLException</code>
   *              (which is saved for later retrieval by the <code>getCause()</code> method);
   *              may be null indicating the cause is non-existent or unknown.
   * @since 1.6
   */
  public SQLException(Throwable cause) {
    super(cause);


  }

  /**
   * Constructs a <code>SQLException</code> object with a given
   * <code>reason</code> and  <code>cause</code>.
   * The <code>SQLState</code> is  initialized to <code>null</code>
   * and the vendor code is initialized to 0.
   * <p/>
   *
   * @param reason a description of the exception.
   * @param cause  the underlying reason for this <code>SQLException</code>
   *               (which is saved for later retrieval by the <code>getCause()</code> method);
   *               may be null indicating the cause is non-existent or unknown.
   * @since 1.6
   */
  public SQLException(String reason, Throwable cause) {
    super(reason, cause);

  }

  /**
   * Constructs a <code>SQLException</code> object with a given
   * <code>reason</code>, <code>SQLState</code> and  <code>cause</code>.
   * The vendor code is initialized to 0.
   * <p/>
   *
   * @param reason   a description of the exception.
   * @param sqlState an XOPEN or SQL:2003 code identifying the exception
   * @param cause    the underlying reason for this <code>SQLException</code>
   *                 (which is saved for later retrieval by the
   *                 <code>getCause()</code> method); may be null indicating
   *                 the cause is non-existent or unknown.
   * @since 1.6
   */
  public SQLException(String reason, String sqlState, Throwable cause) {
    super(reason, cause);

    this.SQLState = sqlState;
    this.vendorCode = 0;

  }

  /**
   * Constructs a <code>SQLException</code> object with a given
   * <code>reason</code>, <code>SQLState</code>, <code>vendorCode</code>
   * and  <code>cause</code>.
   * <p/>
   *
   * @param reason     a description of the exception
   * @param sqlState   an XOPEN or SQL:2003 code identifying the exception
   * @param vendorCode a database vendor-specific exception code
   * @param cause      the underlying reason for this <code>SQLException</code>
   *                   (which is saved for later retrieval by the <code>getCause()</code> method);
   *                   may be null indicating the cause is non-existent or unknown.
   * @since 1.6
   */
  public SQLException(String reason, String sqlState, int vendorCode, Throwable cause) {
    super(reason, cause);

    this.SQLState = sqlState;
    this.vendorCode = vendorCode;

  }

  /**
   * Retrieves the SQLState for this <code>SQLException</code> object.
   *
   * @return the SQLState value
   */
  public String getSQLState() {
    return (SQLState);
  }

  /**
   * Retrieves the vendor-specific exception code
   * for this <code>SQLException</code> object.
   *
   * @return the vendor's error code
   */
  public int getErrorCode() {
    return (vendorCode);
  }

  /**
   * Retrieves the exception chained to this
   * <code>SQLException</code> object by setNextException(SQLException ex).
   *
   * @return the next <code>SQLException</code> object in the chain;
   *         <code>null</code> if there are none
   * @see #setNextException
   */
  public SQLException getNextException() {
    return null; // not implemented, can remove?
  }

  /**
   * Adds an <code>SQLException</code> object to the end of the chain.
   *
   * @param ex the new exception that will be added to the end of
   *           the <code>SQLException</code> chain
   * @see #getNextException
   */
  public void setNextException(SQLException ex) {
    // not implementeed-- can remove?
  }

  /**
   * Returns an iterator over the chained SQLExceptions.  The iterator will
   * be used to iterate over each SQLException and its underlying cause
   * (if any).
   *
   * @return an iterator over the chained SQLExceptions and causes in the proper
   *         order
   * @since 1.6
   */
  public Iterator<Throwable> iterator() {
    // not implemented. can remove?
    return null;
  }

  /**
   * @serial
   */
  private String SQLState;

  /**
   * @serial
   */
  private int vendorCode;

}
