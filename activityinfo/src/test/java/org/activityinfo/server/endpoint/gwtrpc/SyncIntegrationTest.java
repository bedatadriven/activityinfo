/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.server.endpoint.gwtrpc;

import com.bedatadriven.rebar.sync.client.BulkUpdater;
import com.bedatadriven.rebar.sync.mock.MockBulkUpdater;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.offline.sync.Synchronizer;
import org.activityinfo.server.dao.OnDataSet;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.util.logging.LoggingModule;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.MockHibernateModule;
import org.activityinfo.test.Modules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/schema1.db.xml")
@Modules({
        MockHibernateModule.class,
        GwtRpcModule.class,
        LoggingModule.class
})
public class SyncIntegrationTest {

    @Inject
    private CommandServlet servlet;
    private User user;
    private Dispatcher dispatcher;
    private Connection localDbConnection;
    private BulkUpdater updater;

    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        user = new User();
        user.setId(1);
        dispatcher = new MockDispatcher();
        
        Class.forName("org.sqlite.JDBC");
        localDbConnection = DriverManager.getConnection("jdbc:sqlite:test.db");
        updater = new MockBulkUpdater(localDbConnection);
    }

    @Test
    public void run() throws SQLException {
        Synchronizer syncr = new Synchronizer(dispatcher, localDbConnection, updater);
        syncr.start();
    }


    private class MockDispatcher implements Dispatcher {
        @Override
        public <T extends CommandResult> void execute(Command<T> command, AsyncMonitor monitor, AsyncCallback<T> callback) {
            List<CommandResult> results = servlet.handleCommands(user, Collections.<Command>singletonList(command));
            CommandResult result = results.get(0);
            if(result instanceof Exception)
                throw new Error((Throwable) result);
            else
                callback.onSuccess((T) result);
        }
    }
}
