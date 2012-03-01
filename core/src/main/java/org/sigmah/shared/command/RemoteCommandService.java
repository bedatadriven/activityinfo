/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;


import java.util.List;

import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;

import com.google.gwt.user.client.rpc.RemoteService;

public interface RemoteCommandService extends RemoteService {
    
    List<CommandResult> execute(String authToken, List<Command> cmd) throws CommandException;

}
