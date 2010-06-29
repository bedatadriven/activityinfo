package org.sigmah.shared.command;


import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.shared.command.result.CommandResult;

import java.util.List;

public interface RemoteCommandServiceAsync
{

    void execute(String authToken, List<Command> cmd, AsyncCallback<List<CommandResult>> results);

}
