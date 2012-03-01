package org.sigmah.server.endpoint.gwtrpc;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.sigmah.server.database.dao.UserDAO;
import org.sigmah.server.database.hibernate.entity.Authentication;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.util.logging.LogException;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.RemoteCommandService;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.InvalidAuthTokenException;
import org.sigmah.shared.exception.UnexpectedCommandException;

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
