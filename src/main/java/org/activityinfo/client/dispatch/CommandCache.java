/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch;

import org.activityinfo.client.dispatch.remote.cache.CacheResult;
import org.activityinfo.shared.command.Command;

/**
 * Interface to a class which is capable of handling {@link org.activityinfo.shared.command.Command}s
 * locally, before they are sent to the server.
 *
 * @param <T>
 */
public interface CommandCache<T extends Command> {

    /**
     * Tries to the execute the command locally.
     *
     * @param command the command to execute
     * @return a ProxyResult instance indicating whether local execution was possible, and the
     * CommandResult if it was.
     */
    CacheResult maybeExecute(T command);

    
    /**
     * Clears the cache, discarding any cached data
     */
    void clear();
    
}
