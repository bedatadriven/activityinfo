package org.sigmah.client.offline.sync;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;

import org.junit.Test;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.dispatch.remote.DirectDispatcher;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.mock.MockEventBus;
import org.sigmah.client.offline.command.CommandQueue;
import org.sigmah.client.offline.command.HandlerRegistry;
import org.sigmah.client.offline.command.LocalDispatcher;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SynchronizerImplTest {

	
	@Test
	public void databaseExceptionsAreCaught() {
		
		
		SqlDatabase database = createMock(SqlDatabase.class);
		replay(database);
		
		Authentication auth = new Authentication(1, "XYZ123", "akbertram@gmail.com");
		
		HandlerRegistry registry = new HandlerRegistry();
		
		LocalDispatcher localDispatcher = new LocalDispatcher(auth, database, registry);
		
		SynchronizerDispatcher remoteDispatcher = createMock(SynchronizerDispatcher.class);
		replay(remoteDispatcher);
		
		DirectDispatcher directDispatcher = createMock(DirectDispatcher.class);
		replay(directDispatcher);
		
		MockEventBus eventBus = new MockEventBus();
		
		AppCacheSynchronizer appCache = new AppCacheSynchronizer(eventBus);
		
		UIConstants constants = createMock(UIConstants.class);
		replay(constants);
		
		CommandQueue commandQueue = new CommandQueue(database);
		
		DownSynchronizer down = new DownSynchronizer(eventBus, remoteDispatcher, database, constants);
		UpdateSynchronizer up = new UpdateSynchronizer(commandQueue, remoteDispatcher);

		SynchronizerImpl sync = new SynchronizerImpl(localDispatcher, directDispatcher, appCache, down, up, auth);
		
		
	}
	
	
}
