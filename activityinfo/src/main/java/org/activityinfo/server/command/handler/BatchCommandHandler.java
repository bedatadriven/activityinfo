/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.command.handler;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.BatchResult;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @see org.activityinfo.shared.command.BatchCommand
 *
 * @author Alex Bertram
 */
public class BatchCommandHandler implements CommandHandler<BatchCommand> {

    private final Injector injector;

    @Inject
    public BatchCommandHandler(Injector injector) {
        this.injector = injector;
    }

    public CommandResult execute(BatchCommand batch, User user) throws CommandException {

        List<CommandResult> results = new ArrayList<CommandResult>();

        for(Command cmd : batch.getCommands()) {

             CommandHandler etor = (CommandHandler) injector.getInstance(
                            HandlerUtil.executorForCommand(cmd));

            results.add( etor.execute(cmd, user) );

        }

        return new BatchResult(results);
    }
}
