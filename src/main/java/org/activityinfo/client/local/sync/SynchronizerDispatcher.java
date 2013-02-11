package org.activityinfo.client.local.sync;

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
