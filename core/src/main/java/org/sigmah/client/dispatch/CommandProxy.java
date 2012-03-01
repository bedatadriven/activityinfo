/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch;

import org.sigmah.client.dispatch.remote.cache.ProxyResult;
import org.sigmah.shared.command.Command;

/**
 * Interface to a class which is capable of handling {@link org.sigmah.shared.command.Command}s
 * locally, before they are sent to the server. Implementations include caches and offline implementations
 * relying on a local database.
 *
 * @param <T>
 */
public interface CommandProxy<T extends Command> {

    /**
     * Tries to the execute the command locally.
     *
     * @param command the command to execute
     * @return a ProxyResult instance indicating whether local execution was possible, and the
     * CommandResult if it was.
     */
    public ProxyResult maybeExecute(T command);

}
