package org.activityinfo.server.servlet;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.activityinfo.server.command.handler.CommandHandler;
import org.activityinfo.server.command.handler.HandlerUtil;
import org.activityinfo.server.dao.AuthDAO;
import org.activityinfo.server.domain.Authentication;
import org.activityinfo.server.domain.DomainFilters;
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
 * Proccess command objects from the client and returns CommandResults.
 *
 * This servlet is at the heart of the command execution pipeline, but delegates all
 * logic processing to the {@link org.activityinfo.server.command.handler.CommandHandler} corresponding
 * to the given {@link org.activityinfo.shared.command.Command}s.
 *
 * CommandHandlers are loaded based on name from the org.activityinfo.server.command.handler package.
 *
 * E.g. UpdateEntity => org.activityinfo.server.command.handler.UpdateEntityHandler
 *
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
	public String processCall(String arg0) throws SerializationException {
        String result;
		try {
			result = super.processCall(arg0);
        } catch(SerializationException ex) {
            ex.printStackTrace();
            throw ex;
		} catch(Throwable ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
		return result;
	}


    @Override
	public List<CommandResult> execute(String authToken, List<Command> commands) throws CommandException {
			

		EntityManager em = injector.getInstance(EntityManager.class);

       /*
         * Retrieve the current user based from the authorization
         * token (authToken) and apply filters
         */
        AuthDAO authDAO = injector.getInstance(AuthDAO.class);
        Authentication session = authDAO.getSession(authToken);
        if(session == null) {
            throw new InvalidAuthTokenException();
        }

		try {

            DomainFilters.applyUserFilter(session.getUser(), em);

            List<CommandResult> results = new ArrayList<CommandResult>();

            for(Command command : commands) {

                em.getTransaction().begin();
                
                /*
                 * Instantiate the handler for this particular command
                 */
                try {

                    CommandHandler etor = (CommandHandler) injector.getInstance(
                            HandlerUtil.executorForCommand(command));


                    results.add( etor.execute(command, session.getUser()) );

                    em.getTransaction().commit();

                } catch(CommandException ce) {
                    em.getTransaction().rollback();
                    ce.printStackTrace();
                    results.add( ce );
                } catch(Throwable caught) {
                    em.getTransaction().rollback();
                    caught.printStackTrace();
                    results.add( new UnexpectedCommandException() );
                }
            }


			return results;

		} catch (Throwable caught) {
            caught.printStackTrace();
			throw new CommandException();
		}
				
	}



}
