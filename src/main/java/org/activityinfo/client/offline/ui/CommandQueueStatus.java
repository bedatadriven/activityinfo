package org.activityinfo.client.offline.ui;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.offline.command.CommandQueueEvent;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Status;
import com.google.inject.Inject;

public class CommandQueueStatus extends Status {

	@Inject
	public CommandQueueStatus(EventBus eventBus) {
		eventBus.addListener(CommandQueueEvent.TYPE, new Listener<CommandQueueEvent>() {

			@Override
			public void handleEvent(CommandQueueEvent be) {
				if(be.getEnqueuedItemCount() > 0) {
					setText(be.getEnqueuedItemCount() + " changes pending");
					setBox(true);
				} else {
					setText(null);
					setBox(false);
				}
			}
		});
	}
}
