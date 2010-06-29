/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.sync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface Step {
    boolean isComplete();
    String getDescription();
    void execute(AsyncCallback<Void> callback);
}
