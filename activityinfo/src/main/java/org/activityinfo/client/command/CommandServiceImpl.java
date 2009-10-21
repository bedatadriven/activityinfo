package org.activityinfo.client.command;

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
import org.activityinfo.client.command.cache.CommandListener;
import org.activityinfo.client.command.cache.CommandProxy;
import org.activityinfo.client.command.cache.CommandProxyResult;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.command.monitor.NullAsyncMonitor;
import org.activityinfo.client.event.ConnectionEvent;
import org.activityinfo.client.util.ITimer;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.RemoteCommandServiceAsync;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.command.MutatingCommand;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.InvalidAuthTokenException;
import org.activityinfo.shared.exception.UnexpectedCommandException;
import org.activityinfo.shared.report.ExportService;
import org.activityinfo.shared.report.ExportServiceAsync;
import org.activityinfo.shared.report.model.ReportElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * An implementation of {@link org.activityinfo.client.command.CommandService} that
 * provides for baching of commands at 200 ms, pluggable caches, and retrying.
 */
@Singleton
public class CommandServiceImpl implements CommandService, CommandEventSource {

    protected final EventBus eventBus;
    protected final RemoteCommandServiceAsync service;
    protected final ExportServiceAsync exportService;
    protected final Authentication authentication;

    protected boolean connected = false;

    protected ProxyManager proxyManager = new ProxyManager();

    protected List<CommandRequest> pendingCommands = new ArrayList<CommandRequest>();
    protected List<CommandRequest> allExecutingCommands = new ArrayList<CommandRequest>();

    protected final AsyncMonitor nullMonitor = new NullAsyncMonitor();

    @Inject
    public CommandServiceImpl(RemoteCommandServiceAsync service,
                              ExportServiceAsync exportService,
                              EventBus eventBus, ITimer timer, Authentication authentication) {
        this.service = service;
        this.exportService = exportService;
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

        if(monitor!=null)
            monitor.beforeRequest();

        // is this command identical to an existing command?
        if(tryToPiggyBack(pendingCommands, command, monitor, callback) ||
           tryToPiggyBack(allExecutingCommands, command, monitor, callback)) {

            return;
        }

        // nope. create a new request wrapper
        pendingCommands.add(new CommandRequest(command, monitor, callback));
        GWT.log("CommandServiceImpl: Scheduled " + command.toString() + ", now " +
                pendingCommands.size() + " command(s) pending", null);
    }

    private boolean tryToPiggyBack(List<CommandRequest> cmds, Command cmd, AsyncMonitor monitor, AsyncCallback callback) {
        for(int i=cmds.size()-1;i>=0; --i) {
            if(cmds.get(i).getCommand() instanceof MutatingCommand)
                return false;
            if(cmds.get(i).getCommand().equals(cmd)) {

                GWT.log("CommandService: merging " + cmd.toString() + " with pending/executing command " +
                    cmds.get(i).getCommand().toString(), null);

                cmds.get(i).piggyback(monitor, callback);
                return true;
            }
        }
        return false;
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

                if(cmd.retries > 0 && !cmd.fireRetrying()) {

                    GWT.log("CommandService: The monitor " +
                            " has denied a retry attempt after " + cmd.retries +
                            " retries, the command is removed from the queue.", null);

                    executingCommands.remove(i);
                    cmd.fireRetriesMaxedOut();

                } else {

                    proxyManager.notifyListenersBefore(executingCommands.get(i).getCommand());
                    i++;
                }
            }
        }

        GWT.log("CommandService: sending "  + executingCommands.size() + " to server.", null);


        if(executingCommands.size() == 0)
            return;

        allExecutingCommands.addAll(executingCommands);

        /*
         * Now contact the server to execute the rest
         */

        try {
            service.execute(authentication.getAuthToken(), commandList(executingCommands), new AsyncCallback<List<CommandResult>>() {
                public void onSuccess(List<CommandResult> results) {
                    allExecutingCommands.removeAll(executingCommands);
                    onRemoteCallSuccess(results, executingCommands);
                }

                public void onFailure(Throwable caught) {
                    allExecutingCommands.removeAll(executingCommands);
                    remoteServiceFailure(executingCommands, caught);
                }
            });
        } catch(Throwable caught) {
            allExecutingCommands.removeAll(executingCommands);
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
                cmd.fireOnFailure((CommandException)result,
                        result instanceof UnexpectedCommandException);

            } else {
                cmd.fireOnSuccess(result);
                proxyManager.notifyListenersOfSuccess(cmd.getCommand(), result);
            }
        }
    }

    protected void remoteServiceFailure(List<CommandRequest> executingCommands, Throwable caught) {
        
        GWT.log("CommandServiceImpl: remote call failed", caught);

        /*
        * Not all failures are created equal.
        * Sort through the harry mess of exceptions to figure out how
        * to present this to the user.
        */

        if(caught instanceof InvalidAuthTokenException) {
            /*
             * Our authorization token has expired,
             * requeue these commands and trigger an authentication
             */

            // TODO: for the moment our auth tokens don't expire. If at some point
            // we implement more aggressive security this needs to be handled gracefully
            // on the client.
            Window.alert("You are not authenticated. Not clear why this has happened. Try logging in again.");


        } else if(caught instanceof IncompatibleRemoteServiceException) {

            // The correct respone to receiving an instance of this exception in the
            // AsyncCallback.onFailure(Throwable) method is to get the application into
            //  a state where a browser refresh can be done.

            Window.alert("A new version of ActivityInfo has been posted to the server. You will need to refresh before continuing.");


        } else if(caught instanceof StatusCodeException) {

            int code = ((StatusCodeException) caught).getStatusCode();

            // TODO: handle 404s and other indications of temporary service inavailability
            // (different than 500 which means we screwed up on the server)

            // internal server error. This shouldn't happen so probably
            // indicates a pretty serious error.

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
                cmd.fireOnConnectionProblem();
                pendingCommands.add(cmd);
            }
        } else {
            for(CommandRequest cmd : executingCommands) {
                cmd.fireOnFailure(caught, true);
            }
        }
    }

    protected void onServerError(List<CommandRequest> executingCommands, Throwable caught) {
        for(CommandRequest cmd : executingCommands) {
            cmd.fireOnFailure(caught, true);
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
            cmd.fireOnSuccess(r.result);
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

    @Override
    public void export(ReportElement element, RenderElement.Format format, final AsyncMonitor monitor, final AsyncCallback<Void> callback) {
        exportService.export(authentication.getAuthToken(), element, format, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                monitor.onCompleted();
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(String result) {
                callback.onSuccess(null);
            }
        });
    }
}
