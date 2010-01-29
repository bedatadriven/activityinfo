package org.activityinfo.client.dispatch;

import org.activityinfo.shared.command.Command;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface DispatchEventSource {
    <T extends Command> void registerListener(Class<T> commandClass, DispatchListener<T> listener);

    <T extends Command> void registerProxy(Class<T> commandClass, CommandProxy<T> proxy);
}
