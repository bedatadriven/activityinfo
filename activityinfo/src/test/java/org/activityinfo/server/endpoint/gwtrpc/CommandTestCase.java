package org.activityinfo.server.endpoint.gwtrpc;


import com.google.inject.Inject;
import com.google.inject.Injector;
import org.activityinfo.server.BeanMappingModule;
import org.activityinfo.server.TemplateModule;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.Modules;
import org.activityinfo.test.TestingHibernateModule;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

@Modules({
        TestingHibernateModule.class,
        TemplateModule.class,
        BeanMappingModule.class,
        GwtRpcModule.class
})
public abstract class CommandTestCase {

    @Inject
    protected CommandServlet servlet;
    @Inject
    protected EntityManager em;

    @Inject
    protected Injector injector;

    protected int userId = 1;


    protected void setUser(int userId) {
        this.userId = userId;
    }

    protected <T extends CommandResult> T execute(Command<T> command) throws CommandException {
        User user = em.find(User.class, userId);
        assert user != null;

        List<CommandResult> results = servlet.handleCommands(user, Collections.<Command>singletonList(command));

        // normally each request and so each handleCommand() gets its own
        // EntityManager, but here successive requests in the same test
        // will share an EntityManager, which can be bad if there are collections
        // still living in the first-level cache
        //
        // I think these command tests should ultimately become real end-to-end
        // tests and so would go through the actual servlet process, but for the moment,
        // we'll just add this work aroudn that clears the cache after each command.
        em.clear();


        CommandResult result = results.get(0);
        if (result instanceof CommandException)
            throw (CommandException) result;

        return (T) result;
    }
}
