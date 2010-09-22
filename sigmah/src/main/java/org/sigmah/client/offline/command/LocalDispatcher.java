/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.command;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.handler.GetSchemaHandler;
import org.sigmah.shared.command.handler.GetSitesHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.exception.CommandException;

/**
 * Dispatches commands to local handlers
 */
public class LocalDispatcher implements Dispatcher {
    private final Authentication auth;
    private final Provider<GetSchemaHandler> getSchemaHandler;
    private final Provider<GetSitesHandler> getSitesHandler;

    @Inject
    public LocalDispatcher(
            Provider<GetSchemaHandler> getSchemaHandler,
            Provider<GetSitesHandler> getSitesHandler,
            Authentication auth) {
        this.auth = auth;
        this.getSchemaHandler = getSchemaHandler;
        this.getSitesHandler = getSitesHandler;
    }

    @Override
    public <T extends CommandResult> void execute(Command<T> command, AsyncMonitor monitor, AsyncCallback<T> callback) {
        // pass a dummy user to the command
        User user =  new User();
        user.setId(auth.getUserId());
        user.setEmail(auth.getEmail());

        if(monitor != null) {
            monitor.beforeRequest();
        }

        CommandHandler<Command<?>> handler = getHandler(command);
        try {
            CommandResult result = handler.execute(command, user);
            Log.debug("Command success");
            if(monitor!=null) {
                monitor.onCompleted();
            }
            callback.onSuccess((T)result);
        } catch (CommandException e) {
            Log.debug("Command failure");
            if(monitor!=null) {
                monitor.onCompleted();
            }
            callback.onFailure(e);
        }

    }


    private <T extends CommandHandler> T getHandler(Command c) {
        // TODO enable more offline schema handlers
        if (c instanceof GetSchema) {
            return (T) getSchemaHandler.get();
        } else if (c instanceof GetSites) {
            return (T) getSitesHandler.get();
        } else {
            throw new IllegalArgumentException("No handler class for " + c);
        }
    }

}
