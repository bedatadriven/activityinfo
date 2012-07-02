package org.activityinfo.client.dispatch.remote;


import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.CommandProxy;
import org.activityinfo.client.dispatch.DispatchEventSource;
import org.activityinfo.client.dispatch.DispatchListener;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.remote.cache.ProxyResult;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class CachingDispatcher implements Dispatcher, DispatchEventSource {

	private ProxyManager proxyManager = new ProxyManager();
	private Dispatcher dispatcher;
	
	public CachingDispatcher(ProxyManager proxyManager, Dispatcher dispatcher) {
		super();
		this.proxyManager = proxyManager;
		this.dispatcher = dispatcher;
	}

	@Override
	public final <T extends Command> void registerListener(Class<T> commandClass, DispatchListener<T> listener) {
		proxyManager.registerListener(commandClass, listener);
	}

	@Override
	public final <T extends Command> void registerProxy(Class<T> commandClass, CommandProxy<T> proxy) {
		proxyManager.registerProxy(commandClass, proxy);
	}

	@Override
	public <T extends CommandResult> void execute(Command<T> command,
			AsyncMonitor monitor, AsyncCallback<T> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends CommandResult> void execute(final Command<T> command,
			final AsyncCallback<T> callback) {

		proxyManager.notifyListenersBefore(command);

		ProxyResult proxyResult = proxyManager.execute(command);
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
		            proxyManager.notifyListenersOfSuccess(command, result);
				}
			});
		}
	}
}
