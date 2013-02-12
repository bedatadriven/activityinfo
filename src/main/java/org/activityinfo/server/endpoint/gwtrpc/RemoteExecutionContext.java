package org.activityinfo.server.endpoint.gwtrpc;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.activityinfo.login.shared.AuthenticatedUser;
import org.activityinfo.server.command.handler.CommandHandler;
import org.activityinfo.server.command.handler.HandlerUtil;
import org.activityinfo.server.database.hibernate.HibernateExecutor;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.MutatingCommand;
import org.activityinfo.shared.command.handler.AuthorizationHandler;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.UnexpectedCommandException;
import org.hibernate.ejb.HibernateEntityManager;

import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcScheduler;
import com.bedatadriven.rebar.sql.shared.adapter.SyncTransactionAdapter;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Injector;

public class RemoteExecutionContext implements ExecutionContext {
	
	private static Logger LOGGER = Logger.getLogger(RemoteExecutionContext.class.getName());

	private static ThreadLocal<RemoteExecutionContext> CURRENT = new ThreadLocal<RemoteExecutionContext>();
	
	private AuthenticatedUser user;
	private Injector injector;
	private SyncTransactionAdapter tx;
	private HibernateEntityManager entityManager;
	private JdbcScheduler scheduler;

	public RemoteExecutionContext(Injector injector) {
		super();		
		this.injector = injector;
		this.user = injector.getInstance(AuthenticatedUser.class);
		this.entityManager = (HibernateEntityManager) injector.getInstance(EntityManager.class);
		this.scheduler = new JdbcScheduler();
		this.scheduler.allowNestedProcessing();
	}

	@Override
	public boolean isRemote() {
		return true;
	}

	@Override
	public AuthenticatedUser getUser() {
		return user;
	}

	@Override
	public SqlTransaction getTransaction() {
		return tx;
	}
	
	public static RemoteExecutionContext current() {
		RemoteExecutionContext current = CURRENT.get();
		if(current == null) {
			throw new IllegalStateException("No current command execution context");
		}
		return current;
	}
	
	public static boolean inProgress() {
		return CURRENT.get() != null;
	}

	/**
	 * Executes the top-level command, starting a database transaction
	 */
	public <C extends Command<R>, R extends CommandResult> R startExecute(
			final C command) {
		
		if(CURRENT.get() != null) {
			throw new IllegalStateException("Command execution context already in progress");
		}
		
		try {
			CURRENT.set(this);
			/*
			 * Begin the transaction
			 */
			this.entityManager.getTransaction().begin();
			
			/*
			 * Setup an async transaction simply wrapping the
			 * hibernate transaction 
			 */
			this.tx = new SyncTransactionAdapter(new HibernateExecutor(this.entityManager), 
					scheduler, new TransactionCallback());
			this.tx.withManualCommitting();
			
			/*
			 * Execute the command
			 */

			R result;
			
			try {
				result = execute(command);
				
				scheduler.process();
								
			} catch(Exception e) {
				/*
				 * If the execution fails, rollback 
				 */
				try {
					this.entityManager.getTransaction().rollback();
				} catch(Exception rollbackException) {
					LOGGER.log(Level.SEVERE, "Exception rolling back failed transaction", rollbackException);
				}
				
				/*
				 * Rethrow exception, wrapping if necessary
				 */
				throw wrapException(e);
			}
			
			/*
			 * Commit the transaction
			 */
			
			try {
				this.entityManager.getTransaction().commit();
			} catch(Exception e) {
				LOGGER.log(Level.SEVERE, "Commit failed!", e);
				throw new RuntimeException("Commit failed", e);
			}			
			
			return result;
			
		} finally {
			CURRENT.remove();
		}
	}
	
	/**
	 * Executes a (nested) command synchronously.
	 * This is called from within CommandHandlers to execute 
	 * nested commands
	 */
	public <C extends Command<R>, R extends CommandResult> R execute(
			final C command) {
		
		if(tx == null) {
			throw new IllegalStateException("Command execution has not started yet");
		}
		
		ResultCollector<R> collector = new ResultCollector<R>(command.getClass().getSimpleName());
		execute(command, collector);
		
		scheduler.process();
				
		return collector.get();
	}
	

	/**
	 * Executes a (nested) command (pseudo) asynchronously. 
	 * This is called from within CommandHandlers
	 * to execute nested commands.
	 */
	@Override
	public <C extends Command<R>, R extends CommandResult> void execute(
			final C command, final AsyncCallback<R> callback) {

		if(command instanceof MutatingCommand) {
			// mutating commands MUST have a server-side AuthorizationHandler
			Class<AuthorizationHandler<C>> authHandlerClass = HandlerUtil.authorizationHandlerForCommand(command);
			
			if(authHandlerClass == null) {
				LOGGER.warning("No authorization handler for " + command.getClass());
				onAuthorized(command, callback);
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
			/**
			 * Execute Asynchronously
			 */
			((CommandHandlerAsync<C,R>)handler).execute(command, this, callback);
		} else if(handler instanceof CommandHandler) {
			/**
			 * Executes Synchronously
			 */
			try {
				callback.onSuccess((R) ((CommandHandler)handler).execute(command, retrieveUserEntity()));
			} catch(Exception e) {
				callback.onFailure(e);
			}
		}
	}

	private User retrieveUserEntity() {
		return entityManager.find(User.class, user.getId());
	}
	
	private static RuntimeException wrapException(Throwable t) {
		if(t instanceof RuntimeException) {
			return (RuntimeException) t;
		} else {
			LOGGER.log(Level.SEVERE, "Unexpected command exception: " + t.getMessage(), t);
			return new RuntimeException(t);
		}
	}
	
	private static class TransactionCallback extends SqlTransactionCallback {

		@Override
		public void begin(SqlTransaction tx) {
			// we actually start the transaction our self, so we know it
			// is already active.
		}

		@Override
		public void onError(SqlException e) {
			throw e;
		}
	}

	private static class ResultCollector<R> implements AsyncCallback<R> {

		private String name;
		private int callbackCount=0;
		private R result = null;
		private Throwable caught = null;
	
		
		
		public ResultCollector(String name) {
			super();
			this.name = name;
		}

		@Override
		public void onFailure(Throwable caught) {
			this.callbackCount++;
			if(callbackCount > 1) {
				throw new RuntimeException("Callback for '" + name + "' called multiple times");
			}
			this.caught = caught;
		}

		public R get() throws CommandException {
			if(callbackCount != 1) {
				throw new IllegalStateException("Callback for '" + name + "' called " + callbackCount + " times");
			} else if(caught != null) {
				throw wrapException(caught);
			}
			return result;
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