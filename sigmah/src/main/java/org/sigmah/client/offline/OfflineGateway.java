/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import java.util.Date;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.shared.command.Command;

import com.google.gwt.user.client.rpc.AsyncCallback;

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
    
    /**
     * Checks whether a command can be handled offline
     * 
     * @param command the command for which to check
     * @return true if the command can be handled offline
     */
    boolean canHandle(Command command);
    
    void execute(Command command, AsyncMonitor monitor, AsyncCallback callback);
    
}
