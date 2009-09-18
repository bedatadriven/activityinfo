package org.activityinfo.client.command;

import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface CommandService {

    <T extends CommandResult> void execute(Command<T> command, AsyncMonitor monitor,
                                                  AsyncCallback<T> callback  );
}
