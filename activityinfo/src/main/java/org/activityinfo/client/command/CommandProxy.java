package org.activityinfo.client.command;

import org.activityinfo.shared.command.Command;

public interface CommandProxy<T extends Command> {
    

    public CommandProxyResult execute(T command);


}
