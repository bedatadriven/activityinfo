/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

/**
 * Supports the execution of {@link org.sigmah.shared.command.Command},
 * either remotely or through local proxies/caches.
 */
package org.sigmah.client.dispatch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;

/**
 * Executes {@link org.sigmah.shared.command.Command}s on behalf of client code. The implementation handles
 * authentication with the server, batching commands, caching, and
 * everything else complicated.
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface Dispatcher {

    /**
     * Attempts to execute a command. The command may not neccessarily be executed
     * immediately, and there are no gaurantees that commands will complete in the order
     * they were submitted.
     *
     * @param command  The command to execute
     * @param monitor  The monitor which handles status reports to the user
     * @param callback The callback which implements application logic
     * @param <T>      The Command subclass
     */
    <T extends CommandResult> void execute(Command<T> command, AsyncMonitor monitor,
                                           AsyncCallback<T> callback);


}
