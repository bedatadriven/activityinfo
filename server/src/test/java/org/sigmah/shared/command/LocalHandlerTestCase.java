/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.replay;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.activityinfo.client.MockEventBus;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.UIMessages;
import org.activityinfo.client.offline.OfflineModuleStub;
import org.activityinfo.client.offline.command.CommandQueue;
import org.activityinfo.client.offline.command.LocalDispatcher;
import org.activityinfo.client.offline.sync.DownSynchronizer;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.SyncRegionUpdate;
import org.activityinfo.shared.util.Collector;
import org.junit.After;
import org.junit.Before;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.server.authentication.AuthenticationModuleStub;
import org.sigmah.server.endpoint.gwtrpc.CommandServlet;
import org.sigmah.test.Modules;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcDatabase;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcScheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

@Modules({AuthenticationModuleStub.class})
public abstract class LocalHandlerTestCase {
    @Inject
    private CommandServlet servlet;
    @Inject
    protected EntityManagerFactory serverEntityManagerFactory;

    /**
     * this is scoped to Tests as the analog of being
     * scoped to a request.
     */
    @Inject
    protected EntityManager serverEm;

    protected Dispatcher remoteDispatcher;

    protected LocalDispatcher localDispatcher;
    protected JdbcDatabase localDatabase;
    
    protected CommandQueue commandQueue;
        
    private UIConstants uiConstants;
    private UIMessages uiMessages;
	protected Connection localConnection;
		
	
	private String databaseName = "target/localdbtest" + new java.util.Date().getTime();
	protected DownSynchronizer synchronizer;

    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        
    	localDatabase = new JdbcDatabase(databaseName);
        
        setUser(1); // default is db owner

        remoteDispatcher = new RemoteDispatcherStub();

                        
        uiConstants = createNiceMock(UIConstants.class);
        uiMessages = createNiceMock(UIMessages.class);
        replay(uiConstants, uiMessages);

        Log.setCurrentLogLevel(Log.LOG_LEVEL_DEBUG);
        
        	
    }

    protected void setUser(int userId) {
    	AuthenticationModuleStub.setUserId(userId);
    	
        Injector clientSideInjector = Guice.createInjector(new OfflineModuleStub(AuthenticationModuleStub.getCurrentUser(), localDatabase));
        localDispatcher = clientSideInjector.getInstance(LocalDispatcher.class);
        
    }

    
    protected void synchronizeFirstTime() {
    	newRequest();    	
    	synchronizer = new DownSynchronizer(new MockEventBus(), remoteDispatcher, localDatabase, 
                uiConstants);
        synchronizer.startFresh(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new AssertionError(caught);
			}

			@Override
			public void onSuccess(Void result) {
				
			}
		});
        localDatabase.processEventQueue();
        
    }
    
    protected void synchronize() {
    	newRequest();    	
    	synchronizer = new DownSynchronizer(new MockEventBus(), remoteDispatcher, localDatabase, 
                uiConstants);
        synchronizer.start(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new AssertionError(caught);
			}

			@Override
			public void onSuccess(Void result) {
				
			}
		});
        localDatabase.processEventQueue();
        
    }
    
    protected <C extends Command<R>, R extends CommandResult> R executeLocally(C command) {
    	Collector<R> collector = Collector.newCollector();
    	localDispatcher.execute(command, null, collector);
    	return collector.getResult();
    }
    
    protected <C extends Command<R>, R extends CommandResult> R executeRemotely(C command) {
    	Collector<R> collector = Collector.newCollector();
    	remoteDispatcher.execute(command, null, collector);
    	return collector.getResult();
    }
    
        
    
    @After
    public void tearDown() {
    	JdbcScheduler.get().forceCleanup();
    }

    protected void newRequest() {
    	serverEm.clear();
    }

    private class RemoteDispatcherStub implements Dispatcher {
        @Override
        public <T extends CommandResult> void execute(Command<T> command, AsyncMonitor monitor, AsyncCallback<T> callback) {
            List<CommandResult> results = servlet.handleCommands(Collections.<Command>singletonList(command));
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
