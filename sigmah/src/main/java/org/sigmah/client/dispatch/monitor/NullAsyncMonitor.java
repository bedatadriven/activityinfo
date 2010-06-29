package org.sigmah.client.dispatch.monitor;

import org.sigmah.client.dispatch.AsyncMonitor;

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
