package org.activityinfo.client.command;

import org.activityinfo.client.command.cache.CommandListener;
import org.activityinfo.client.command.cache.CommandProxy;
import org.activityinfo.shared.command.Command;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface CommandEventSource {
    <T extends Command> void registerListener(Class<T> commandClass, CommandListener<T> listener);

    <T extends Command> void registerProxy(Class<T> commandClass, CommandProxy<T> proxy);
}
