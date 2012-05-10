package org.activityinfo.client.dispatch;

import java.util.List;

import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MultiDispatch {

	private Dispatcher dispatcher;
	private List<CommandResult> results = Lists.newArrayList();
	private List<Boolean> finished = Lists.newArrayList();
	private AsyncCallback<List<CommandResult>> callback;
	
	private Throwable caught = null;
	private boolean calledBack = false;
	


	public MultiDispatch(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	public <C extends Command<R>, R extends CommandResult>void execute(C command) {
		if(calledBack) {
			throw new IllegalStateException("Already called back");
		}
		results.add(null);
		finished.add(null);
		final int commandIndex = results.size();
		dispatcher.execute(command, null, new AsyncCallback<R>() {

			@Override
			public void onFailure(Throwable caught) {
				MultiDispatch.this.caught = caught;
				maybeCallback();
			}

			@Override
			public void onSuccess(R result) {
				results.set(commandIndex, result);
				finished.set(commandIndex, true);
				maybeCallback();
			}
		});
	}
	
	protected void maybeCallback() {
		if(callback != null && !calledBack) {
			if(caught != null) {
				callback.onFailure(caught);
				calledBack = true;
			} else if(allFinished()){
				callback.onSuccess(results);
				calledBack = true;
			}
		}
	}

	private boolean allFinished() {
		for(Boolean finished : this.finished) {
			if(!finished) {
				return false;
			}
		}
		return true;
	}
	
	public void callback(AsyncCallback<List<CommandResult>> callback) {
		if(calledBack) {
			throw new IllegalStateException("Callback already called");
		}
		this.callback = callback;
		maybeCallback();
	}
	
}
