package org.sigmah.client.offline.sync;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Direct;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.inject.Inject;

/**
 * Dispatcher implementation used by the synchronizer classes that 
 * executes commands directly without caching, and retries aggressively.
 */
public class SynchronizerDispatcher implements Dispatcher {
	
	private final EventBus eventBus;
	private final Dispatcher remoteDispatcher;
	
	private static final int MAX_RETRY_COUNT = 16;

	@Inject
	public SynchronizerDispatcher(EventBus eventBus, @Direct Dispatcher remoteDispatcher) {
		super();
		this.eventBus = eventBus;
		this.remoteDispatcher = remoteDispatcher;
	}

	@Override
	public <T extends CommandResult> void execute(Command<T> command,
			AsyncMonitor monitor, AsyncCallback<T> callback) {

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
			callback.onFailure(new SynchronizerConnectionException());
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
