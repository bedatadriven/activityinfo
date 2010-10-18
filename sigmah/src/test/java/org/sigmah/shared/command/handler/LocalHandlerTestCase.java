/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.handler;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.sync.client.BulkUpdaterAsync;
import com.bedatadriven.rebar.sync.mock.MockBulkUpdater;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.junit.Before;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.mock.MockEventBus;
import org.sigmah.client.offline.command.LocalDispatcher;
import org.sigmah.client.offline.sync.Synchronizer;
import org.sigmah.server.endpoint.gwtrpc.CommandServlet;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.SyncRegionUpdate;
import org.sigmah.shared.domain.User;

import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public abstract class LocalHandlerTestCase {
    @Inject
    private CommandServlet servlet;
    @Inject
    protected EntityManagerFactory serverEntityManagerFactory;

    protected User user;

    protected Dispatcher remoteDispatcher;

    protected Authentication localAuth;
    protected LocalDispatcher localDispatcher;
    protected Connection localConnection;
    private BulkUpdaterAsync updater;

    @Before
    public void setUp() throws SQLException, ClassNotFoundException {

        setUser(1); // default is db owner

        remoteDispatcher = new RemoteDispatcherStub();

        Class.forName("org.sqlite.JDBC");
        localConnection = DriverManager.getConnection("jdbc:sqlite::memory:");
        updater = new MockBulkUpdater(localConnection);

        Log.setCurrentLogLevel(Log.LOG_LEVEL_DEBUG);
    }

    protected void setUser(int userId) {
        user = new User();
        user.setId(userId);
        localAuth = new Authentication(user.getId(), "X", user.getEmail());
    }

    protected void synchronize() {
        Synchronizer syncr = new Synchronizer(new MockEventBus(), remoteDispatcher, localConnection, updater,
                localAuth);
        syncr.start();
    }

    private class RemoteDispatcherStub implements Dispatcher {
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
