/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.local.sync;

import java.util.Date;

import org.activityinfo.shared.command.Command;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Decouples the actual offline implementation from the manager
 * so we can stick all the offline code in a separate js split.
 */
public interface Synchronizer {
    void install(AsyncCallback<Void> callback);

    /**
     * @return  the date of the last successful synchronization to the client
     */
    void getLastSyncTime(AsyncCallback<Date> callback);

    /**
     * Conducts sanity checks to be sure that we are really prepared to go
     * offline. (It's possible that the Offline "state" flag was mis-set in the past.)
     *
     * @return true if we are ready
     */
    void validateOfflineInstalled(AsyncCallback<Void> callback);
    
    void synchronize();
    
    // TODO: move to separate interface
    void execute(Command command, AsyncCallback callback);
    
}
