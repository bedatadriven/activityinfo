/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command;


import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;

import org.activityinfo.server.authentication.AuthenticationModuleStub;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.endpoint.gwtrpc.CommandServlet;
import org.activityinfo.server.endpoint.gwtrpc.GwtRpcModule;
import org.activityinfo.server.i18n.LocaleModule;
import org.activityinfo.server.util.TemplateModule;
import org.activityinfo.server.util.beanMapping.BeanMappingModule;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.MockHibernateModule;
import org.activityinfo.test.Modules;

import com.google.inject.Inject;
import com.google.inject.Injector;

@Modules({
        MockHibernateModule.class,
        TemplateModule.class,
        BeanMappingModule.class,
        GwtRpcModule.class,
        AuthenticationModuleStub.class,
        LocaleModule.class
})
public abstract class CommandTestCase {

    @Inject
    protected CommandServlet servlet;
    @Inject
    protected EntityManager em;

    @Inject
    protected Injector injector;


    protected void setUser(int userId) {
    	AuthenticationModuleStub.setUserId(userId);
    }

    protected <T extends CommandResult> T execute(Command<T> command) throws CommandException {
        User user = em.find(User.class, AuthenticationModuleStub.getCurrentUser().getUserId());
        assert user != null : "cannot find user id " + 
        	AuthenticationModuleStub.getCurrentUser().getUserId() + " in the database, have you " +
        	" called execute() without a @OnDataset annotation?";
        Locale.setDefault(Locale.ENGLISH);

        List<CommandResult> results = servlet.handleCommands(Collections.<Command>singletonList(command));

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
        if (result instanceof CommandException) {
            throw (CommandException) result;
        }

        return (T) result;
    }
}
