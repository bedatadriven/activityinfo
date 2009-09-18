package org.activityinfo.client.command;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.activityinfo.client.AppEvents;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.command.monitor.NullAsyncMonitor;
import org.activityinfo.client.event.AuthenticationEvent;
import org.activityinfo.client.event.ConnectionEvent;
import org.activityinfo.client.util.ITimer;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.RemoteCommandServiceAsync;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.ReportTemplateDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.InvalidAuthTokenException;
import org.activityinfo.shared.exception.UnexpectedCommandException;

import java.util.*;
import java.util.Map.Entry;

@Singleton
public class CommandServiceImpl implements CommandService, CommandEventSource {

    protected final EventBus eventBus;
    protected final RemoteCommandServiceAsync service;
    protected final Authentication authentication;

    protected boolean connected = false;

    protected ProxyManager proxyManager = new ProxyManager();

    protected List<CommandRequest> pendingCommands = new ArrayList<CommandRequest>();

    protected final AsyncMonitor nullMonitor = new NullAsyncMonitor();

    @Inject
    public CommandServiceImpl(RemoteCommandServiceAsync service, EventBus eventBus, ITimer timer, Authentication authentication) {
        this.service = service;
        this.eventBus = eventBus;
        this.authentication = authentication;

        GWT.log("CommandServiceImpl created: " + this.toString(), null);

        timer.scheduleRepeating(200, new ITimer.Callback() {
            public void run() {
                executePending();
            }
        });
    }

    @Override
    public <T extends Command> void registerListener(Class<T> commandClass, CommandListener<T> listener) {
        proxyManager.registerListener(commandClass, listener);
    }

    @Override
    public <T extends Command> void registerProxy(Class<T> commandClass, CommandProxy<T> proxy) {
        proxyManager.registerProxy(commandClass, proxy);
    }

    /**
     * Schedules a command for asynchronous execution
     *
     * N.B. commands are NOT gauaranteed to be executed in the order
     * queued. The failure of earlier commands does not impede
     * the execution of subsequent commands
     */
    @Override
    public <T extends CommandResult> void execute(Command<T> command, AsyncMonitor monitor,
                                                  AsyncCallback<T> callback  ) {

        if(monitor==null) {
            monitor = new NullAsyncMonitor();
        }

        monitor.beforeRequest();

        CommandRequest req = new CommandRequest(command, monitor, callback);

        if(!tryLocal(req)) {

            pendingCommands.add(req);

            GWT.log("CommandServiceImpl: Scheduled " + command.toString() + ", now " + pendingCommands.size() + " command(s) pending", null);
        }
    }

    public boolean hasPendingCommands() {
        return pendingCommands.size() != 0;
    }

    /**
     * Executes a collection of pending commands
     */
    public void executePending() {

        if(pendingCommands.size() == 0)
            return;

        final List<CommandRequest> executingCommands = pendingCommands;
        pendingCommands = new ArrayList<CommandRequest>();

        GWT.log("CommandServiceImpl: " + executingCommands.size() + " are pending.", null);

        /*
         * Try first to execute the commands locally using
         * the registered proxies
         */

        int i=0;
        while(i<executingCommands.size()) {

            CommandRequest cmd = executingCommands.get(i);

            if(tryLocal(cmd)) {
                executingCommands.remove(i);
            } else {

                /* Give callers a chance to biff on calls that
                 * are being retried
                 */

                if(cmd.retries > 0 && cmd.getMonitor() != null &&
                        !cmd.getMonitor().onRetrying()) {

                    GWT.log("CommandService: The monitor of " + cmd.toString() + " (" + cmd.getMonitor().toString() +
                            ") has denied a retry attempt after " + cmd.retries +
                            " retries, the command is removed from the queue.", null);

                    executingCommands.remove(i);

                } else {

                    proxyManager.notifyListenersBefore(executingCommands.get(i).getCommand());
                    i++;
                }
            }
        }

        GWT.log("CommandService: sending "  + executingCommands.size() + " to server.", null);


        if(executingCommands.size() == 0)
            return;

        /*
         * Now contact the server to execute the rest
         */

        try {
            service.execute(authentication.getAuthToken(), commandList(executingCommands), new AsyncCallback<List<CommandResult>>() {

                public void onSuccess(List<CommandResult> results) {

                    onRemoteCallSuccess(results, executingCommands);
                }

                public void onFailure(Throwable caught) {

                    remoteServiceFailure(executingCommands, caught);
                }
            });
        } catch(Throwable caught) {

            GWT.log("CommandService: client side exception thrown during execution of remote command", caught);

            onServerError(executingCommands, caught);

        }
    }

    protected void onRemoteCallSuccess(List<CommandResult> results, List<CommandRequest> executingCommands) {
        GWT.log("CommandServiceImpl: remote call succeed", null);

        if(!connected) {
            connected = true;
            eventBus.fireEvent(AppEvents.ConnectionStatusChange, new ConnectionEvent(connected));
        }

        /*
        * Post process results
        */

        for(int i = 0; i!=executingCommands.size(); ++i) {

            CommandRequest cmd = executingCommands.get(i);
            CommandResult result = results.get(i);

            if(result instanceof CommandException) {
                if(result instanceof UnexpectedCommandException) {
                    cmd.getMonitor().onServerError();
                } else {
                    cmd.getMonitor().onCompleted();
                }
                cmd.getCallback().onFailure((CommandException)result);
            } else {
                cmd.getMonitor().onCompleted();
                proxyManager.notifyListenersOfSuccess(cmd.getCommand(), result);
                cmd.getCallback().onSuccess(result);
            }
        }
    }

    protected void remoteServiceFailure(List<CommandRequest> executingCommands, Throwable caught) {
        
        GWT.log("CommandServiceImpl: remote call failed", caught);

        /*
        * Not all failures are created equal.
        * If our authentication token has expired, for
        * example, we just need to login again
        */

        if(caught instanceof InvalidAuthTokenException) {
            /*
             * Our authorization token has expired,
             * requeue these commands and trigger an authentication
             */

            // TODO:
            Window.alert("You are not authenticated. Not clear why this has happened. Try logging in again.");


        } else if(caught instanceof IncompatibleRemoteServiceException) {

            // The correct respone to receiving an instance of this exception in the
            // AsyncCallback.onFailure(Throwable) method is to get the application into
            //  a state where a browser refresh can be done.

            Window.alert("A new version of ActivityInfo has been posted to the server. You will need to refresh before continuing.");


        } else if(caught instanceof StatusCodeException) {

            onServerError(executingCommands, caught);

        } else if(caught instanceof InvocationException) {

            /*
             * Connection problem. Increment the retry counter,
             * and requeue
             */

            if(connected) {
                connected = false;
                eventBus.fireEvent(AppEvents.ConnectionStatusChange, new ConnectionEvent(connected));
            }

            for(CommandRequest cmd : executingCommands) {
                cmd.getMonitor().onConnectionProblem();
                pendingCommands.add(cmd);
            }
        } else {
            for(CommandRequest cmd : executingCommands) {
                cmd.getMonitor().onCompleted();
                cmd.getCallback().onFailure(caught);
            }
        }
    }

    protected void onServerError(List<CommandRequest> executingCommands, Throwable caught) {
        for(CommandRequest cmd : executingCommands) {
            cmd.getMonitor().onServerError();
            cmd.getCallback().onFailure(caught);
        }
    }

    /**
     * Attempts to execute the command locally using one of the registered
     * proxies
     *
     * @param cmd
     * @return
     */
    private boolean tryLocal(CommandRequest cmd) {

        CommandProxyResult r = proxyManager.execute(cmd.getCommand());
        if(r.couldExecute) {
            cmd.getMonitor().onCompleted();
            cmd.getCallback().onSuccess(r.result);

            return true;
        } else {

            return false;
        }

    }

    private List<Command> commandList(List<CommandRequest> pending) {
        List<Command> cmds = new ArrayList<Command>(pending.size());
        for(CommandRequest cmd : pending) {
            cmds.add(cmd.getCommand());
        }
        return cmds;
    }


}
