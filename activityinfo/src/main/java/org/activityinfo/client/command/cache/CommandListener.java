package org.activityinfo.client.command.cache;

import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

/**
 * Provides an interface through which caches can monitor the
 * results of remote service calls.
 *
 * <code>CommandListener</code>s can be connected to the local
 * <code>CommandService</code> by calling Co
 *
 * @param <T> The type of command for which to listen.
 */
public interface CommandListener<T extends Command> {

    public void beforeCalled(T command);

    public void onSuccess(T command, CommandResult result);

    public void onFailure(T command, Throwable caught);

}
