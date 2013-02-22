package org.activityinfo.client.local.sync;

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

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.remote.AbstractDispatcher;
import org.activityinfo.client.dispatch.remote.Remote;
import org.activityinfo.client.dispatch.remote.RemoteDispatcher;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.inject.Inject;

/**
 * Dispatcher implementation used by the synchronizer classes that 
 * executes commands directly without caching, and retries aggressively.
 */
public class SynchronizerDispatcher extends AbstractDispatcher {
	
	private final EventBus eventBus;
	private final Dispatcher remoteDispatcher;
	
	private static final int MAX_RETRY_COUNT = 16;

	@Inject
	public SynchronizerDispatcher(EventBus eventBus, @Remote Dispatcher remoteDispatcher) {
		super();
		this.eventBus = eventBus;
		this.remoteDispatcher = remoteDispatcher;
	}

	@Override
	public <T extends CommandResult> void execute(Command<T> command,
			AsyncCallback<T> callback) {

		tryExecute(command, callback, 0);
		
	}

	private final <T extends CommandResult> void tryExecute(final Command<T> command, final AsyncCallback<T> callback, final int attempt) {
		remoteDispatcher.execute(command, null, new AsyncCallback<T>() {

			@Override
			public void onFailure(Throwable caught) {
				if(caught instanceof InvocationException) {
					handleConnectionFailure(command, caught, callback, attempt);
				} else {
					callback.onFailure(caught);
				}
			}

			@Override
			public void onSuccess(T result) {
				callback.onSuccess(result);
			}
		});
	}
	
	private final <T extends CommandResult> void handleConnectionFailure(final Command<T> command, Throwable caught, final AsyncCallback<T> callback, final int attempt) {
		if(attempt > MAX_RETRY_COUNT) {
			callback.onFailure(new SynchronizerConnectionException(caught));
		} else {
			int delay = retryDelay(attempt);
			eventBus.fireEvent(new SyncConnectionProblemEvent(attempt+1, delay));
			new Timer() {

				@Override
				public void run() {
					tryExecute(command, callback, attempt+1);
				}
			}.schedule(delay);
		}
	}

	private int retryDelay(final int attempt) {
		return (int)Math.pow(2, attempt);
	}
}
