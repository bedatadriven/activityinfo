

package org.activityinfo.client.dispatch.remote.cache;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
