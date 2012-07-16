/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.offline.command;

import org.activityinfo.client.dispatch.remote.AbstractDispatcher;
import org.activityinfo.client.dispatch.remote.RemoteDispatcher;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.util.Collector;

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
public class LocalDispatcher extends AbstractDispatcher {
    private final AuthenticatedUser auth;
    private final HandlerRegistry registry;
    private final SqlDatabase database;
    private final CommandQueue commandQueue;
	private final RemoteDispatcher remoteDispatcher;
    
    @Inject
    public LocalDispatcher(AuthenticatedUser auth, SqlDatabase database, HandlerRegistry registry,
    		RemoteDispatcher remoteDispatcher) {
        Log.trace("LocalDispatcher constructor starting...");
    	this.auth = auth;
        this.registry = registry;
        this.database = database;
        this.commandQueue = new CommandQueue(database);
        this.remoteDispatcher = remoteDispatcher;
    }
    
    @Override
	public <R extends CommandResult> void execute(Command<R> command,
			final AsyncCallback<R> callback) {
    	
    	if(registry.hasHandler(command)) {
    		executeOffline(command, callback);
    	} else {
    		executeRemotely(command, callback);
    	}
	}

	/**
     * Begins a new transaction, initializes a new ExecutionContext, and executes the root command.
     * @param command
     * @param callback
     */
    private <R extends CommandResult> void executeOffline(final Command<R> command, final AsyncCallback<R> callback) {
    	try {
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
    	} catch(Exception e) {
    		callback.onFailure(e);
    	}
    }

    private <R extends CommandResult> void executeRemotely(final Command<R> command, final AsyncCallback<R> callback) {
    	remoteDispatcher.execute(command, callback);
    }
}
