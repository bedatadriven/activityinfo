package org.activityinfo.server.endpoint.gwtrpc;

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

import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.logging.LogException;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.RemoteCommandService;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.InvalidAuthTokenException;
import org.activityinfo.shared.exception.UnexpectedCommandException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * New hibernate-free command servlet. Currently only used in 
 * test code, will eventually replace CommandServlet when
 * all dependencies on hibernate are removed.
 *
 */
@Singleton
public class CommandServlet2 extends RemoteServiceServlet implements RemoteCommandService  {

    @Inject
    private Injector injector;

    
    private static final Logger LOGGER = Logger.getLogger(CommandServlet2.class.getName());
    
    @Override
    @LogException
    public List<CommandResult> execute(String authToken, List<Command> commands) throws CommandException {
        Authentication auth = retrieveAuthentication(authToken);
        try {
            return handleCommands(auth.getUser(), commands);

        } catch (Exception caught) {
            caught.printStackTrace();
            throw new CommandException();
        }
    }

    public CommandResult execute(String authToken, Command command) throws CommandException {
        Authentication auth = retrieveAuthentication(authToken);
        return handleCommand(auth.getUser(), command);
    }

	private Authentication retrieveAuthentication(String authToken)
			throws InvalidAuthTokenException {
//		Authentication auth = userDAO.findAuthenticationByToken(authToken);        
//		if (auth == null) {
//            throw new InvalidAuthTokenException();
//		}
//		return auth;
		throw new UnsupportedOperationException();
	}

    /**
     * Publicly visible for testing *
     */
    @LogException
    public List<CommandResult> handleCommands(User user, List<Command> commands) {
        List<CommandResult> results = new ArrayList<CommandResult>();
        for (Command command : commands) {
            try {
                results.add(handleCommand(user, command));
            } catch (CommandException e) {
                // include this as an error-ful result and
                // continue executing other commands in the list
                results.add(e);
            } catch (Exception e) {
                // something when wrong while executing the command
                // this is already logged by the logging interceptor
                // so just pass a new UnexpectedCommandException to the client
                results.add(new UnexpectedCommandException(e));
            }
        }
        return results;
    }

    @LogException(emailAlert = true)
    protected CommandResult handleCommand(User user, Command command) throws CommandException {
    	RemoteExecutionContext context = new RemoteExecutionContext(injector);
    	return context.execute(command);
    }
    
   
}
