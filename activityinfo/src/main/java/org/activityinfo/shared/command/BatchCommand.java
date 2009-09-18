package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.BatchResult;

import java.util.List;
import java.util.ArrayList;
/*
 * @author Alex Bertram
 */

public class BatchCommand implements Command<BatchResult> {

    private List<Command> commands = new ArrayList<Command>();

    public BatchCommand() {
    }

    public BatchCommand(List<Command> commands) {
        this.commands = commands;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public void add(Command command) {
        commands.add(command);
    }
}
