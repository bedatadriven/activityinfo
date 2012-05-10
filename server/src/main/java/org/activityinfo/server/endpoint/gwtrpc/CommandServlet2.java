package org.activityinfo.server.endpoint.gwtrpc;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.server.database.dao.UserDAO;
import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.logging.LogException;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.RemoteCommandService;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.InvalidAuthTokenException;
import org.activityinfo.shared.exception.UnexpectedCommandException;
import org.apache.log4j.Logger;

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

    @Inject 
    private UserDAO userDAO;
    
    private static final Logger LOGGER = Logger.getLogger(CommandServlet2.class);
    
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
		Authentication auth = userDAO.findAuthenticationByToken(authToken);        
		if (auth == null) {
            throw new InvalidAuthTokenException();
		}
		return auth;
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
    	return ServerExecutionContext.execute(injector, command);
    }
    
   
}
