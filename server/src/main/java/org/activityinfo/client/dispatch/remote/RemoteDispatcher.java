/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch.remote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.SessionUtil;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.CommandProxy;
import org.activityinfo.client.dispatch.DispatchEventSource;
import org.activityinfo.client.dispatch.DispatchListener;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.remote.cache.ProxyResult;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.RemoteCommandServiceAsync;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.InvalidAuthTokenException;
import org.activityinfo.shared.exception.UnexpectedCommandException;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
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
public class RemoteDispatcher implements Dispatcher, DispatchEventSource {

    private final RemoteCommandServiceAsync service;
    private final AuthenticatedUser authentication;

    private boolean connected = false;
    private ProxyManager proxyManager = new ProxyManager();

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
                            EventBus eventBus, AuthenticatedUser authentication) {
        this.service = service;
        this.authentication = authentication;

        Log.debug("RemoteDispatcher created: " + this.toString());

        // to facilitate unit testing, we only start the timer
        // if we're running on the client: either as byte-code in development mode
        // or javascript in production mode.
        //
        // unit tests running in a plain-old JVM will need to call proccessPendingCommands() themselves
        if (GWT.isClient()) {
            startTimer();
        }
    }

    private void startTimer() {
        new Timer() {
            @Override
            public void run() {
                if (!pendingCommands.isEmpty()) {
                    processPendingCommands();
                }
            }
        }.scheduleRepeating(200);
    }

    @Override
    public final <T extends Command> void registerListener(Class<T> commandClass, DispatchListener<T> listener) {
        proxyManager.registerListener(commandClass, listener);
    }

    @Override
    public final <T extends Command> void registerProxy(Class<T> commandClass, CommandProxy<T> proxy) {
        proxyManager.registerProxy(commandClass, proxy);
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

        CommandRequest request = new CommandRequest(command, monitor, callback);
        request.fireBeforeRequest();

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

    /**
     * Executes a collection of pending commands
     */
    public void processPendingCommands() {
        preprocessQueue();
        sendPendingToServer();
    }

    private void preprocessQueue() {
        Log.debug("RemoteDispatcher: " + pendingCommands.size() + " are pending.");

        int i = 0;
        while (i < pendingCommands.size()) {
            CommandRequest cmd = pendingCommands.get(i);
            ProxyResult proxyResult = proxyManager.execute(cmd.getCommand());
            if (proxyResult.isCouldExecute()) {
            	pendingCommands.remove(i);
            	cmd.fireOnSuccess(proxyResult.getResult());
            
            } else if (retriesMaxedOut(cmd)) {
                pendingCommands.remove(i);
                onRetriesMaxedOut(cmd);

            } else {
                proxyManager.notifyListenersBefore(cmd.getCommand());
                i++;
            }
        }
    }

    private boolean retriesMaxedOut(CommandRequest cmd) {
        return cmd.getRetries() > 0 && !cmd.fireRetrying();
    }

    private void onRetriesMaxedOut(CommandRequest cmd) {
        Log.debug("RemoteDispatcher: The monitor " +
                " has denied a retry attempt after " + cmd.getRetries() +
                " retries, the command is removed from the queue.");
        cmd.fireRetriesMaxedOut();
    }


    private List<Command> commandListFromRequestList(List<CommandRequest> pending) {
        List<Command> commands = new ArrayList<Command>(pending.size());
        for (CommandRequest request : pending) {
            commands.add(request.getCommand());
        }
        return commands;
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
            cmd.fireOnFailure((CommandException) result,
                    result instanceof UnexpectedCommandException);

        } else {
            cmd.fireOnSuccess(result);
            proxyManager.notifyListenersOfSuccess(cmd.getCommand(), result);
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
            onVersionMismatch();

        } else if (caught instanceof StatusCodeException) {
            onHttpError(sentCommands, caught);

        } else if (caught instanceof InvocationException) {
            onConnectionProblem(sentCommands);

        } else {
            for (CommandRequest cmd : sentCommands) {
                cmd.fireOnFailure(caught, true);
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
            cmd.fireOnConnectionProblem();
            pendingCommands.add(cmd);
        }
    }

    private void onVersionMismatch() {
        // The correct response to receiving an instance of this exception in the
        // AsyncCallback.onFailure(Throwable) method is to get the application into
        //  a state where a browser refresh can be done.
        // TODO: this needs to be handled by the user interface somewhere
        Window.alert(I18N.CONSTANTS.newVersionPrompt());
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
            cmd.fireOnFailure(caught, true);
        }
    }
}
