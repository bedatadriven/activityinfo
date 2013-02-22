package org.activityinfo.server.command;

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


import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.endpoint.gwtrpc.RemoteExecutionContext;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class DispatcherSyncImpl implements DispatcherSync {

	private final Injector injector;
	private final Provider<AuthenticatedUser> userProvider;

	@Inject
	public DispatcherSyncImpl(Injector injector, Provider<AuthenticatedUser> userProvider) {
		this.injector = injector;
		this.userProvider = userProvider;
	}
	
	@Override
	public <C extends Command<R>, R extends CommandResult> R execute(C command) throws CommandException {
		if(RemoteExecutionContext.inProgress()) {
			return RemoteExecutionContext.current().execute(command);
		} else {
			User user = new User();
			user.setId(userProvider.get().getUserId());
			user.setEmail(userProvider.get().getEmail());
			user.setLocale(userProvider.get().getUserLocale());
			
			RemoteExecutionContext context = new RemoteExecutionContext(injector);
			return context.startExecute(command);
		}
	}
}
