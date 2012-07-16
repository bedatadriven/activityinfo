package org.activityinfo.client.offline.sync.pipeline;

import org.activityinfo.client.dispatch.callback.NullAsyncCallback;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AsyncPipeline {

	private final AsyncCommand[] commands;
	private AsyncCallback<Void> finalCallback;
	private int commandIndex = 0;
	private boolean running;
	
	public AsyncPipeline(AsyncCommand... commands) {
		this.commands = commands;
	}

	public void start(AsyncCallback<Void> callback) {
		if(!running) {
			running = true;
			finalCallback = callback;
			commandIndex = 0;
			executeNext();
		}
	}
	
	public void start() {
		start(new NullAsyncCallback<Void>());
	}
	
	private void executeNext() {
		this.running = true;
		if(commandIndex < commands.length) {
			int nextCommand = commandIndex++;
			Log.trace("AsyncPipeline: executing " + commands[nextCommand].toString());
			try {
				commands[nextCommand].execute(new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						AsyncPipeline.this.executeNext();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						running = false;
						finalCallback.onFailure(caught);
					}
				});
			} catch(Exception e) {
				running = false;
				finalCallback.onFailure(e);
			}
		} else {
			running = false;
			finalCallback.onSuccess(null);
		}
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public static void execute(AsyncCallback<Void> callback, AsyncCommand... commands) {
		new AsyncPipeline(commands).start(callback);
	}
	
	public static void execute(AsyncCommand... commands) {
		execute(new NullAsyncCallback<Void>(), commands);
	}
}
