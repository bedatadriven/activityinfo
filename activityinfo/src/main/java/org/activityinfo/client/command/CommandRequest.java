package org.activityinfo.client.command;

import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.shared.command.Command;

import com.google.gwt.user.client.rpc.AsyncCallback;/*
 * @author Alex Bertram
 */

public class CommandRequest {


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
