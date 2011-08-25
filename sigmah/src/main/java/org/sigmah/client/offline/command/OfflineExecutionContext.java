package org.sigmah.client.offline.command;

import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.MutatingCommand;
import org.sigmah.shared.command.handler.ExecutionContext;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;

import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class OfflineExecutionContext implements ExecutionContext {

	private User user;
	private SqlTransaction tx;
	private HandlerRegistry registry;
	private CommandQueue commandQueue;
	
		
	public OfflineExecutionContext(User user, SqlTransaction tx,
			HandlerRegistry registry,
			CommandQueue commandQueue) {
		super();
		this.user = user;
		this.tx = tx;
		this.registry = registry;
		this.commandQueue = commandQueue;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public SqlTransaction getTransaction() {
		return tx;
	}

	@Override
	public <C extends Command<R>, R extends CommandResult> void execute(
			final C command, final AsyncCallback<R> callback) {

		registry.getHandler(command).execute(command, this, new AsyncCallback<R>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
				
			}

			@Override
			public void onSuccess(R result) {
				if(command instanceof MutatingCommand) {
					commandQueue.queue(tx, command);
				}
				callback.onSuccess(result);
			}
		});
	}
}
