/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch;

import org.sigmah.client.dispatch.remote.cache.CommandProxyResult;
import org.sigmah.shared.command.Command;

public interface CommandProxy<T extends Command> {


    public CommandProxyResult execute(T command);


}
