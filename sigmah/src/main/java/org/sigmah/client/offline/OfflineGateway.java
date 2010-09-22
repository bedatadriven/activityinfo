/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Decouples the actual offline implementation from the manager
 * so we can stick all the offline code in a separate js split.
 */
public interface OfflineGateway {
    void install(AsyncCallback<Void> callback);
    void goOffline(AsyncCallback<Void> callback);
    void goOnline(AsyncCallback<Void> callback);
}
