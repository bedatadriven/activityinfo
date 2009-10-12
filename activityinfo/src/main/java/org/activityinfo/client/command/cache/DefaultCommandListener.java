package org.activityinfo.client.command.cache;

import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class DefaultCommandListener<T extends Command> implements CommandListener<T> {
    
    @Override
    public void beforeCalled(T command) {

    }

    @Override
    public void onSuccess(T command, CommandResult result) {

    }

    @Override
    public void onFailure(T command, Throwable caught) {

    }
}
