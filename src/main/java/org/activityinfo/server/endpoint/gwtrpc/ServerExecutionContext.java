package org.activityinfo.server.endpoint.gwtrpc;

import javax.persistence.EntityManager;

import org.activityinfo.server.command.handler.CommandHandler;
import org.activityinfo.server.command.handler.HandlerUtil;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.MutatingCommand;
import org.activityinfo.shared.command.handler.AuthorizationHandler;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.UnexpectedCommandException;
import org.activityinfo.shared.util.Collector;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcScheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Injector;

public class ServerExecutionContext implements ExecutionContext {
	
	private AuthenticatedUser user;
	private Injector injector;
	private SqlTransaction tx;

	public ServerExecutionContext(Injector injector, SqlTransaction tx, AuthenticatedUser user) {
		super();
		this.injector = injector;
		this.tx = tx;
		this.user = user;
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
		Object handler = injector.getInstance(HandlerUtil.asyncHandlerForCommand(command));
		if(handler instanceof CommandHandlerAsync) {
			((CommandHandlerAsync<C,R>)handler).execute(command, this, callback);
		} else if(handler instanceof CommandHandler) {
			try {
				callback.onSuccess((R) ((CommandHandler)handler).execute(command, retrieveUserEntity()));
				
			} catch(Exception e) {
				callback.onFailure(e);
			}
		}
	}

	private User retrieveUserEntity() {
		return this.injector.getInstance(EntityManager.class).find(User.class, user.getId());
	}
	
	
	public static <C extends Command<R>, R extends CommandResult> CommandResult execute(final Injector injector, 
			final C command) throws CommandException {
		final ResultCollector<R> result = new ResultCollector<R>();
		final Collector<Boolean> txResult = Collector.newCollector();
		
		// Try first to do a simple synchronous execution if the handler is an old
		// hibernate handler
		final Object handler = injector.getInstance(HandlerUtil.handlerForCommand(command));
		final AuthenticatedUser user = injector.getInstance(AuthenticatedUser.class);
		if(handler instanceof CommandHandler) {
			User userEntity = injector.getInstance(EntityManager.class).find(User.class, user.getId());
			return ((CommandHandler) handler).execute(command, userEntity);
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
		private R result = null;
		private Throwable caught = null;
		
		@Override
		public void onFailure(Throwable caught) {
			this.callbackCount++;
			if(callbackCount > 1) {
				throw new RuntimeException("Callback called multiple times");
			}
			this.caught = caught;
		}

		@Override
		public void onSuccess(R result) {
			callbackCount ++;
			if(callbackCount > 1) {
				throw new RuntimeException("Callback called multiple times");
			}
			this.result = result;
		}
	
	}
	
}