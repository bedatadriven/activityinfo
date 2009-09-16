package org.activityinfo.server.servlet;

import javax.persistence.EntityManager;

import org.activityinfo.server.command.handler.CommandHandler;
import org.activityinfo.server.command.handler.HandlerUtil;
import org.activityinfo.server.dao.AuthDAO;
import org.activityinfo.server.domain.Authentication;
import org.activityinfo.server.domain.DomainFilters;
import org.activityinfo.server.service.Authenticator;
import org.activityinfo.shared.command.AuthenticationResult;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.RemoteCommandService;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.InvalidAuthTokenException;
import org.activityinfo.shared.exception.InvalidLoginException;
import org.activityinfo.shared.exception.UnexpectedCommandException;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import java.util.List;
import java.util.ArrayList;


@Singleton
public class RemoteCommandServlet extends RemoteServiceServlet implements RemoteCommandService {

	@Inject
	private Injector injector;
	
	@Override
	public String processCall(String arg0) throws SerializationException {
        String result;
		try {
			result = super.processCall(arg0);
		} catch(Throwable ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
		return result;
	}


    @Override
	public List<CommandResult> execute(String authToken, List<Command> commands) throws CommandException {
			

		EntityManager em = injector.getInstance(EntityManager.class);
		em.getTransaction().begin();

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

                /*
                 * Instantiate the handler for this particular command
                 */
                try {

                    CommandHandler etor = (CommandHandler) injector.getInstance(
                            HandlerUtil.executorForCommand(command));


                    results.add( etor.execute(command, session.getUser()) );

                } catch(CommandException ce) {
                    ce.printStackTrace();
                    results.add( ce );
                } catch(Throwable caught) {
                    caught.printStackTrace();
                    results.add( new UnexpectedCommandException() );
                }
            }

			em.getTransaction().commit();

			return results;

		} catch (Throwable caught) {
			em.getTransaction().rollback();
            caught.printStackTrace();
			throw new CommandException();
		}
				
	}



}
