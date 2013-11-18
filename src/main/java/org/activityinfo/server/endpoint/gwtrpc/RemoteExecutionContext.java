package org.activityinfo.server.endpoint.gwtrpc;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.activityinfo.server.command.handler.CommandHandler;
import org.activityinfo.server.command.handler.HandlerUtil;
import org.activityinfo.server.database.hibernate.HibernateExecutor;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.event.CommandEvent;
import org.activityinfo.server.event.ServerEventBus;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.MutatingCommand;
import org.activityinfo.shared.command.handler.AuthorizationHandler;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.hibernate.ejb.HibernateEntityManager;

import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcScheduler;
import com.bedatadriven.rebar.sql.shared.adapter.SyncTransactionAdapter;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Injector;

public class RemoteExecutionContext implements ExecutionContext {

    private static final Logger LOGGER = Logger
        .getLogger(RemoteExecutionContext.class.getName());

    private static final ThreadLocal<RemoteExecutionContext> CURRENT = new ThreadLocal<RemoteExecutionContext>();

    private AuthenticatedUser user;
    private Injector injector;
    private SyncTransactionAdapter tx;
    private HibernateEntityManager entityManager;
    private JdbcScheduler scheduler;

    private ServerEventBus serverEventBus;

    public RemoteExecutionContext(Injector injector) {
        super();
        this.injector = injector;
        this.user = injector.getInstance(AuthenticatedUser.class);
        this.entityManager = (HibernateEntityManager) injector
            .getInstance(EntityManager.class);
        this.scheduler = new JdbcScheduler();
        this.scheduler.allowNestedProcessing();
        this.serverEventBus = injector.getInstance(ServerEventBus.class);
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
        if (current == null) {
            throw new IllegalStateException(
                "No current command execution context");
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

        if (CURRENT.get() != null) {
            throw new IllegalStateException(
                "Command execution context already in progress");
        }

        try {
            CURRENT.set(this);
            /*
             * Begin the transaction
             */
            this.entityManager.getTransaction().begin();

            /*
             * Setup an async transaction simply wrapping the hibernate
             * transaction
             */
            this.tx = new SyncTransactionAdapter(new HibernateExecutor(
                this.entityManager),
                scheduler, new TransactionCallback());
            this.tx.withManualCommitting();

            /*
             * Execute the command
             */

            R result;

            try {
                result = execute(command);

                scheduler.process();

            } catch (Exception e) {
                /*
                 * If the execution fails, rollback
                 */
                try {
                    this.entityManager.getTransaction().rollback();
                } catch (Exception rollbackException) {
                    LOGGER.log(Level.SEVERE,
                        "Exception rolling back failed transaction",
                        rollbackException);
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
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Commit failed!", e);
                throw new RuntimeException("Commit failed", e);
            }

            return result;

        } finally {
            CURRENT.remove();
        }
    }

    /**
     * Executes a (nested) command synchronously. This is called from within
     * CommandHandlers to execute nested commands
     */
    public <C extends Command<R>, R extends CommandResult> R execute(
        final C command) {

        if (tx == null) {
            throw new IllegalStateException(
                "Command execution has not started yet");
        }

        ResultCollector<R> collector = new ResultCollector<R>(command
            .getClass().getSimpleName());
        execute(command, collector);

        scheduler.process();

        return collector.get();
    }

    /**
     * Executes a (nested) command (pseudo) asynchronously. This is called from
     * within CommandHandlers to execute nested commands.
     */
    @Override
    public <C extends Command<R>, R extends CommandResult> void execute(
        final C command, final AsyncCallback<R> callback) {

        if (command instanceof MutatingCommand) {
            // mutating commands MUST have a server-side AuthorizationHandler
            Class<AuthorizationHandler<C>> authHandlerClass = HandlerUtil
                .authorizationHandlerForCommand(command);

            if (authHandlerClass == null) {
                LOGGER.warning("No authorization handler for "
                    + command.getClass());
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

    private <C extends Command<R>, R extends CommandResult> void onAuthorized(
        final C command, AsyncCallback<R> outerCallback) {
        
        AsyncCallback<R> callback = new FiringCallback<R>(command, outerCallback);
        
        
        Object handler = injector.getInstance(HandlerUtil
            .asyncHandlerForCommand(command));

        if (handler instanceof CommandHandlerAsync) {
            /**
             * Execute Asynchronously
             */
            ((CommandHandlerAsync<C, R>) handler).execute(command, this, callback);
            
        } else if (handler instanceof CommandHandler) {
            /**
             * Executes Synchronously
             */
            try {
                callback.onSuccess((R) ((CommandHandler) handler)
                    .execute(command, retrieveUserEntity()));
            } catch (Exception e) {
                callback.onFailure(e);
            }
        }
    }

    private User retrieveUserEntity() {
        return entityManager.find(User.class, user.getId());
    }

    private void fireEvent(Command command, CommandResult result) {
        LOGGER.fine("notifying serverEventBus of completed command " + command.toString());
        serverEventBus.post(new CommandEvent(command, result, this));
    }
    
    private static RuntimeException wrapException(Throwable t) {
        if (t instanceof RuntimeException) {
            return (RuntimeException) t;
        } else {
            LOGGER.log(Level.SEVERE,
                "Unexpected command exception: " + t.getMessage(), t);
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

    private class FiringCallback<R extends CommandResult> implements AsyncCallback<R> {
        private final Command command;
        private final AsyncCallback<R> callback;
        
        public FiringCallback(Command command, AsyncCallback<R> callback) {
            super();
            this.command = command;
            this.callback = callback;
        }

        @Override
        public void onFailure(Throwable caught) {
            callback.onFailure(caught);
        }

        @Override
        public void onSuccess(R result) {
            LOGGER.fine("notifying serverEventBus of completed command " + command.toString());
            try {
                serverEventBus.post(new CommandEvent(command, result, RemoteExecutionContext.this));
            } catch(Exception e) {
                LOGGER.log(Level.SEVERE, "Exception while posting via server event bus: " + e.getMessage(), e);
            }
            callback.onSuccess(result);
        }
    }
    
    private static class ResultCollector<R> implements AsyncCallback<R> {

        private String name;
        private int callbackCount = 0;
        private R result = null;
        private Throwable caught = null;

        public ResultCollector(String name) {
            super();
            this.name = name;
        }

        @Override
        public void onFailure(Throwable caught) {
            this.callbackCount++;
            if (callbackCount > 1) {
                throw new RuntimeException("Callback for '" + name
                    + "' called multiple times");
            }
            this.caught = caught;
        }

        public R get() throws CommandException {
            if (callbackCount != 1) {
                throw new IllegalStateException("Callback for '" + name
                    + "' called " + callbackCount + " times");
            } else if (caught != null) {
                throw wrapException(caught);
            }
            return result;
        }

        @Override
        public void onSuccess(R result) {
            callbackCount++;
            if (callbackCount > 1) {
                throw new RuntimeException("Callback called multiple times");
            }
            this.result = result;
        }
    }
}