/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.endpoint.gwtrpc;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.activityinfo.login.shared.AnonymousUser;
import org.activityinfo.login.shared.AuthenticatedUser;
import org.activityinfo.server.DeploymentEnvironment;
import org.activityinfo.server.authentication.ServerSideAuthProvider;
import org.activityinfo.server.database.hibernate.entity.DomainFilters;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.event.CommandEvent;
import org.activityinfo.server.event.ServerEventBus;
import org.activityinfo.server.util.logging.LogException;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.RemoteCommandService;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.UnexpectedCommandException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
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

    private static final Logger LOGGER = Logger.getLogger(CommandServlet.class.getName());

	
    @Inject
    private Injector injector;

    @Inject
    private ServerEventBus serverEventBus;

    @Inject
    private ServerSideAuthProvider authProvider;
    
    private AppEnginePolicyProvider policyProvider;
    
    
    
    @Override
	public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	if(DeploymentEnvironment.isAppEngine()) {
    		policyProvider = new AppEnginePolicyProvider(getServletContext());
    	}
    }



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
        	
        	LOGGER.log(Level.INFO, authProvider.get().getEmail() + ": " + command.getClass().getSimpleName());
        	
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

    @LogException(emailAlert = true)
    protected CommandResult handleCommand(Command command) throws CommandException {
    	long timeStart = System.currentTimeMillis();
    	RemoteExecutionContext context = new RemoteExecutionContext(injector);
		CommandResult result = context.startExecute(command);

		long timeElapsed = System.currentTimeMillis() - timeStart;
		if(timeElapsed > 1000) {
			LOGGER.warning("Command " + command.toString() + " completed in " + timeElapsed + "ms" );
		}
		
		if ( !(result instanceof CommandException))  {
			// if the command completed successfully, notify listeners
			LOGGER.fine("notifying serverEventBus of completed command " + command.toString());
			serverEventBus.post(new CommandEvent(command, result, context));
		}
		
		return result;
    }
    
  	@Override
	public void log(String message, Throwable t) {
		super.log(message, t);
		LOGGER.log(Level.SEVERE, message, t);
	}

	@Override
	public void log(String msg) {
		super.log(msg);
		LOGGER.log(Level.INFO, msg);
	}

	@Override
	protected SerializationPolicy doGetSerializationPolicy(
			HttpServletRequest request, String moduleBaseURL, String strongName) {
		if(policyProvider == null) {
			return super.doGetSerializationPolicy(request, moduleBaseURL, strongName);
		} else {
			return policyProvider.getSerializationPolicy(moduleBaseURL, strongName);
		}
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
