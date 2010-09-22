/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.sigmah.client.dispatch.remote.RemoteDispatcher;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;

/**
 *  Contextual object that holds the current reference to concrete Dispatcher strategy object.
 *
 *  Defaults to the CachingDispatcher
 *
 */
@Singleton
public class SwitchingDispatcher implements Dispatcher {
	private final RemoteDispatcher remoteDispatcher;
    private Dispatcher currentStrategy;

                           
    @Inject
	public SwitchingDispatcher(RemoteDispatcher remoteDispatcher) {
		this.remoteDispatcher = remoteDispatcher;
        this.currentStrategy = remoteDispatcher;
    }
	
	@Override
	public <T extends CommandResult> void execute(Command<T> command,
			AsyncMonitor monitor, AsyncCallback<T> callback) {

        currentStrategy.execute(command, monitor, callback);
	}


    /**
     * Sets the current dispatching strategy to the given
     * local dispatcher
     */
    public void setLocalDispatcher(Dispatcher dispatcher) {
        currentStrategy = dispatcher;
    }

    /**
     * Clears any local dispatcher previously set and sets the
     * current strategy to the default remote dispatcher
     */
    public void clearLocalDispatcher() {
        currentStrategy = remoteDispatcher;
    }

}
