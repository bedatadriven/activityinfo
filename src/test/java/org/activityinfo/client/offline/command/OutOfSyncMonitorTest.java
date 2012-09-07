package org.activityinfo.client.offline.command;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.activityinfo.client.MockEventBus;
import org.activityinfo.client.offline.sync.SyncCompleteEvent;
import org.activityinfo.client.offline.sync.SyncRequestEvent;
import org.activityinfo.client.offline.sync.SyncStatusEvent;
import org.activityinfo.client.offline.sync.Synchronizer;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Provider;

public class OutOfSyncMonitorTest {

	private MockEventBus eventBus;
	private OutOfSyncMonitor.View notifier;

	@Before
	public void setUp() {
		eventBus = new MockEventBus();	
		notifier = createNiceMock(OutOfSyncMonitor.View.class);
		replay(notifier);
	}
	
	@Test
	public void simple() {
		
		OutOfSyncMonitor monitor = new OutOfSyncMonitor(eventBus, notifier);
		monitor.onServerStateMutated();
		
		// The notifier should mark its state as dirty
		assertThat("outOfSync", monitor.isOutOfSync(), equalTo(true));
		
		
		// Synchronization should be triggered
		eventBus.assertEventFired(SyncRequestEvent.INSTANCE);
		
		syncStatusEventFires(0);
		syncStatusEventFires(50);
		syncStatusEventFires(95);
		
		// When synchronization completes, the dirty flag should be changed
		syncCompleteEventFires();
		
		assertThat("outOfSync", monitor.isOutOfSync(), equalTo(false));
	}

	private boolean syncCompleteEventFires() {
		return eventBus.fireEvent(new SyncCompleteEvent(new Date()));
	}
	
	@Test
	public void notFireOnStartup() {
		
		OutOfSyncMonitor monitor = new OutOfSyncMonitor(eventBus, notifier);

		syncCompleteEventFires();
		
		eventBus.assertNotFired(SyncRequestEvent.TYPE);
		
		
	}
	
	@Test
	public void syncAlreadyInProgress() {
				

		OutOfSyncMonitor monitor = new OutOfSyncMonitor(eventBus, notifier);
		

		// sync is already started...
		syncStatusEventFires(50);
		
		// ...when a new mutation is sent
		monitor.onServerStateMutated();
		
		// The notifier should mark its state as dirty
		assertThat("outOfSync", monitor.isOutOfSync(), equalTo(true));
		
		
		syncCompleteEventFires();
		
		assertThat("outOfSync", monitor.isOutOfSync(), equalTo(true));
		
		// Synchronization should be triggered
		// ...but should not trigger a new round of synchronization until 
		// the previous synchronization is complete

		eventBus.assertEventFired(SyncRequestEvent.INSTANCE);
		
		syncStatusEventFires(0);
		syncStatusEventFires(50);
		syncStatusEventFires(95);
		syncCompleteEventFires();
		
		assertThat("outOfSync", monitor.isOutOfSync(), equalTo(false));
	}

	private void syncStatusEventFires(int percentComplete) {
		eventBus.fireEvent(new SyncStatusEvent("", percentComplete));
	}
	
}
