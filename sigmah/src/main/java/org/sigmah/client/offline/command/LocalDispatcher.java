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
import org.sigmah.shared.command.handler.CommandContext;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.handler.CommandHandlerAsync;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * Dispatches commands to local handlers
 */
public class LocalDispatcher implements Dispatcher, CommandContext {
    private final Authentication auth;
    private final Map<Class, CommandHandlerAsync> registry = new HashMap<Class, CommandHandlerAsync>();
    
    @Inject
    public LocalDispatcher(Authentication auth) {
        this.auth = auth;
    }

    public <C extends Command<R>, R extends CommandResult> void registerHandler(Class<C> commandClass, CommandHandlerAsync<C,R> handler) {
        registry.put(commandClass, handler);
    }

    @Override
    public <R extends CommandResult> void execute(Command<R> command, final AsyncMonitor monitor, final AsyncCallback<R> callback) {
       	    	
        if(monitor != null) {
            monitor.beforeRequest();
        }
        try {
            CommandHandlerAsync handler = getHandler(command);
            handler.execute(command, null, new AsyncCallback<R>() {

				@Override
				public void onFailure(Throwable caught) {
					try {
			            if(monitor!=null) {
			                monitor.onServerError();
			            }
		            } catch(Throwable ignored) {
		            }
		            callback.onFailure(caught);
				}

				@Override
				public void onSuccess(R result) {
		            Log.debug("Command success");
					if(monitor!=null) {
						monitor.onCompleted();
					}
					callback.onSuccess(result);					
				}
			});

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
    	CommandHandlerAsync handler = getHandler(c);
    	if(handler instanceof PartialCommandHandler) {
    		return ((PartialCommandHandler) handler).canExecute(c);
    	} else {
    		return true;
    	}
    	
    }
    
    private <C extends Command<R>,R extends CommandResult> CommandHandlerAsync<C,R> getHandler(C c) {
        CommandHandlerAsync<C,R> handler = registry.get(c.getClass());
        if(handler == null) {
            throw new IllegalArgumentException("No handler class for " + c.toString());
        }

        return handler;
    }

	@Override
	public User getUser() {
        User user =  new User();
        user.setId(auth.getUserId());
        user.setEmail(auth.getEmail());
        return user;
	}
    
}
