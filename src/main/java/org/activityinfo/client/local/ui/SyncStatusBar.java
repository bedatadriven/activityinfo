package org.activityinfo.client.local.ui;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.local.command.OutOfSyncStatus;

import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.inject.Inject;

public class SyncStatusBar extends ToolBar {
	
	public static final int HEIGHT = 30;
	
	@Inject
	public SyncStatusBar(EventBus eventBus, OutOfSyncStatus outOfSyncStatus,
			CommandQueueStatus commandQueueStatus) {		
		setHeight(HEIGHT);
		add(new WorkStatus(eventBus));
		add(new LastSyncStatus(eventBus));
		add(outOfSyncStatus);
		add(commandQueueStatus);
		
		SyncStatusResources.INSTANCE.style().ensureInjected();
	}
}
