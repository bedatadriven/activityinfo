/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;

/**
 * Decouples the actual offline implementation from the manager
 * so we can stick all the offline code in a separate js split.
 */
public interface OfflineGateway {
    void install(AsyncCallback<Void> callback);

    /**
     * @return  the date of the last successful synchronization to the client
     */
    Date getLastSyncTime();

    /**
     * Conducts sanity checks to be sure that we are really prepared to go
     * offline. (It's possible that the Offline "state" flag was mis-set in the past.)
     *
     * @return true if we are ready
     */
    boolean validateOfflineInstalled();

    void goOffline(AsyncCallback<Void> callback);
    void goOnline(AsyncCallback<Void> callback);
    void synchronize(AsyncCallback<Void> callback);
}
