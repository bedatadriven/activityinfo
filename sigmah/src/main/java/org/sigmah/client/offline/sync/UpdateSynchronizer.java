package org.sigmah.client.offline.sync;

import java.sql.SQLException;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Direct;
import org.sigmah.client.offline.command.CommandQueue;
import org.sigmah.client.offline.command.CommandQueue.QueueEntry;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Sends updates to the remote database. 
 * 
 */
@Singleton
public class UpdateSynchronizer {

	private CommandQueue commandQueue;
	private Dispatcher dispatcher;
	
	private AsyncCallback<Void> callback;
	private QueueEntry currentEntry = null;
	
	@Inject
	public UpdateSynchronizer(CommandQueue commandQueue, SynchronizerDispatcher dispatcher) {
		super();
		this.commandQueue = commandQueue;
		this.dispatcher = dispatcher;
//		
//		try {
//			this.commandQueue.createTableIfNotExists();
//		} catch (SQLException e) {
//			throw new RuntimeException("Could not create command queue_table", e);
//		}
	}
	
	
	public void sync(AsyncCallback<Void> callback) {
		this.callback = callback;
		nextCommand();
	}
	
	private void nextCommand() {
		commandQueue.peek(new AsyncCallback<CommandQueue.QueueEntry>() {
			
			@Override
			public void onSuccess(QueueEntry entry) {
				currentEntry = entry;
				if(currentEntry == null) {
					callback.onSuccess(null);
				} else {
					sendToServer();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}


	private void sendToServer() {
		dispatcher.execute(currentEntry.getCommand(), null, new AsyncCallback() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(Object result) {
				removeFromQueue();
			}
		});
		
	}
	
	private void removeFromQueue() {
		commandQueue.remove(currentEntry.getId(), new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean removed) {
				nextCommand();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}
}
