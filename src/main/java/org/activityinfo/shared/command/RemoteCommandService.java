package org.activityinfo.shared.command;


import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;

import com.google.gwt.user.client.rpc.RemoteService;

import java.util.List;

public interface RemoteCommandService extends RemoteService {
    
    List<CommandResult> execute(String authToken, List<Command> cmd) throws CommandException;

}
