package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.BatchResult;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Executes a batch of commands atomically.
 *
 * Unlike sending multiple commands to the server using
 * {@link org.activityinfo.shared.command.RemoteCommandService#execute(String, java.util.List)},
 * the commands in the given list are guaranted to be executed in sequence and within
 * a single transaction. If one command fails, all commands will be rolled back and the
 * BatchCommand will fail.
 *
 * Returns {@link org.activityinfo.shared.command.result.BatchResult}
 *
 * @author Alex Bertram
 */
public class BatchCommand implements Command<BatchResult> {

    private List<Command> commands = new ArrayList<Command>();

    public BatchCommand() {
    }

    public BatchCommand(Command... commands) {
        this.commands = new ArrayList<Command>(commands.length);
        for(Command cmd : commands) {
            this.commands.add(cmd);
        }
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
