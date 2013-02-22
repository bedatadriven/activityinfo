

package org.activityinfo.shared.command;

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
import java.util.List;

import org.activityinfo.shared.command.result.BatchResult;

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
