

package org.activityinfo.client.dispatch;

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


/**
 * The <code>AsyncMonitor</code> provides a complement to the AsyncCallback
 * interface, allowing a division of responibility between application logic,
 * which is handled by classes implementing AsyncCallback, and which handle
 * command success or "expected" <code>CommandException</code> and the AsyncMonitor
 * which provides feedback to the user regarding progress, completion, connection
 * problems, general surver failures, and <code>UnexpectedCommandException</code>s
 */
public interface AsyncMonitor {

    /**
     * Called just before a request is sent to the server
     */
    void beforeRequest();

    /**
     * Called when the async command completes either sucessfully or
     * with any CommandException except UnexpectedCommandException
     */
    void onCompleted();

    /**
     * Called when the async command fails due to network problems.
     */
    void onConnectionProblem();

    /**
     * Called before the async command is retried following a connection failure
     *
     * @return true to allow the retry to continue, false to cancel
     */
    boolean onRetrying();

    /**
     * Called when the async commands fails due to a server error (not
     * just a connection problem)
     */
	void onServerError();
}
