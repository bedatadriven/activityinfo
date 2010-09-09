/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.sigmah.shared.command.BatchCommand;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.BatchResult;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.exception.CommandException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.BatchCommand
 */
public class BatchCommandHandler implements CommandHandler<BatchCommand> {

    private final Injector injector;

    @Inject
    public BatchCommandHandler(Injector injector) {
        this.injector = injector;
    }

    public CommandResult execute(BatchCommand batch, User user) throws CommandException {

        List<CommandResult> results = new ArrayList<CommandResult>();

        for (Command cmd : batch.getCommands()) {

            CommandHandler etor = (CommandHandler) injector.getInstance(
                    HandlerUtil.executorForCommand(cmd));

            results.add(etor.execute(cmd, user));

        }

        return new BatchResult(results);
    }
}
