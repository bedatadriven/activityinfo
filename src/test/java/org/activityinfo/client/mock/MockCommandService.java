package org.activityinfo.client.mock;

import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.BatchResult;
import org.activityinfo.shared.command.result.CommandResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MockCommandService implements CommandService {

    private Map<Command, CommandResult> results = new HashMap<Command, CommandResult>();
    private Map<Class, CommandResult> resultByClass = new HashMap<Class, CommandResult>();

    private List<Command> log = new ArrayList<Command>();

    public MockCommandService() {

    }

    @Override
    public <T extends CommandResult> void execute(Command<T> command, AsyncMonitor monitor, AsyncCallback<T> callback) {

        if(command instanceof BatchCommand) {
            BatchCommand batch = (BatchCommand)command;
            List<CommandResult> results = new ArrayList<CommandResult>();
            for(Command batchCmd : batch.getCommands()) {
                results.add(findResult(batchCmd));
            }
            callback.onSuccess((T)new BatchResult(results));
        } else {
           callback.onSuccess((T)findResult(command));
        }
    }

    private CommandResult findResult(Command command) {
        CommandResult result = results.get(command);
        if(result == null) {

            result = resultByClass.get(command.getClass());
            if(result == null) {
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
        for(Command cmd : log) {
            if(commandClass.equals(cmd.getClass())) {
                count ++;
            }
        }
        if(count != expectedCount) {
            throw new AssertionError("Execution count for " + commandClass.getName() + ": expected : " + expectedCount
                + " actual count: " + count);
            
        }
    }

    public <T extends Command> T getLastExecuted(Class commandClass) {
        for(int i= log.size()-1; i>=0; i--) {
            if(log.get(i).getClass().equals(commandClass)) {
                return (T)log.get(i);
            }
        }
        throw new AssertionError(commandClass.getName() + " was not excecuted.");
    }

    public void resetLog() {
        log.clear();
    }
}
