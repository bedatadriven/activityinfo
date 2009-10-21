package org.activityinfo.client.command;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.UnexpectedCommandException;

import java.util.List;
import java.util.ArrayList;


/**
 *
 * Encapsulates a pending command request to the server.
 *
 * @author Alex Bertram
 */
public class CommandRequest {


    /**
     * The pending command
     */
    private final Command command;

    private final List<AsyncMonitor> monitors = new ArrayList<AsyncMonitor>();
    private final List<AsyncCallback> callbacks = new ArrayList<AsyncCallback>();

    public int retries = 0;

    public CommandRequest(Command command, AsyncMonitor monitor, AsyncCallback callback) {
        this.command = command;
        if(monitor != null)
            this.monitors.add(monitor);
        this.callbacks.add(callback);
    }


    public Command getCommand() {
        return command;
    }

    private void fireCompleted() {
        for(AsyncMonitor m : monitors) {
            m.onCompleted();
        }
    }

    public void fireOnFailure(Throwable caught, boolean unexpected) {
        if(unexpected) {
            for(AsyncMonitor m : monitors) {
                m.onServerError();
            }
        } else {
            fireCompleted();
        }
        for(AsyncCallback c : callbacks) {
            c.onFailure(caught);
        }
    }

    public void fireOnSuccess(CommandResult result) {
        fireCompleted();
        for(AsyncCallback c: callbacks) {
            c.onSuccess(result);
        }
    }

    public void fireOnConnectionProblem() {
        for(AsyncMonitor m : monitors) {
            m.onConnectionProblem();
        }
    }

    public boolean fireRetrying() {
        boolean retry = false;
        for(AsyncMonitor m : monitors) {
            if(m.onRetrying()) {
                retry = true;
            }
        }
        return retry;
    }

    public void fireRetriesMaxedOut() {
        for(AsyncMonitor m : monitors) {
            
        }
    }

    public void piggyback(AsyncMonitor monitor, AsyncCallback callback) {
        if(monitor != null) {
            monitors.add(monitor);
        }
        callbacks.add(callback);

        // reset the retry count
        retries = 0;
    }
}
