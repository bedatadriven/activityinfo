package org.sigmah.client.dispatch.remote;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.sigmah.client.AppEvents;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.*;
import org.sigmah.client.dispatch.remote.cache.CommandProxyResult;
import org.sigmah.client.event.ConnectionEvent;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.RemoteCommandServiceAsync;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.InvalidAuthTokenException;
import org.sigmah.shared.exception.UnexpectedCommandException;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link org.sigmah.client.dispatch.Dispatcher} that
 * provides for batching of commands at 200 ms, plugable caches, and retrying.
 */
@Singleton
public class RemoteDispatcher implements Dispatcher, DispatchEventSource {

    protected final EventBus eventBus;
    protected final RemoteCommandServiceAsync service;
    protected final Authentication authentication;

    protected boolean connected = false;
    protected ProxyManager proxyManager = new ProxyManager();

    /**
     * Pending commands have been requested but not yet sent to the server
     */
    protected List<CommandRequest> pendingCommands = new ArrayList<CommandRequest>();

    /**
     * Executing commands have been sent to the server but for which we have
     * not yet received a response.
     */
    protected List<CommandRequest> executingCommands = new ArrayList<CommandRequest>();

    @Inject
    public RemoteDispatcher(RemoteCommandServiceAsync service,
                            EventBus eventBus, Authentication authentication) {
        this.service = service;
        this.eventBus = eventBus;
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
    public <T extends Command> void registerListener(Class<T> commandClass, DispatchListener<T> listener) {
        proxyManager.registerListener(commandClass, listener);
    }

    @Override
    public <T extends Command> void registerProxy(Class<T> commandClass, CommandProxy<T> proxy) {
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
    public <T extends CommandResult> void execute(Command<T> command, AsyncMonitor monitor,
                                                  AsyncCallback<T> callback) {

        CommandRequest request = new CommandRequest(command, monitor, callback);
        request.fireBeforeRequest();

        if (!request.isMutating() &&
                !request.mergeSuccessfulInto(pendingCommands) &&
                !request.mergeSuccessfulInto(executingCommands)) {

            Log.debug("RemoteDispatcher: Scheduled " + command.toString() + ", now " +
                    pendingCommands.size() + " command(s) pending");

            queue(request);
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

            if (executionWithProxySuccessful(cmd)) {
                pendingCommands.remove(i);

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
        return cmd.retries > 0 && !cmd.fireRetrying();
    }

    private void onRetriesMaxedOut(CommandRequest cmd) {
        Log.debug("RemoteDispatcher: The monitor " +
                " has denied a retry attempt after " + cmd.retries +
                " retries, the command is removed from the queue.");
        cmd.fireRetriesMaxedOut();
    }


    /**
     * Attempts to execute the command locally using one of the registered
     * proxies
     *
     * @param request
     * @return
     */
    private boolean executionWithProxySuccessful(CommandRequest request) {
        CommandProxyResult r = proxyManager.execute(request.getCommand());
        if (!r.couldExecute) {
            return false;
        }

        request.fireOnSuccess(r.result);
        return true;
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

        try {
            service.execute(authentication.getAuthToken(), commandListFromRequestList(sent), new AsyncCallback<List<CommandResult>>() {
                public void onSuccess(List<CommandResult> results) {
                    executingCommands.removeAll(sent);
                    onRemoteCallSuccess(results, sent);
                }

                public void onFailure(Throwable caught) {
                    executingCommands.removeAll(sent);
                    onRemoteServiceFailure(sent, caught);
                }
            });
        } catch (Throwable caught) {
            executingCommands.removeAll(sent);
            onClientSideSerializationError(caught);
        }
    }


    protected void onRemoteCallSuccess(List<CommandResult> results, List<CommandRequest> executingCommands) {
        Log.debug("RemoteDispatcher: remote call succeed");

        if (!connected) {
            connected = true;
            eventBus.fireEvent(AppEvents.ConnectionStatusChange, new ConnectionEvent(connected));
        }

        /*
        * Post process results
        */

        for (int i = 0; i != executingCommands.size(); ++i) {

            CommandRequest cmd = executingCommands.get(i);
            CommandResult result = results.get(i);

            if (result instanceof CommandException) {
                cmd.fireOnFailure((CommandException) result,
                        result instanceof UnexpectedCommandException);

            } else {
                cmd.fireOnSuccess(result);
                proxyManager.notifyListenersOfSuccess(cmd.getCommand(), result);
            }
        }
    }

    /**
     * Handles remote service call failures. Normally, even if a specific Command fails, we will
     * get CommandResult exception rather than a general failure, so if we reach here, it means
     * something more general has gone wrong with the communication with the server, such as a
     * network failure, or a mismatch between server/client versions.
     */
    protected void onRemoteServiceFailure(List<CommandRequest> sentCommands, Throwable caught) {

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
        int code = ((StatusCodeException) caught).getStatusCode();

        // TODO: handle 404s and other indications of temporary service inavailability
        // (different than 500 which means we screwed up on the server)

        // internal server error. This shouldn't happen so probably
        // indicates a pretty serious error.

        onServerError(executingCommands, caught);
    }

    private void onConnectionProblem(List<CommandRequest> executingCommands) {
        if (connected) {
            connected = false;
            eventBus.fireEvent(AppEvents.ConnectionStatusChange, new ConnectionEvent(connected));
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
        Window.alert("A new version of ActivityInfo has been posted to the server. You will need to refresh before continuing.");
    }

    private void onAuthenticationExpired() {
        /*
        * Our authorization token has expired,
        * requeue these commands and trigger an authentication
        */

        // TODO: for the moment our auth tokens don't expire. If at some point
        // we implement more aggressive security this needs to be handled gracefully
        // on the client.
        Window.alert("You are not authenticated. Not clear why this has happened. Try logging in again.");
    }

    private void onClientSideSerializationError(Throwable caught) {
        Log.error("RemoteDispatcher: client side exception thrown during execution of remote command", caught);
        onServerError(executingCommands, caught);
    }

    protected void onServerError(List<CommandRequest> executingCommands, Throwable caught) {
        for (CommandRequest cmd : executingCommands) {
            cmd.fireOnFailure(caught, true);
        }
    }
}
