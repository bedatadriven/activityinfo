package org.activityinfo.client.local.command;

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

import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.MutatingCommand;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.command.result.CommandResult;

import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LocalExecutionContext implements ExecutionContext {

    private AuthenticatedUser user;
    private SqlTransaction tx;
    private HandlerRegistry registry;
    private CommandQueue commandQueue;

    public LocalExecutionContext(AuthenticatedUser user, SqlTransaction tx,
        HandlerRegistry registry,
        CommandQueue commandQueue) {
        super();
        this.user = user;
        this.tx = tx;
        this.registry = registry;
        this.commandQueue = commandQueue;
    }

    @Override
    public boolean isRemote() {
        return false;
    }

    @Override
    public AuthenticatedUser getUser() {
        return user;
    }

    @Override
    public SqlTransaction getTransaction() {
        return tx;
    }

    @Override
    public <C extends Command<R>, R extends CommandResult> void execute(
        final C command, final AsyncCallback<R> callback) {

        registry.getHandler(command).execute(command, this,
            new AsyncCallback<R>() {

                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(R result) {
                    if (command instanceof MutatingCommand) {
                        commandQueue.queue(tx, command);
                    }
                    callback.onSuccess(result);
                }
            });
    }
}
