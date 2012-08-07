package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.VoidResult;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;

public class PostClientMetrics implements Command<VoidResult> {

	public PostClientMetrics() {
		
	}

	private int BATCH_INTERVAL_MS() {
		return 5000;
	}
	
	
}
