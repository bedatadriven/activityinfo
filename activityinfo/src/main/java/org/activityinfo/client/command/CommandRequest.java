package org.activityinfo.client.command;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.shared.command.Command;


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

    private final AsyncMonitor monitor;
    private final AsyncCallback callback;

    public int retries = 0;

    public CommandRequest(Command command, AsyncMonitor monitor, AsyncCallback callback) {
        this.command = command;
        this.monitor = monitor;
        this.callback = callback;
    }


    public Command getCommand() {
        return command;
    }

    public AsyncMonitor getMonitor() {
        return monitor;
    }

    public AsyncCallback getCallback() {
        return callback;
    }
}
