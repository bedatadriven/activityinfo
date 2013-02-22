

package org.activityinfo.client.dispatch;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activityinfo.client.dispatch.remote.AbstractDispatcher;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.BatchResult;
import org.activityinfo.shared.command.result.CommandResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class DispatcherStub extends AbstractDispatcher {

    private Map<Command, CommandResult> results = new HashMap<Command, CommandResult>();
    private Map<Class, CommandResult> resultByClass = new HashMap<Class, CommandResult>();

    private List<Command> log = new ArrayList<Command>();

    public DispatcherStub() {

    }

    @Override
	public <T extends CommandResult> void execute(Command<T> command, AsyncCallback<T> callback) {
		if (command instanceof BatchCommand) {
            BatchCommand batch = (BatchCommand) command;
            List<CommandResult> results = new ArrayList<CommandResult>();
            for (Command batchCmd : batch.getCommands()) {
                results.add(findResult(batchCmd));
            }
            callback.onSuccess((T) new BatchResult(results));
        } else {
            callback.onSuccess((T) findResult(command));
        }
	}

    private CommandResult findResult(Command command) {
        CommandResult result = results.get(command);
        if (result == null) {

            result = resultByClass.get(command.getClass());
            if (result == null) {
                throw new AssertionError("Unexpected command: " + command.toString());
            }
        }
        log.add(command);
        return result;
    }

    public void setResult(Command command, CommandResult result) {
        results.put(command, result);
    }

    public void setResult(Class<? extends Command> commandClass, CommandResult result) {
        resultByClass.put(commandClass, result);
    }

    public void assertExecuteCount(Class commandClass, int expectedCount) {
        int count = 0;
        for (Command cmd : log) {
            if (commandClass.equals(cmd.getClass())) {
                count++;
            }
        }
        if (count != expectedCount) {
            throw new AssertionError("Execution count for " + commandClass.getName() + ": expected : " + expectedCount
                    + " actual count: " + count);

        }
    }

    public <T extends Command> T getLastExecuted(Class commandClass) {
        for (int i = log.size() - 1; i >= 0; i--) {
            if (log.get(i).getClass().equals(commandClass)) {
                return (T) log.get(i);
            }
        }
        throw new AssertionError(commandClass.getName() + " was not excecuted.");
    }

    public void resetLog() {
        log.clear();
    }
}
