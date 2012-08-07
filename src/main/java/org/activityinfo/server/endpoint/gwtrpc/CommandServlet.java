/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.endpoint.gwtrpc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.activityinfo.server.authentication.ServerSideAuthProvider;
import org.activityinfo.server.database.hibernate.dao.Transactional;
import org.activityinfo.server.database.hibernate.entity.DomainFilters;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.logging.LogException;
import org.activityinfo.server.util.monitoring.DatadogClient;
import org.activityinfo.server.util.monitoring.Event;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.RemoteCommandService;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.AnonymousUser;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.UnexpectedCommandException;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;


/**
 * Process command objects from the client and returns CommandResults.
 * <p/>
 * This servlet is at the heart of the command execution pipeline, but delegates all
 * logic processing to the {@link org.activityinfo.server.command.handler.CommandHandler} corresponding
 * to the given {@link org.activityinfo.shared.command.Command}s.
 * <p/>
 * CommandHandlers are loaded based on name from the org.activityinfo.server.command.handler package.
 * <p/>
 * E.g. UpdateEntity => org.activityinfo.server.command.handler.UpdateEntityHandler
 */
@Singleton
public class CommandServlet extends RemoteServiceServlet implements RemoteCommandService {

    private static final Logger LOGGER = Logger.getLogger(CommandServlet.class);

	
    @Inject
    private Injector injector;

    @Inject
    private ServerSideAuthProvider authProvider;

    @Inject
    private DatadogClient datadog;
       
    @Override
    @LogException
    public List<CommandResult> execute(String authToken, List<Command> commands) throws CommandException {
    	checkAuthentication(authToken);
    	
        try {
            return handleCommands(commands);

        } catch (Exception caught) {
            throw new CommandException();
        }
    }

    public CommandResult execute(String authToken, Command command) throws CommandException {
    	checkAuthentication(authToken);
    	applyUserFilters();
        return handleCommand(command);
    }

    /**
     * Publicly visible for testing *
     */
    @LogException
    public List<CommandResult> handleCommands(List<Command> commands) {
        applyUserFilters();

        List<CommandResult> results = new ArrayList<CommandResult>();
        for (Command command : commands) {
        	
        	LOGGER.debug(authProvider.get().getEmail() + ": " + command.getClass().getSimpleName());
        	
            try {
                results.add(handleCommand(command));
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

    private void applyUserFilters() {
        EntityManager em = injector.getInstance(EntityManager.class);
        User user = em.getReference(User.class, authProvider.get().getUserId());
        DomainFilters.applyUserFilter(user, em);
    }

    @Transactional
    @LogException(emailAlert = true)
    protected CommandResult handleCommand(Command command) throws CommandException {
    	long timeStart = System.currentTimeMillis();
		CommandResult result = ServerExecutionContext.execute(injector, command);

		long timeElapsed = System.currentTimeMillis() - timeStart;
		LOGGER.info("Command " + command.getClass().getSimpleName() + " completed in " + timeElapsed + "ms" );
		if(timeElapsed > 1000) {
			postSlowEvent(command, timeElapsed);
		}
		return result;
    }
    
    private void postSlowEvent(Command command, long timeElapsed) {
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	
    	Event event = new Event();
    	event.setAlertType(Event.WARNING);
    	event.setTitle("Slow RPC: " + command.getClass().getSimpleName() + " [" + timeElapsed + "ms]");
    	event.setText(gson.toJson(command));
    	event.setAggregationKey("Slow" + command.getClass().getSimpleName());
    	datadog.postEvent(event);
	}

	private void checkAuthentication(String authToken) {
    	if(authToken.equals(AnonymousUser.AUTHTOKEN)) {
    		authProvider.set(AuthenticatedUser.getAnonymous());
    	} else {
    		
    		// TODO(alex): renable this check once the authToken has been removed from the host
    		// page
    		
//    		// user is already authenticated, but ensure that the authTokens match
//    		if(!authToken.equals(authProvider.get().getAuthToken())) {
//    			throw new InvalidAuthTokenException("Auth Tokens do not match, possible XSRF attack");
//    		}
    	}
    }
}
