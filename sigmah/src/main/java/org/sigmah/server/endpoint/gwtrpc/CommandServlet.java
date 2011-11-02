/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.sigmah.server.database.hibernate.dao.AuthenticationDAO;
import org.sigmah.server.database.hibernate.dao.Transactional;
import org.sigmah.server.database.hibernate.entity.Authentication;
import org.sigmah.server.database.hibernate.entity.DomainFilters;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.util.LocaleHelper;
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
import com.teklabs.gwt.i18n.server.LocaleProxy;


/**
 * Process command objects from the client and returns CommandResults.
 * <p/>
 * This servlet is at the heart of the command execution pipeline, but delegates all
 * logic processing to the {@link org.sigmah.shared.command.handler.CommandHandler} corresponding
 * to the given {@link org.sigmah.shared.command.Command}s.
 * <p/>
 * CommandHandlers are loaded based on name from the org.sigmah.server.command.handler package.
 * <p/>
 * E.g. UpdateEntity => org.sigmah.server.command.handler.UpdateEntityHandler
 */
@Singleton
public class CommandServlet extends RemoteServiceServlet implements RemoteCommandService {

    @Inject
    private Injector injector;

    private static final Logger logger = Logger.getLogger(CommandServlet.class);

    @Override
    @LogException
    public List<CommandResult> execute(String authToken, List<Command> commands) throws CommandException {
        Authentication auth = retrieveAuthentication(authToken);
        try {
            return handleCommands(auth.getUser(), commands);

        } catch (Throwable caught) {
            caught.printStackTrace();
            throw new CommandException();
        }
    }

    public CommandResult execute(String authToken, Command command) throws CommandException {
        Authentication auth = retrieveAuthentication(authToken);
        applyUserFilters(auth.getUser());
        LocaleProxy.setLocale(LocaleHelper.getLocaleObject(auth.getUser()));
        return handleCommand(command);
    }

    /**
     * Publicly visible for testing *
     */
    @LogException
    public List<CommandResult> handleCommands(User user, List<Command> commands) {
        applyUserFilters(user);

        List<CommandResult> results = new ArrayList<CommandResult>();
        for (Command command : commands) {
        	
        	logger.debug(user.getEmail() + ": " + command.getClass().getSimpleName());
        	
            try {
                results.add(handleCommand(command));
            } catch (CommandException e) {
                // include this as an error-ful result and
                // continue executing other commands in the list
                results.add(e);
            } catch (Throwable e) {
                // something when wrong while executing the command
                // this is already logged by the logging interceptor
                // so just pass a new UnexpectedCommandException to the client
                results.add(new UnexpectedCommandException(e));
            }
        }
        return results;
    }

    private void applyUserFilters(User user) {
        EntityManager em = injector.getInstance(EntityManager.class);
        DomainFilters.applyUserFilter(user, em);
    }

    @Transactional
    @LogException(emailAlert = true)
    protected CommandResult handleCommand(Command command) throws CommandException {
    	return ServerExecutionContext.execute(injector, command);
    }
    

    private Authentication retrieveAuthentication(String authToken) throws InvalidAuthTokenException {
        AuthenticationDAO authDAO = injector.getInstance(AuthenticationDAO.class);
        Authentication auth = authDAO.findById(authToken);
        if (auth == null) {
            throw new InvalidAuthTokenException();
        }
        return auth;
    }
    
}
