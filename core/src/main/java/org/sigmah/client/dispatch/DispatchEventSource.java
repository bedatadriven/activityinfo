/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch;

import org.sigmah.shared.command.Command;

/**
 * Interface to a class that which emits Dispatch-related events.
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface DispatchEventSource {
    /**
     * Registers a DispatchListener with the source. DispatchListeners are called before and
     * after commands are dispatched.
     * @param commandClass The class of Command for which to listen
     * @param listener
     * @param <T> A Command class implementing {@link org.sigmah.shared.command.Command}
     */
    <T extends Command> void registerListener(Class<T> commandClass, DispatchListener<T> listener);

    /**
     * Registers a CommandProxy with the source. Command proxies are called before commands are sent
     * to the server.
     * @param commandClass  The class of Command for which to proxy
     * @param proxy
     * @param <T> A Command class implementing {@link org.sigmah.shared.command.Command}
     */
    <T extends Command> void registerProxy(Class<T> commandClass, CommandProxy<T> proxy);
}
