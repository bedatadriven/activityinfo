package org.activityinfo.client.page.base;

import org.activityinfo.client.command.monitor.AsyncMonitor;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface SaveChangesCallback {

    public void save(AsyncMonitor monitor);

    public void cancel();

    public void discard();

}
