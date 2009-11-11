package java.sql;

import java.util.*;

public enum ClientInfoStatus {

  /**
   * The client info property could not be set for some unknown reason
   *
   * @since 1.6
   */
  REASON_UNKNOWN,

  /**
   * The client info property name specified was not a recognized property
   * name.
   *
   * @since 1.6
   */
  REASON_UNKNOWN_PROPERTY,

  /**
   * The value specified for the client info property was not valid.
   *
   * @since 1.6
   */
  REASON_VALUE_INVALID,

  /**
   * The value specified for the client info property was too large.
   *
   * @since 1.6
   */
  REASON_VALUE_TRUNCATED
}
