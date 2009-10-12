package org.activityinfo.shared.command;


import com.google.gwt.user.client.rpc.RemoteService;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;

import java.util.List;

public interface RemoteCommandService extends RemoteService {
    
    List<CommandResult> execute(String authToken, List<Command> cmd) throws CommandException;

}
