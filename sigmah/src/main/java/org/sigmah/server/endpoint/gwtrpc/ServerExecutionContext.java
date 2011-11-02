package org.sigmah.server.endpoint.gwtrpc;

import org.sigmah.server.command.handler.HandlerUtil;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.MutatingCommand;
import org.sigmah.shared.command.handler.AuthorizationHandler;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.handler.CommandHandlerAsync;
import org.sigmah.shared.command.handler.ExecutionContext;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.UnexpectedCommandException;
import org.sigmah.shared.util.Collector;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcScheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Injector;

public class ServerExecutionContext implements ExecutionContext {
	
	private User user;
	private Injector injector;
	private SqlTransaction tx;

	public ServerExecutionContext(Injector injector, SqlTransaction tx, User user) {
		super();
		this.injector = injector;
		this.tx = tx;
		this.user = user;
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

		if(command instanceof MutatingCommand) {
			// mutating commands MUST have a server-side AuthorizationHandler
			Class<AuthorizationHandler<C>> authHandlerClass = HandlerUtil.authorizationHandlerForCommand(command);
			
			if(authHandlerClass == null) {
				callback.onFailure(new RuntimeException("Missing authorizatin handler for " + command.getClass()));
			} else {			
				AuthorizationHandler<C> authHandler = injector.getInstance(
						authHandlerClass);

				authHandler.authorize(command, this, new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						onAuthorized(command, callback);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}
				});
			}
		} else {
			onAuthorized(command, callback);
		}
	}

	private <C extends Command<R>, R extends CommandResult> void onAuthorized(C command, final AsyncCallback<R> callback) {
		CommandHandlerAsync<C,R> handler = injector.getInstance(HandlerUtil.asyncHandlerForCommand(command));
		handler.execute(command, this, callback);
	}
	
	
	public static <C extends Command<R>, R extends CommandResult> CommandResult execute(final Injector injector, final User user, final C command) throws CommandException {
		final ResultCollector<R> result = new ResultCollector<R>();
		final Collector<Boolean> txResult = Collector.newCollector();
		
		final Object handler = injector.getInstance(
				HandlerUtil.handlerForCommand(command));
		
		if(handler instanceof CommandHandler) {
			return ((CommandHandler) handler).execute(command, user);
		}
		
		// TODO: log here, if there is something in the queue there is a problem somewhere.
		JdbcScheduler.get().forceCleanup();
 		
		injector.getInstance(SqlDatabase.class).transaction(new SqlTransactionCallback() {
			
			@Override
			public void begin(SqlTransaction tx) {
				ServerExecutionContext context = new ServerExecutionContext(injector, tx, user);
				context.execute(command, result);
			}

			@Override
			public void onError(SqlException e) {
				throw e;
			}

			@Override
			public void onSuccess() {
				txResult.onSuccess(true);
			}
		});
		
		if(!txResult.getResult()) {
			throw new AssertionError("tx did not complete. hint: you may be switching between sync and async code nilly-willy");
		}

		if(result.callbackCount != 1) {
			throw new UnexpectedCommandException("Callback called " + result.callbackCount + " times");
		}
		
		if(result.caught != null) {
			if(result.caught instanceof CommandException) {
				throw (CommandException)result.caught;
			} else {
				throw new UnexpectedCommandException(result.caught);
			}
		}
		
		return result.result;
	}

	private static class ResultCollector<R> implements AsyncCallback<R> {

		private int callbackCount=0;
		R result = null;
		Throwable caught = null;
		
		@Override
		public void onFailure(Throwable caught) {
			this.callbackCount++;
			this.caught = caught;
		}

		@Override
		public void onSuccess(R result) {
			callbackCount ++;
			this.result = result;
		}
	
	}
	
}