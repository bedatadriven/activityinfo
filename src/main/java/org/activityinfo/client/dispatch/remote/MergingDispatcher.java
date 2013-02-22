package org.activityinfo.client.dispatch.remote;

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

import org.activityinfo.client.Log;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * An implementation of {@link org.activityinfo.client.dispatch.Dispatcher} that
 * merges equivalent commands independently executed during the same event loop.
 * 
 * <p>
 * This is critical when we have multiple, independent components that all
 * request GetSchema() or other basic information as they are loaded.
 */
public class MergingDispatcher extends AbstractDispatcher {

    private Dispatcher dispatcher;

    /**
     * Pending commands have been requested but not yet sent to the server
     */
    private List<CommandRequest> pendingCommands = new ArrayList<CommandRequest>();

    /**
     * Executing commands have been sent to the server but for which we have not
     * yet received a response.
     */
    private List<CommandRequest> executingCommands = new ArrayList<CommandRequest>();

    @Inject
    public MergingDispatcher(Dispatcher dispatcher,
        Scheduler scheduler) {
        this.dispatcher = dispatcher;

        scheduler.scheduleFinally(new RepeatingCommand() {

            @Override
            public boolean execute() {
                try {
                    if (!pendingCommands.isEmpty()) {
                        dispatchPending();
                    }
                } catch (Exception e) {
                    Log.error(
                        "Uncaught exception while dispatching in MergingDispatcher",
                        e);
                }
                return true;
            }
        });
    }

    @Override
    public <T extends CommandResult> void execute(Command<T> command,
        AsyncCallback<T> callback) {

        CommandRequest request = new CommandRequest(command, callback);

        if (request.isMutating()) {
            // mutating requests get queued immediately, don't try to merge them
            // into any pending/executing commands, it wouldn't be correct

            queue(request);
        } else {
            if (!request.mergeSuccessfulInto(pendingCommands) &&
                !request.mergeSuccessfulInto(executingCommands)) {

                queue(request);

                Log.debug("MergingDispatcher: Scheduled " + command.toString()
                    + ", now " +
                    pendingCommands.size() + " command(s) pending");
            }
        }
    }

    private void queue(CommandRequest request) {
        pendingCommands.add(request);
    }

    private void dispatchPending() {
        Log.debug("MergingDispatcher: sending " + pendingCommands.size()
            + " to server.");

        final List<CommandRequest> sent = new ArrayList<CommandRequest>(
            pendingCommands);
        executingCommands.addAll(sent);
        pendingCommands.clear();

        if (!sent.isEmpty()) {
            for (final CommandRequest request : sent) {
                dispatcher.execute(request.getCommand(), new AsyncCallback() {

                    @Override
                    public void onFailure(Throwable caught) {
                        executingCommands.remove(request);
                        request.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(Object result) {
                        executingCommands.remove(request);
                        request.onSuccess(result);
                    }
                });
            }
        }
    }
}
