package org.activityinfo.client.local.command;

import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.MutatingCommand;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.command.result.CommandResult;

import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LocalExecutionContext implements ExecutionContext {

	private AuthenticatedUser user;
	private SqlTransaction tx;
	private HandlerRegistry registry;
	private CommandQueue commandQueue;
	
		
	public LocalExecutionContext(AuthenticatedUser user, SqlTransaction tx,
			HandlerRegistry registry,
			CommandQueue commandQueue) {
		super();
		this.user = user;
		this.tx = tx;
		this.registry = registry;
		this.commandQueue = commandQueue;
	}

	@Override
	public boolean isRemote() {
		return false;
	}

	@Override
	public AuthenticatedUser getUser() {
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
