package org.activityinfo.client.local.sync;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.local.command.CommandQueue;
import org.activityinfo.client.local.command.CommandQueue.QueueEntry;
import org.activityinfo.shared.command.result.CommandResult;

import com.bedatadriven.rebar.async.Async;
import com.bedatadriven.rebar.async.AsyncCommand;
import com.bedatadriven.rebar.async.AsyncFunction;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Sends updates to the remote database. 
 * 
 */
@Singleton
public class UpdateSynchronizer implements AsyncCommand {

	private CommandQueue commandQueue;
	private Dispatcher dispatcher;
	
	@Inject
	public UpdateSynchronizer(CommandQueue commandQueue, SynchronizerDispatcher dispatcher) {
		super();
		this.commandQueue = commandQueue;
		this.dispatcher = dispatcher;
	}
	
	private AsyncFunction<QueueEntry, CommandResult> dispatch() {
		return new AsyncFunction<CommandQueue.QueueEntry, CommandResult>() {
			
			@Override
			protected void doApply(QueueEntry argument, AsyncCallback<CommandResult> callback) {
				dispatcher.execute(argument.getCommand(),  callback);
			}
		};
	}
	
	public void execute(AsyncCallback<Void> callback) {
		commandQueue.get().map(
				Async.sequence(dispatch(), 
							   commandQueue.remove()))
			.discardResult()
			.apply(null, callback);	
	}
	
}
