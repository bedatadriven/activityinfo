package org.activityinfo.server.command.handler;

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
import java.util.logging.Logger;

import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.command.result.BatchResult;
import org.activityinfo.shared.command.result.CommandResult;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.BatchCommand
 */
public class BatchCommandHandler implements
    CommandHandlerAsync<BatchCommand, BatchResult> {

    private static final Logger LOGGER = Logger
        .getLogger(BatchCommandHandler.class.getName());

    @Override
    public void execute(BatchCommand batch, ExecutionContext context,
        final AsyncCallback<BatchResult> callback) {

        if (batch.getCommands().isEmpty()) {
            LOGGER.warning("Received empty batch command");
            callback.onSuccess(new BatchResult(Lists
                .<CommandResult>newArrayList()));
        } else {
            final ArrayList<CommandResult> results = new ArrayList<CommandResult>();
            for (Command command : batch.getCommands()) {
                results.add(null);
            }
            final boolean[] finished = new boolean[batch.getCommands().size()];
            final List<Throwable> exceptions = Lists.newArrayList();

            for (int i = 0; i != batch.getCommands().size(); ++i) {
                final int commandIndex = i;
                context.execute(batch.getCommands().get(i),
                    new AsyncCallback<CommandResult>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            if (exceptions.isEmpty()) {
                                exceptions.add(caught);
                                callback.onFailure(caught);
                            }
                        }

                        @Override
                        public void onSuccess(CommandResult result) {
                            results.set(commandIndex, result);
                            finished[commandIndex] = true;
                            if (all(finished)) {
                                callback.onSuccess(new BatchResult(results));
                            }
                        }

                    });

            }
        }
    }

    private boolean all(boolean[] finished) {
        for (int i = 0; i != finished.length; ++i) {
            if (!finished[i]) {
                return false;
            }
        }
        return true;
    }
}
