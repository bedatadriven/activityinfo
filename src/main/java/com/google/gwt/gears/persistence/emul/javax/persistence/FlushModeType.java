
package javax.persistence;


public enum FlushModeType {
	/**
	 * Flushing must occur only at transaction commit
	 */
	COMMIT,
	/**
	 * (Default) Flushing to occur at query execution
	 */
	AUTO
}
