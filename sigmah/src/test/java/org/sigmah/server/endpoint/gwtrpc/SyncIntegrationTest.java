/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.sync.client.BulkUpdaterAsync;
import com.bedatadriven.rebar.sync.mock.MockBulkUpdater;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.offline.sync.Synchronizer;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.shared.domain.User;
import org.sigmah.server.util.BeanMappingModule;
import org.sigmah.server.util.logging.LoggingModule;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.SyncRegionUpdate;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

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
        BeanMappingModule.class,
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
        
        Class.forName("org.sqlite.JDBC");
        localDbConnection = DriverManager.getConnection("jdbc:sqlite::memory:");
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
