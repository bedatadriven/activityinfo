package org.sigmah.client.offline.sync;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;

import org.junit.Before;
import org.junit.Test;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.dispatch.remote.DirectDispatcher;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.mock.MockEventBus;
import org.sigmah.client.offline.command.CommandQueue;
import org.sigmah.client.offline.command.HandlerRegistry;
import org.sigmah.client.offline.command.LocalDispatcher;
import org.sigmah.database.ClientDatabaseStubs;

import com.bedatadriven.rebar.sql.client.SqlDatabase;

public class SynchronizerImplTest {

	
	private HandlerRegistry registry;
	private Authentication auth;
	private MockEventBus eventBus;

	@Before
	public void setUp() {
		registry = new HandlerRegistry();
		auth = new Authentication(1, "XYZ123", "akbertram@gmail.com");
		eventBus = new MockEventBus();
	}

	@Test
	public void databaseExceptionsAreCaught() {
		
		
		SqlDatabase database = createMock(SqlDatabase.class);
		replay(database);
		
		LocalDispatcher localDispatcher = new LocalDispatcher(auth, database, registry);
		
		SynchronizerDispatcher remoteDispatcher = createMock(SynchronizerDispatcher.class);
		replay(remoteDispatcher);
		
		DirectDispatcher directDispatcher = createMock(DirectDispatcher.class);
		replay(directDispatcher);
		
		AppCacheSynchronizer appCache = new AppCacheSynchronizer(eventBus);
		
		UIConstants constants = createMock(UIConstants.class);
		replay(constants);
		
		CommandQueue commandQueue = new CommandQueue(database);
		
		DownSynchronizer down = new DownSynchronizer(eventBus, remoteDispatcher, database, constants);
		UpdateSynchronizer up = new UpdateSynchronizer(commandQueue, remoteDispatcher);

		SynchronizerImpl sync = new SynchronizerImpl(localDispatcher, directDispatcher, appCache, down, up, auth);
	}

		
}
