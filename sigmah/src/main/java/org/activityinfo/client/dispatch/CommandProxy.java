package org.activityinfo.client.dispatch;

import org.activityinfo.client.dispatch.remote.cache.CommandProxyResult;
import org.activityinfo.shared.command.Command;

public interface CommandProxy<T extends Command> {


    public CommandProxyResult execute(T command);


}
