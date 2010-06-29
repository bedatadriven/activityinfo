/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch;

import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;

/**
 * Provides an interface through which caches can monitor the
 * execution of remote service calls.
 *
 * @param <T> The type of command for which to listen.
 */
public interface DispatchListener<T extends Command> {

    /**
     * Called just before action is taken to dispatch the given the Command.
     * Listeners can use this event to invalidate caches that would be affected by
     * mutating commands.
     */
    void beforeDispatched(T command);

    /**
     * Called following the successful dispatch of the given command.
     */
    void onSuccess(T command, CommandResult result);

    /**
     * Called following the failure, expected or otherwise, of the given command
     */
    public void onFailure(T command, Throwable caught);

}
