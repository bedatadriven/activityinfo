package org.activityinfo.client.command;

import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

public interface CommandListener<T extends Command> {

    public void beforeCalled(T command);

    public void onSuccess(T command, CommandResult result);

    public void onFailure(T command, Throwable caught);

}
