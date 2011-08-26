package org.sigmah.server.endpoint.gwtrpc;

import java.util.Collections;
import java.util.List;

import org.sigmah.server.database.TestDatabaseModule;
import org.sigmah.server.database.dao.UserDAO;
import org.sigmah.server.util.TemplateModule;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.Modules;

import com.bedatadriven.rebar.sql.server.jdbc.JdbcScheduler;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Test fixture for running hibernate-free 
 * commands.
 *
 * The future.
 *
 */
@Modules({
        TestDatabaseModule.class,
        TemplateModule.class,
        GwtRpcModule.class
})
public class CommandTestCase2 {
	

    @Inject
    protected CommandServlet2 servlet;
 
    @Inject
    protected Injector injector;

    @Inject
    protected UserDAO users;
    
    protected int userId = 1;


    protected void setUser(int userId) {
        this.userId = userId;
    }

    protected <T extends CommandResult> T execute(Command<T> command) throws CommandException {
        User user = users.findById(userId);
        assert user != null;

        List<CommandResult> results = servlet.handleCommands(user, Collections.<Command>singletonList(command));

        JdbcScheduler.get().forceCleanup();
        
        // normally each request and so each handleCommand() gets its own
        // EntityManager, but here successive requests in the same test
        // will share an EntityManager, which can be bad if there are collections
        // still living in the first-level cache
        //
        // I think these command tests should ultimately become real end-to-end
        // tests and so would go through the actual servlet process, but for the moment,
        // we'll just add this work aroudn that clears the cache after each command.
//        em.clear();


        CommandResult result = results.get(0);
        if (result instanceof CommandException) {
            throw (CommandException) result;
        }

        return (T) result;
    }

}
