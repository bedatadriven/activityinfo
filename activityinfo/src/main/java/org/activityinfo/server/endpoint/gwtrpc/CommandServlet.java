package org.activityinfo.server.endpoint.gwtrpc;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.activityinfo.server.dao.AuthenticationDAO;
import org.activityinfo.server.dao.Transactional;
import org.activityinfo.server.domain.Authentication;
import org.activityinfo.server.domain.DomainFilters;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.endpoint.gwtrpc.handler.CommandHandler;
import org.activityinfo.server.endpoint.gwtrpc.handler.HandlerUtil;
import org.activityinfo.server.util.logging.LogException;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.RemoteCommandService;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.InvalidAuthTokenException;
import org.activityinfo.shared.exception.UnexpectedCommandException;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


/**
 * Process command objects from the client and returns CommandResults.
 * <p/>
 * This servlet is at the heart of the command execution pipeline, but delegates all
 * logic processing to the {@link org.activityinfo.server.endpoint.gwtrpc.handler.CommandHandler} corresponding
 * to the given {@link org.activityinfo.shared.command.Command}s.
 * <p/>
 * CommandHandlers are loaded based on name from the org.activityinfo.server.command.handler package.
 * <p/>
 * E.g. UpdateEntity => org.activityinfo.server.command.handler.UpdateEntityHandler
 */
@Singleton
public class CommandServlet extends RemoteServiceServlet implements RemoteCommandService {

    @Inject
    private Injector injector;

    /**
     * Overrides the default implementation to intercept exceptions (primarily serialization
     * exceptions at this point) and log them.
     *
     * @param arg0
     * @return
     * @throws SerializationException
     */
    @Override
    @LogException(emailAlert = true)
    public String processCall(String arg0) throws SerializationException {
        return super.processCall(arg0);
    }


    @Override
    public List<CommandResult> execute(String authToken, List<Command> commands) throws CommandException {
        Authentication auth = retrieveAuthentication(authToken);
        try {
            return handleCommands(auth.getUser(), commands);

        } catch (Throwable caught) {
            caught.printStackTrace();
            throw new CommandException();
        }
    }

    /**
     * Publicly visible for testing *
     */
    public List<CommandResult> handleCommands(User user, List<Command> commands) {
        applyUserFilters(user);

        List<CommandResult> results = new ArrayList<CommandResult>();
        for (Command command : commands) {
            try {
                results.add(handleCommand(user, command));
            } catch (CommandException e) {
                // include this as an error-ful result and
                // continue executing other commands in the list
                results.add(e);
            } catch (Throwable e) {
                // something when wrong while executing the command
                // this is already logged by the logging interceptor
                // so just pass a new UnexpectedCommandException to the client
                results.add(new UnexpectedCommandException());
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
    protected CommandResult handleCommand(User user, Command command) throws CommandException {
        CommandHandler handler = createHandler(command);
        return handler.execute(command, user);
    }

    private CommandHandler createHandler(Command command) {
        return (CommandHandler) injector.getInstance(
                HandlerUtil.executorForCommand(command));
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
