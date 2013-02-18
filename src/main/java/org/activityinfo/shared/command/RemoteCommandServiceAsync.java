/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command;


import java.util.List;

import org.activityinfo.shared.command.result.CommandResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteCommandServiceAsync
{

    void execute(String authToken, List<Command> cmd, AsyncCallback<List<CommandResult>> results);

}
