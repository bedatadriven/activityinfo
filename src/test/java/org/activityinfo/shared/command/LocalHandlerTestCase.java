

package org.activityinfo.shared.command;

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


import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.replay;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.remote.AbstractDispatcher;
import org.activityinfo.client.i18n.UIConstants;
import org.activityinfo.client.i18n.UIMessages;
import org.activityinfo.client.local.LocalModuleStub;
import org.activityinfo.client.local.command.CommandQueue;
import org.activityinfo.client.local.command.LocalDispatcher;
import org.activityinfo.client.local.sync.SyncHistoryTable;
import org.activityinfo.client.local.sync.pipeline.InstallPipeline;
import org.activityinfo.client.local.sync.pipeline.SyncPipeline;
import org.activityinfo.server.authentication.AuthenticationModuleStub;
import org.activityinfo.server.endpoint.gwtrpc.CommandServlet;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.SyncRegionUpdate;
import org.activityinfo.shared.util.Collector;
import org.activityinfo.test.Modules;
import org.junit.After;
import org.junit.Before;

import com.bedatadriven.rebar.async.AsyncPipeline;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcDatabase;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcScheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
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

	protected AsyncPipeline installer;
	protected AsyncPipeline synchronizer;
	protected SyncHistoryTable syncHistoryTable;
	
    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        
    	System.err.println("Sqlite database = " + databaseName);
    	
    	localDatabase = new JdbcDatabase(databaseName);
        
        setUser(1); // default is db owner
                        
        uiConstants = createNiceMock(UIConstants.class);
        uiMessages = createNiceMock(UIMessages.class);
        replay(uiConstants, uiMessages);

    }

    protected void setUser(int userId) {
    	AuthenticationModuleStub.setUserId(userId);
        remoteDispatcher = new RemoteDispatcherStub();

        Injector clientSideInjector = Guice.createInjector(
        		new LocalModuleStub(AuthenticationModuleStub.getCurrentUser(), 
        				localDatabase,
        				remoteDispatcher));
        localDispatcher = clientSideInjector.getInstance(LocalDispatcher.class);
        synchronizer = clientSideInjector.getInstance(SyncPipeline.class);
        installer = clientSideInjector.getInstance(InstallPipeline.class);
        syncHistoryTable = clientSideInjector.getInstance(SyncHistoryTable.class);
    }

    
    protected void synchronizeFirstTime() {
    	newRequest();   
    	installer.start(this.<Void>throwOnFailure());
    	localDatabase.processEventQueue();
    }
    
    protected void synchronize() {
    	newRequest();    	
    	synchronizer.start();
    	localDatabase.processEventQueue();
        
    }
    
    protected <C extends Command<R>, R extends CommandResult> R executeLocally(C command) {
    	Collector<R> collector = Collector.newCollector();
    	localDispatcher.execute(command, collector);
    	return collector.getResult();
    }
    
    protected <C extends Command<R>, R extends CommandResult> R executeRemotely(C command) {
    	Collector<R> collector = Collector.newCollector();
    	remoteDispatcher.execute(command, collector);
    	return collector.getResult();
    }
    
        
    
    @After
    public void tearDown() {
    	JdbcScheduler.get().forceCleanup();
    }

    protected void newRequest() {
    	serverEm.clear();
    }

    private class RemoteDispatcherStub extends AbstractDispatcher {

    	@Override
    	public <T extends CommandResult> void execute(final Command<T> command,
    			final AsyncCallback<T> callback) {
    		
    		JdbcScheduler.get().scheduleDeferred(new ScheduledCommand() {
				
				@Override
				public void execute() {

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
			});
    		JdbcScheduler.get().process();
    		
    		
    	}
    }
    
    private <T> AsyncCallback<T> throwOnFailure() {
    	return new AsyncCallback<T>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}

			@Override
			public void onSuccess(T result) {				
			}
		};
    }
}
