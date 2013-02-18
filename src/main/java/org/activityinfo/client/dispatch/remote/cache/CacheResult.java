/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch.remote.cache;

import org.activityinfo.shared.command.result.CommandResult;

/**
 * Encapsulates the return value of call to a {@link org.activityinfo.client.dispatch.CommandCache}.
 *
 * @author Alex Bertram
 */
public class CacheResult<T extends CommandResult> {

    /**
     * True if the handler was able to execute the command locally
     */
    private final boolean couldExecute;

    /**
     * The result of the local execution. N.B.: <code>null</code> is a legitimate
     * return value of a {@link org.activityinfo.shared.command.Command} so be sure to check the
     * value of <code>couldExecute</code>
     */
    private final T result;

    private static CacheResult failed = new CacheResult();

    private CacheResult() {
        couldExecute = false;
        result = null;
    }

    /**
     * Creates a return value indicating that local excecution was
     * successful
     *
     * @param result The result of the local execution (can be null)
     */
    public CacheResult(T result) {
        this.couldExecute = true;
        this.result = result;
    }

    /**
     * Creates a return value indicating that local execution was not possible.
     *
     * @return A {@link org.activityinfo.client.dispatch.CommandCache} return value indicating that
     *         local execution was not possible.
     */
    public static CacheResult couldNotExecute() {
        return failed;
    }

	public T getResult() {
		return result;
	}

	public boolean isCouldExecute() {
		return couldExecute;
	}
}
