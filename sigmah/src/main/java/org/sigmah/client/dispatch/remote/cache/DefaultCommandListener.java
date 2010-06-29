package org.sigmah.client.dispatch.remote.cache;

import org.sigmah.client.dispatch.DispatchListener;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class DefaultCommandListener<T extends Command> implements DispatchListener<T> {

    @Override
    public void beforeDispatched(T command) {

    }

    @Override
    public void onSuccess(T command, CommandResult result) {

    }

    @Override
    public void onFailure(T command, Throwable caught) {

    }
}
