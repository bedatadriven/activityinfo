/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.command;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.auth.AuthenticatedUser;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.util.Collector;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * Dispatches commands to local handlers
 */
public class LocalDispatcher implements Dispatcher {
    private final AuthenticatedUser auth;
    private final HandlerRegistry registry;
    private final SqlDatabase database;
    private final CommandQueue commandQueue;
    
    @Inject
    public LocalDispatcher(AuthenticatedUser auth, SqlDatabase database, HandlerRegistry registry) {
        Log.trace("LocalDispatcher constructor starting...");
    	this.auth = auth;
        this.registry = registry;
        this.database = database;
        this.commandQueue = new CommandQueue(database);
    }


    @Override
    public <R extends CommandResult> void execute(Command<R> command, final AsyncMonitor monitor, final AsyncCallback<R> callback) {
       	    	
        if(monitor != null) {
            monitor.beforeRequest();
        }
        try {
            doExecute(command, new AsyncCallback<R>() {

				@Override
				public void onFailure(Throwable caught) {
					Log.debug("Offline command execution failed", caught);
					try {
			            if(monitor!=null) {
			                monitor.onServerError();
			            }
		            } catch(Exception ignored) {
		            }
		            callback.onFailure(caught);
				}

				@Override
				public void onSuccess(R result) {
		            Log.debug("Command success");
					if(monitor!=null) {
						monitor.onCompleted();
					}
					callback.onSuccess(result);					
				}
			});

        } catch (Exception e) {
            Log.debug("Command failure: ", e);
			try {
	            if(monitor!=null) {
	                monitor.onServerError();
	            }
            } catch(Exception ignored) {
            }
            callback.onFailure(e);
        }
    }
    
    /**
     * Begins a new transaction, initializes a new ExecutionContext, and executes the root command.
     * @param command
     * @param callback
     */
    private <R extends CommandResult> void doExecute(final Command<R> command, final AsyncCallback<R> callback) {
    	final Collector<R> commandResult = Collector.newCollector();
    	database.transaction(new SqlTransactionCallback() {
			
			@Override
			public void begin(SqlTransaction tx) {
				OfflineExecutionContext context = new OfflineExecutionContext(auth, tx, registry, commandQueue);
				context.execute(command, commandResult);
			}

			@Override
			public void onError(SqlException e) {
				callback.onFailure(e);
			}

			@Override
			public void onSuccess() {
				callback.onSuccess(commandResult.getResult());
			}
		});
    }
    
    public boolean canExecute(Command c) {
    	return registry.hasHandler(c);    	
    }
}
