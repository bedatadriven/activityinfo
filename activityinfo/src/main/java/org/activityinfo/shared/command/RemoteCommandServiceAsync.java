package org.activityinfo.shared.command;


import org.activityinfo.shared.command.AuthenticationResult;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface RemoteCommandServiceAsync
{

    void execute(String authToken, List<Command> cmd, AsyncCallback<List<CommandResult>> results);

}
