package org.activityinfo.client.dispatch.remote.cache;


import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.remote.AbstractDispatcher;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class CachingDispatcher extends AbstractDispatcher {

	private final CacheManager cacheManager;
	private final Dispatcher dispatcher;
	
	public CachingDispatcher(CacheManager proxyManager, Dispatcher dispatcher) {
		super();
		this.cacheManager = proxyManager;
		this.dispatcher = dispatcher;
	}

	@Override
	public <T extends CommandResult> void execute(final Command<T> command,
			final AsyncCallback<T> callback) {

		cacheManager.notifyListenersBefore(command);

		CacheResult proxyResult = cacheManager.execute(command);
		if (proxyResult.isCouldExecute()) {
			callback.onSuccess((T) proxyResult.getResult());
		} else {
			dispatcher.execute(command, new AsyncCallback<T>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccess(T result) {
		            cacheManager.notifyListenersOfSuccess(command, result);
		            callback.onSuccess(result);
				}
			});
		}
	}
}
