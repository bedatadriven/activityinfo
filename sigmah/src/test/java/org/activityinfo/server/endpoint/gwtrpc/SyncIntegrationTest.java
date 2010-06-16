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

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.sync.client.BulkUpdaterAsync;
import com.bedatadriven.rebar.sync.mock.MockBulkUpdater;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.remote.Authentication;
import org.activityinfo.client.offline.sync.Synchronizer;
import org.activityinfo.server.dao.OnDataSet;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.util.logging.LoggingModule;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.SyncRegionUpdate;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.MockHibernateModule;
import org.activityinfo.test.Modules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.sql.*;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
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
    private BulkUpdaterAsync updater;

    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        user = new User();
        user.setId(1);
        dispatcher = new MockDispatcher();
        
        File dbFile = new File("test.db");
        if(dbFile.exists()) {
            if(!dbFile.delete()) {
                throw new AssertionError("Could not delete sqlite database from previous  test");
            }
        }
        
        Class.forName("org.sqlite.JDBC");
        localDbConnection = DriverManager.getConnection("jdbc:sqlite:test.db");
        updater = new MockBulkUpdater(localDbConnection);

        Log.setCurrentLogLevel(Log.LOG_LEVEL_DEBUG);
    }

    @Test
    public void run() throws SQLException {
        Synchronizer syncr = new Synchronizer(dispatcher, localDbConnection, updater,
                new Authentication(1,"X", "akbertram@gmail.com"));
        syncr.start();

        assertThat(queryValue("select Name from Indicator where IndicatorId=103"),
                equalTo("Nb. of distributions"));
        assertThat(queryValue("select Name from AdminLevel where AdminLevelId=1"),
                equalTo("Province"));
        assertThat(queryValue("select Name from AdminEntity where AdminEntityId=21"),
                equalTo("Irumu"));
        assertThat(queryValue("select Name from Location where LocationId=7"), equalTo("Shabunda"));
    }

    private String queryValue(String sql) throws SQLException {
        Statement stmt = localDbConnection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(!rs.next()) {
            throw new AssertionError("No rows returned for '" + sql + "'");
        }

        return rs.getString(1);
    }


    private class MockDispatcher implements Dispatcher {
        @Override
        public <T extends CommandResult> void execute(Command<T> command, AsyncMonitor monitor, AsyncCallback<T> callback) {
            List<CommandResult> results = servlet.handleCommands(user, Collections.<Command>singletonList(command));
            CommandResult result = results.get(0);
            
            if(result instanceof SyncRegionUpdate) {
                System.out.println(((SyncRegionUpdate) result).getSql());
            }

            if(result instanceof Exception) {
                throw new Error((Throwable) result);
            } else {
                callback.onSuccess((T) result);
            }
        }
    }
}
