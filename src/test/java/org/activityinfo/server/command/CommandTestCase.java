package org.activityinfo.server.command;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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

    protected <T extends CommandResult> T execute(Command<T> command)
        throws CommandException {
        
        
        User user = em.find(User.class, AuthenticationModuleStub
            .getCurrentUser().getUserId());
        assert user != null : "cannot find user id " +
            AuthenticationModuleStub.getCurrentUser().getUserId()
            + " in the database, have you " +
            " called execute() without a @OnDataset annotation?";
        Locale.setDefault(Locale.ENGLISH);

        List<CommandResult> results = servlet.handleCommands(Collections
            .<Command> singletonList(command));

        // normally each request and so each handleCommand() gets its own
        // EntityManager, but here successive requests in the same test
        // will share an EntityManager, which can be bad if there are
        // collections
        // still living in the first-level cache
        //
        // I think these command tests should ultimately become real end-to-end
        // tests and so would go through the actual servlet process, but for the
        // moment,
        // we'll just add this work aroudn that clears the cache after each
        // command.
        em.clear();

        CommandResult result = results.get(0);
        if (result instanceof CommandException) {
            throw (CommandException) result;
        }

        return (T) result;
    }
}
