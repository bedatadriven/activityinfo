package org.activityinfo.server.command.handler;

import org.activityinfo.server.domain.User;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.BatchResult;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;

import com.google.inject.Injector;
import com.google.inject.Inject;

import java.util.List;
import java.util.ArrayList;
/*
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
