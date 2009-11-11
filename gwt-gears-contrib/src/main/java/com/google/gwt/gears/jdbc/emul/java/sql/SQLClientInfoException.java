package java.sql;

import java.util.Map;

public class SQLClientInfoException extends SQLException {


  private Map<String, ClientInfoStatus> failedProperties;

  /**
   * Constructs a <code>SQLClientInfoException</code>  Object.
   * The <code>reason</code>,
   * <code>SQLState</code>, and failedProperties list are initialized to
   * <code> null</code> and the vendor code is initialized to 0.
   * The <code>cause</code> is not initialized, and may subsequently be
   * initialized by a call to the
   * {@link Throwable#initCause(Throwable)} method.
   * <p/>
   *
   * @since 1.6
   */
  public SQLClientInfoException() {

    this.failedProperties = null;
  }

  /**
   * Constructs a <code>SQLClientInfoException</code> object initialized with a
   * given <code>failedProperties</code>.
   * The <code>reason</code> and <code>SQLState</code> are initialized
   * to <code>null</code> and the vendor code is initialized to 0.
   * <p/>
   * The <code>cause</code> is not initialized, and may subsequently be
   * initialized by a call to the
   * {@link Throwable#initCause(Throwable)} method.
   * <p/>
   *
   * @param failedProperties A Map containing the property values that could not
   *                         be set.  The keys in the Map
   *                         contain the names of the client info
   *                         properties that could not be set and
   *                         the values contain one of the reason codes
   *                         defined in <code>ClientInfoStatus</code>
   *                         <p/>
   * @since 1.6
   */
  public SQLClientInfoException(Map<String, ClientInfoStatus> failedProperties) {

    this.failedProperties = failedProperties;
  }

  /**
   * Constructs a <code>SQLClientInfoException</code> object initialized with
   * a given <code>cause</code> and <code>failedProperties</code>.
   * <p/>
   * The <code>reason</code>  is initialized to <code>null</code> if
   * <code>cause==null</code> or to <code>cause.toString()</code> if
   * <code>cause!=null</code> and the vendor code is initialized to 0.
   * <p/>
   * <p/>
   *
   * @param failedProperties A Map containing the property values that could not
   *                         be set.  The keys in the Map
   *                         contain the names of the client info
   *                         properties that could not be set and
   *                         the values contain one of the reason codes
   *                         defined in <code>ClientInfoStatus</code>
   * @param cause            the (which is saved for later retrieval by the <code>getCause()</code> method); may be null indicating
   *                         the cause is non-existent or unknown.
   *                         <p/>
   * @since 1.6
   */
  public SQLClientInfoException(Map<String, ClientInfoStatus> failedProperties,
                                Throwable cause) {

    super(cause != null ? cause.toString() : null);
    initCause(cause);
    this.failedProperties = failedProperties;
  }

  /**
   * Constructs a <code>SQLClientInfoException</code> object initialized with a
   * given <code>reason</code> and <code>failedProperties</code>.
   * The <code>SQLState</code> is initialized
   * to <code>null</code> and the vendor code is initialized to 0.
   * <p/>
   * The <code>cause</code> is not initialized, and may subsequently be
   * initialized by a call to the
   * {@link Throwable#initCause(Throwable)} method.
   * <p/>
   *
   * @param reason           a description of the exception
   * @param failedProperties A Map containing the property values that could not
   *                         be set.  The keys in the Map
   *                         contain the names of the client info
   *                         properties that could not be set and
   *                         the values contain one of the reason codes
   *                         defined in <code>ClientInfoStatus</code>
   *                         <p/>
   * @since 1.6
   */
  public SQLClientInfoException(String reason,
                                Map<String, ClientInfoStatus> failedProperties) {

    super(reason);
    this.failedProperties = failedProperties;
  }

  /**
   * Constructs a <code>SQLClientInfoException</code> object initialized with a
   * given <code>reason</code>, <code>cause</code> and
   * <code>failedProperties</code>.
   * The  <code>SQLState</code> is initialized
   * to <code>null</code> and the vendor code is initialized to 0.
   * <p/>
   *
   * @param reason           a description of the exception
   * @param failedProperties A Map containing the property values that could not
   *                         be set.  The keys in the Map
   *                         contain the names of the client info
   *                         properties that could not be set and
   *                         the values contain one of the reason codes
   *                         defined in <code>ClientInfoStatus</code>
   * @param cause            the underlying reason for this <code>SQLException</code> (which is saved for later retrieval by the <code>getCause()</code> method); may be null indicating
   *                         the cause is non-existent or unknown.
   *                         <p/>
   * @since 1.6
   */
  public SQLClientInfoException(String reason,
                                Map<String, ClientInfoStatus> failedProperties,
                                Throwable cause) {

    super(reason);
    initCause(cause);
    this.failedProperties = failedProperties;
  }

  /**
   * Constructs a <code>SQLClientInfoException</code> object initialized with a
   * given  <code>reason</code>, <code>SQLState</code>  and
   * <code>failedProperties</code>.
   * The <code>cause</code> is not initialized, and may subsequently be
   * initialized by a call to the
   * {@link Throwable#initCause(Throwable)} method. The vendor code
   * is initialized to 0.
   * <p/>
   *
   * @param reason           a description of the exception
   * @param SQLState         an XOPEN or SQL:2003 code identifying the exception
   * @param failedProperties A Map containing the property values that could not
   *                         be set.  The keys in the Map
   *                         contain the names of the client info
   *                         properties that could not be set and
   *                         the values contain one of the reason codes
   *                         defined in <code>ClientInfoStatus</code>
   *                         <p/>
   * @since 1.6
   */
  public SQLClientInfoException(String reason,
                                String SQLState,
                                Map<String, ClientInfoStatus> failedProperties) {

    super(reason, SQLState);
    this.failedProperties = failedProperties;
  }

  /**
   * Constructs a <code>SQLClientInfoException</code> object initialized with a
   * given  <code>reason</code>, <code>SQLState</code>, <code>cause</code>
   * and <code>failedProperties</code>.  The vendor code is initialized to 0.
   * <p/>
   *
   * @param reason           a description of the exception
   * @param SQLState         an XOPEN or SQL:2003 code identifying the exception
   * @param failedProperties A Map containing the property values that could not
   *                         be set.  The keys in the Map
   *                         contain the names of the client info
   *                         properties that could not be set and
   *                         the values contain one of the reason codes
   *                         defined in <code>ClientInfoStatus</code>
   * @param cause            the underlying reason for this <code>SQLException</code> (which is saved for later retrieval by the <code>getCause()</code> method); may be null indicating
   *                         the cause is non-existent or unknown.
   *                         <p/>
   * @since 1.6
   */
  public SQLClientInfoException(String reason,
                                String SQLState,
                                Map<String, ClientInfoStatus> failedProperties,
                                Throwable cause) {

    super(reason, SQLState);
    initCause(cause);
    this.failedProperties = failedProperties;
  }

  /**
   * Constructs a <code>SQLClientInfoException</code> object initialized with a
   * given  <code>reason</code>, <code>SQLState</code>,
   * <code>vendorCode</code>  and <code>failedProperties</code>.
   * The <code>cause</code> is not initialized, and may subsequently be
   * initialized by a call to the
   * {@link Throwable#initCause(Throwable)} method.
   * <p/>
   *
   * @param reason           a description of the exception
   * @param SQLState         an XOPEN or SQL:2003 code identifying the exception
   * @param vendorCode       a database vendor-specific exception code
   * @param failedProperties A Map containing the property values that could not
   *                         be set.  The keys in the Map
   *                         contain the names of the client info
   *                         properties that could not be set and
   *                         the values contain one of the reason codes
   *                         defined in <code>ClientInfoStatus</code>
   *                         <p/>
   * @since 1.6
   */
  public SQLClientInfoException(String reason,
                                String SQLState,
                                int vendorCode,
                                Map<String, ClientInfoStatus> failedProperties) {

    super(reason, SQLState, vendorCode);
    this.failedProperties = failedProperties;
  }

  /**
   * Constructs a <code>SQLClientInfoException</code> object initialized with a
   * given  <code>reason</code>, <code>SQLState</code>,
   * <code>cause</code>, <code>vendorCode</code> and
   * <code>failedProperties</code>.
   * <p/>
   *
   * @param reason           a description of the exception
   * @param SQLState         an XOPEN or SQL:2003 code identifying the exception
   * @param vendorCode       a database vendor-specific exception code
   * @param failedProperties A Map containing the property values that could not
   *                         be set.  The keys in the Map
   *                         contain the names of the client info
   *                         properties that could not be set and
   *                         the values contain one of the reason codes
   *                         defined in <code>ClientInfoStatus</code>
   * @param cause            the underlying reason for this <code>SQLException</code> (which is saved for later retrieval by the <code>getCause()</code> method); may be null indicating
   *                         the cause is non-existent or unknown.
   *                         <p/>
   * @since 1.6
   */
  public SQLClientInfoException(String reason,
                                String SQLState,
                                int vendorCode,
                                Map<String, ClientInfoStatus> failedProperties,
                                Throwable cause) {

    super(reason, SQLState, vendorCode);
    initCause(cause);
    this.failedProperties = failedProperties;
  }

  /**
   * Returns the list of client info properties that could not be set.  The
   * keys in the Map  contain the names of the client info
   * properties that could not be set and the values contain one of the
   * reason codes defined in <code>ClientInfoStatus</code>
   * <p/>
   *
   * @return Map list containing the client info properties that could
   *         not be set
   *         <p/>
   * @since 1.6
   */
  public Map<String, ClientInfoStatus> getFailedProperties() {

    return this.failedProperties;
  }

  private static final long serialVersionUID = -4319604256824655880L;
}
