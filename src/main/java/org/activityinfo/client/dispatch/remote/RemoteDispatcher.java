/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch.remote;

import java.util.Collections;
import java.util.List;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.local.sync.ServerStateChangeEvent;
import org.activityinfo.login.shared.AuthenticatedUser;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.MutatingCommand;
import org.activityinfo.shared.command.RemoteCommandServiceAsync;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * Dispatcher which sends individual commands to the server with no caching, batching,
 * or retrying.
 */
public class RemoteDispatcher extends AbstractDispatcher {
    private final AuthenticatedUser auth;
    private final RemoteCommandServiceAsync service;
	private EventBus eventBus;

    @Inject
    public RemoteDispatcher(EventBus eventBus, AuthenticatedUser auth, RemoteCommandServiceAsync service) {
    	this.eventBus = eventBus;
    	this.auth = auth;
        this.service = service;
    }

    @Override
    public <T extends CommandResult> void execute(final Command<T> command, final AsyncCallback<T> callback) {
        try {
        	final long timeStarted = System.currentTimeMillis();
	    	service.execute(auth.getAuthToken(), Collections.singletonList((Command)command), new AsyncCallback<List<CommandResult>>() {
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
        } catch(Exception e) {
        	// catch client-side serialization exceptions
        	callback.onFailure(e);
        }
    }
}
