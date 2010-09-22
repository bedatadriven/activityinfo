/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.RemoteCommandServiceAsync;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;

import java.util.ArrayList;
import java.util.List;

/**
 * Dispatcher which sends individual commands to the server with no caching, batching,
 * or retrying.
 */
public class DirectDispatcher implements Dispatcher {
    private final Authentication auth;
    private final RemoteCommandServiceAsync service;

    @Inject
    public DirectDispatcher(Authentication auth, RemoteCommandServiceAsync service) {
        this.auth = auth;
        this.service = service;
    }

    @Override
    public <T extends CommandResult> void execute(Command<T> command, AsyncMonitor monitor, final AsyncCallback<T> callback) {
        service.execute(auth.getAuthToken(), singletonArrayList(command), new AsyncCallback<List<CommandResult>>() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(List<CommandResult> commandResults) {
                CommandResult result = commandResults.get(0);
                if(result instanceof CommandException) {
                    callback.onFailure((CommandException) result);
                } else {
                    callback.onSuccess((T) result);
                }
            }
        });

    }

    private List<Command> singletonArrayList(Command command) {
        ArrayList<Command> list = new ArrayList<Command>();
        list.add(command);
        return list;
    }
}
