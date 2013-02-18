/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch.monitor;

import org.activityinfo.client.dispatch.AsyncMonitor;

/**
 * An <code>AsyncMonitor</code> that does nothing, and does not allow retries.
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class NullAsyncMonitor implements AsyncMonitor {

    @Override
    public void beforeRequest() {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onConnectionProblem() {

    }

    @Override
    public boolean onRetrying() {
        return false;
    }

    @Override
    public void onServerError() {

    }
}
