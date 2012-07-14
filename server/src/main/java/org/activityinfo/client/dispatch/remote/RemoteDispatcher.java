/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch.remote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.activityinfo.client.SessionUtil;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.RemoteCommandServiceAsync;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.InvalidAuthTokenException;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * An implementation of {@link org.activityinfo.client.dispatch.Dispatcher} that
 * provides for batching of commands at 200 ms, plugable caches, and retrying.
 */
@Singleton
public class RemoteDispatcher implements Dispatcher {

    private final RemoteCommandServiceAsync service;
    private final AuthenticatedUser authentication;
	private final IncompatibleRemoteHandler incompatibleRemoteHandler;


    private boolean connected = false;

    /**
     * Pending commands have been requested but not yet sent to the server
     */
    private List<CommandRequest> pendingCommands = new ArrayList<CommandRequest>();

    /**
     * Executing commands have been sent to the server but for which we have
     * not yet received a response.
     */
    private List<CommandRequest> executingCommands = new ArrayList<CommandRequest>();
	

    @Inject
    public RemoteDispatcher(RemoteCommandServiceAsync service,
    						Scheduler scheduler,
                            AuthenticatedUser authentication,
                            IncompatibleRemoteHandler incompatibleRemoteHandler) {
        this.service = service;
        this.authentication = authentication;
        this.incompatibleRemoteHandler = incompatibleRemoteHandler;

        Log.debug("RemoteDispatcher created: " + this.toString());

        scheduler.scheduleFinally(new RepeatingCommand() {
			
			@Override
			public boolean execute() {
				if (!pendingCommands.isEmpty()) {
                    sendPendingToServer();
                }
				return true;
			}
		});
    }
    
    @Override
	public <T extends CommandResult> void execute(Command<T> command,
			AsyncCallback<T> callback) {
    	execute(command, null, callback);
	}

	/**
     * Schedules a command for asynchronous execution
     * <p/>
     * N.B. commands are NOT guaranteed to be executed in the order
     * queued. The failure of earlier commands does not impede
     * the execution of subsequent commands
     */
    @Override
    public final <T extends CommandResult> void execute(Command<T> command, AsyncMonitor monitor,
                                                        AsyncCallback<T> callback) {

        CommandRequest request = new CommandRequest(command, callback);

        if (request.isMutating()) {
        	// mutating requests get queued immediately, don't try to merge them
        	// into any pending/executing commands, it wouldn't be correct
        	
        	queue(request);
        } else {
        	if(!request.mergeSuccessfulInto(pendingCommands) &&
               !request.mergeSuccessfulInto(executingCommands)) {

	            queue(request);
	
	            Log.debug("RemoteDispatcher: Scheduled " + command.toString() + ", now " +
	                    pendingCommands.size() + " command(s) pending");
        	}
        }
    }

    private void queue(CommandRequest request) {
        pendingCommands.add(request);
    }
    
    private void sendPendingToServer() {
        Log.debug("RemoteDispatcher: sending " + pendingCommands.size() + " to server.");

        final List<CommandRequest> sent = new ArrayList<CommandRequest>(pendingCommands);
        executingCommands.addAll(sent);
        pendingCommands.clear();

        if(!sent.isEmpty()) {
        	for(final CommandRequest request : sent) {
        		try {
        			service.execute(authentication.getAuthToken(), Lists.newArrayList(request.getCommand()),
        					new AsyncCallback<List<CommandResult>>() {
        				
        				@Override
        				public void onSuccess(List<CommandResult> results) {
        					executingCommands.remove(request);
        					onRemoteCallSuccess(results.get(0), request);
        				}

        				@Override
        				public void onFailure(Throwable caught) {
        					executingCommands.remove(request);
        					onRemoteServiceFailure(Arrays.asList(request), caught);
        				}
        			});
        		} catch (Exception caught) {
        			executingCommands.removeAll(sent);
        			onClientSideSerializationError(caught);
        		}
        	}
        }
    }

    private void onRemoteCallSuccess(CommandResult result, CommandRequest cmd) {
        Log.debug("RemoteDispatcher: remote call succeed");

        if (!connected) {
            connected = true;
        }

        /*
        * Post process results
        */

        if (result instanceof CommandException) {
            Log.debug("Remote command failed. Command = " + cmd.getCommand().toString());
            cmd.fireOnFailure((CommandException) result);

        } else {
            cmd.fireOnSuccess(result);
        }
    }

    /**
     * Handles remote service call failures. Normally, even if a specific Command fails, we will
     * get CommandResult exception rather than a general failure, so if we reach here, it means
     * something more general has gone wrong with the communication with the server, such as a
     * network failure, or a mismatch between server/client versions.
     */
    private void onRemoteServiceFailure(List<CommandRequest> sentCommands, Throwable caught) {

        Log.error("RemoteDispatcher: remote call failed", caught);

        /*
        * Not all failures are created equal.
        * Sort through the harry mess of exceptions to figure out how
        * to present this to the user.
        */

        if (caught instanceof InvalidAuthTokenException) {
            onAuthenticationExpired();

        } else if (caught instanceof IncompatibleRemoteServiceException) {
            incompatibleRemoteHandler.handle();

        } else if (caught instanceof StatusCodeException) {
            onHttpError(sentCommands, caught);

        } else if (caught instanceof InvocationException) {
            onConnectionProblem(sentCommands);

        } else {
            for (CommandRequest cmd : sentCommands) {
                cmd.fireOnFailure(caught);
            }
        }
    }

    private void onHttpError(List<CommandRequest> executingCommands, Throwable caught) {

        // TODO: handle 404s and other indications of temporary service inavailability
        // (different than 500 which means we screwed up on the server)
    	// int code = ((StatusCodeException) caught).getStatusCode();

        // internal server error. This shouldn't happen so probably
        // indicates a pretty serious error.

        onServerError(executingCommands, caught);
    }

    private void onConnectionProblem(List<CommandRequest> executingCommands) {
        if (connected) {
            connected = false;
        }

        for (CommandRequest cmd : executingCommands) {
            pendingCommands.add(cmd);
        }
    }

    private void onAuthenticationExpired() {
        /*
        * Our authorization token has expired,
        * requeue these commands and trigger an authentication
        */

        // TODO: for the moment our auth tokens don't expire. If at some point
        // we implement more aggressive security this needs to be handled gracefully
        // on the client.
        Window.alert(I18N.CONSTANTS.notAuthenticated());
        
        SessionUtil.forceLogin();
    }

	private void onClientSideSerializationError(Throwable caught) {
        Log.error("RemoteDispatcher: client side exception thrown during execution of remote command", caught);
        onServerError(executingCommands, caught);
    }

    private void onServerError(List<CommandRequest> executingCommands, Throwable caught) {
        for (CommandRequest cmd : executingCommands) {
            cmd.fireOnFailure(caught);
        }
    }
}
