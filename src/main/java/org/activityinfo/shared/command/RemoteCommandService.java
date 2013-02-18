/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command;


import java.util.List;

import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;

import com.google.gwt.user.client.rpc.RemoteService;

public interface RemoteCommandService extends RemoteService {
    
    List<CommandResult> execute(String authToken, List<Command> cmd) throws CommandException;

}
