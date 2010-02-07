package org.activityinfo.client.dispatch.remote;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.MutatingCommand;
import org.activityinfo.shared.command.result.CommandResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
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
        if (monitor != null)
            this.monitors.add(monitor);
        this.callbacks.add(callback);
    }


    public Command getCommand() {
        return command;
    }

    public Collection<AsyncCallback> getCallbacks() {
        return Collections.unmodifiableCollection(this.callbacks);
    }

    public Collection<AsyncMonitor> getMonitors() {
        return Collections.unmodifiableCollection(this.monitors);
    }

    private void fireCompleted() {
        for (AsyncMonitor m : monitors) {
            m.onCompleted();
        }
    }

    public void fireOnFailure(Throwable caught, boolean unexpected) {
        if (unexpected) {
            for (AsyncMonitor m : monitors) {
                m.onServerError();
            }
        } else {
            fireCompleted();
        }
        for (AsyncCallback c : callbacks) {
            c.onFailure(caught);
        }
    }

    public void fireOnSuccess(CommandResult result) {
        fireCompleted();
        for (AsyncCallback c : callbacks) {
            try {
                c.onSuccess(result);
            } catch (Throwable e) {
                Log.error("Exception thrown during callback on AsyncCallback.onSuccess() for " + command.toString(), e);
            }
        }
    }

    public void fireOnConnectionProblem() {
        for (AsyncMonitor m : monitors) {
            m.onConnectionProblem();
        }
    }

    public boolean fireRetrying() {
        boolean retry = false;
        for (AsyncMonitor m : monitors) {
            if (m.onRetrying()) {
                retry = true;
            }
        }
        return retry;
    }

    public void fireRetriesMaxedOut() {
        for (AsyncMonitor m : monitors) {

        }
    }

    public void fireBeforeRequest() {
        for (AsyncMonitor m : monitors) {
            m.beforeRequest();
        }
    }

    public boolean mergeSuccessfulInto(List<CommandRequest> list) {
        for (CommandRequest request : list) {
            if (command.equals(request.getCommand())) {
                request.merge(this);
                return true;
            }
        }
        return false;
    }

    private void merge(CommandRequest request) {
        Log.debug("Dispatcher: merging " + request.getCommand().toString() + " with pending/executing command " +
                getCommand().toString());

        monitors.addAll(request.monitors);
        callbacks.addAll(request.callbacks);

        // reset the retry count
        retries = 0;
    }

    /**
     * True if this CommandRequest is expected to mutate (change) the state
     * of the remote server.
     */
    public boolean isMutating() {
        return command instanceof MutatingCommand;
    }
}
