package org.activityinfo.client.offline.sync;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;

import org.activityinfo.client.MockEventBus;
import org.activityinfo.client.dispatch.remote.RemoteDispatcher;
import org.activityinfo.client.offline.command.CommandQueue;
import org.activityinfo.client.offline.command.HandlerRegistry;
import org.activityinfo.client.offline.command.LocalDispatcher;
import org.activityinfo.client.offline.sync.AppCacheSynchronizer;
import org.activityinfo.client.offline.sync.DownSynchronizer;
import org.activityinfo.client.offline.sync.SchemaMigration;
import org.activityinfo.client.offline.sync.SynchronizerDispatcher;
import org.activityinfo.client.offline.sync.SynchronizerImpl;
import org.activityinfo.client.offline.sync.UpdateSynchronizer;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.junit.Before;
import org.junit.Test;
import org.activityinfo.client.i18n.UIConstants;

import com.bedatadriven.rebar.sql.client.SqlDatabase;

public class SynchronizerImplTest {

	
	private HandlerRegistry registry;
	private AuthenticatedUser auth;
	private MockEventBus eventBus;

	@Before
	public void setUp() {
		registry = new HandlerRegistry();
		auth = new AuthenticatedUser("XYZ123", 1, "akbertram@gmail.com");
		eventBus = new MockEventBus();
	}

	@Test
	public void databaseExceptionsAreCaught() {
		
		
		SqlDatabase database = createMock(SqlDatabase.class);
		replay(database);
		
		LocalDispatcher localDispatcher = new LocalDispatcher(auth, database, registry,
				new RemoteDispatcher(null, null));
		
		SynchronizerDispatcher remoteDispatcher = createMock(SynchronizerDispatcher.class);
		replay(remoteDispatcher);
		
		RemoteDispatcher directDispatcher = createMock(RemoteDispatcher.class);
		replay(directDispatcher);
		
		AppCacheSynchronizer appCache = new AppCacheSynchronizer(eventBus);
		
		UIConstants constants = createMock(UIConstants.class);
		replay(constants);
		
		CommandQueue commandQueue = new CommandQueue(database);
		
		DownSynchronizer down = new DownSynchronizer(eventBus, remoteDispatcher, database, constants);
		UpdateSynchronizer up = new UpdateSynchronizer(commandQueue, remoteDispatcher);
		
//		
//
//		SynchronizerImpl sync = new SynchronizerImpl(localDispatcher, directDispatcher, appCache, down, up, auth, 
//				new SchemaMigration(database));
	}

		
}
