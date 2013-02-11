package org.activityinfo.client.local.command;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.local.ui.SyncStatusResources;

import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.tips.ToolTip;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class OutOfSyncStatus extends Status implements OutOfSyncMonitor.View {

	private ToolTip tip = null;

	@Inject
	public OutOfSyncStatus(EventBus eventBus) {
		new OutOfSyncMonitor(eventBus, this);
		createToolTip();
	}


	private void createToolTip() {
		if(this.tip == null) {
			ToolTipConfig config = new ToolTipConfig();
			config.setTitle("Out of Sync");
			config.setText("You have recently sent an update directly to the server.<br>" +
					"Your change will not be visible locally until you synchronize.");
			config.setShowDelay(1);
		
			this.tip = new ToolTip(this, config);
		}
	}
	

	public void show() {
		setIconStyle(SyncStatusResources.INSTANCE.style().warningIcon());
		setText("Your local copy is out sync with the sever");
		setBox(true);
		createTip();
	}
	
	public void hide() {
		setBox(false);
		setIconStyle(null);
		setText(null);
	}

	private ToolTip createTip() {
		ToolTipConfig config = new ToolTipConfig();
		config.setAnchor("top");
		config.setCloseable(false);
		config.setText("Your local database is out of sync with the server. Please wait while synchronization completes");
		
		ToolTip tip = new ToolTip();
		tip.update(config);
		
		return tip;
	}
	
}
