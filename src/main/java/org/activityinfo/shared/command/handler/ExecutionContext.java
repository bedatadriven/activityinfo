package org.activityinfo.shared.command.handler;

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
import org.activityinfo.shared.command.result.CommandResult;

import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExecutionContext {

	AuthenticatedUser getUser();

	boolean isRemote();
	
	/**
	 * 
	 * @return the SqlTransaction associated with the execution of the current command. 
	 * Each command is executed within its own transaction, but any nested command 
	 * will share it's parent transaction.
	 */
	SqlTransaction getTransaction();
	
	
	/**
	 * Executes a nested command
	 * 
	 * @param command the command to execute 
	 * @param callback callback on the command's completion
	 */
	<C extends Command<R>, R extends CommandResult> void execute(C command, AsyncCallback<R> callback);

}
