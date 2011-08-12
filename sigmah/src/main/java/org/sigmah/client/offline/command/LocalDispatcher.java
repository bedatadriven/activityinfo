/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.command;

import java.util.HashMap;
import java.util.Map;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.offline.command.handler.PartialCommandHandler;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.exception.CommandException;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * Dispatches commands to local handlers
 */
public class LocalDispatcher implements Dispatcher {
    private final Authentication auth;
    private final Map<Class, CommandHandler> registry = new HashMap<Class, CommandHandler>();

    
    @Inject
    public LocalDispatcher(Authentication auth) {
        this.auth = auth;
    }

    public <T extends Command> void registerHandler(Class<T> commandClass, CommandHandler<T> handler) {
        registry.put(commandClass, handler);
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
        try {
            CommandHandler handler = getHandler(command);
            CommandResult result = handler.execute(command, user);
            Log.debug("Command success");
            if(monitor!=null) {
                monitor.onCompleted();
            }
            callback.onSuccess((T)result);
        } catch (Throwable e) {
            Log.debug("Command failure: ", e);
            try {
	            if(monitor!=null) {
	                monitor.onServerError();
	            }
            } catch(Throwable ignored) {
            }
            callback.onFailure(e);
        }
    }
    
    public boolean canExecute(Command c) {
    	if(!registry.containsKey(c.getClass())) {
    		return false;
    	}
    	// some commands may only be partially available online
    	CommandHandler handler = getHandler(c);
    	if(handler instanceof PartialCommandHandler) {
    		return ((PartialCommandHandler) handler).canExecute(c);
    	} else {
    		return true;
    	}
    	
    }
    
    private CommandHandler getHandler(Command c) {
        CommandHandler handler = registry.get(c.getClass());
        if(handler == null) {
            throw new IllegalArgumentException("No handler class for " + c.toString());
        }

        return handler;
    }
}
