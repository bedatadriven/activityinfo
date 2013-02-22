
package org.activityinfo.client.dispatch.remote;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import java.util.Collections;
import java.util.List;

import org.activityinfo.client.EventBus;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.Command;
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
