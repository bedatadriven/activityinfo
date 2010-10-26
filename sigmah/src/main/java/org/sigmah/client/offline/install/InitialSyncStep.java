/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.install;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.sigmah.client.offline.sync.Synchronizer;

public class InitialSyncStep implements Step {

    private final Synchronizer syncro;

    @Inject
    public InitialSyncStep(Synchronizer syncro) {
        this.syncro = syncro;
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Initial Synchronization";
    }

    @Override
    public void execute(AsyncCallback<Void> callback) {
        syncro.clearDatabase();
        syncro.start(callback);
    }
}
