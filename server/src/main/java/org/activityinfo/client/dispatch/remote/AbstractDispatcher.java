package org.activityinfo.client.dispatch.remote;

import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.monitor.MonitoringCallback;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractDispatcher implements Dispatcher {

	@Override
	public final <T extends CommandResult> void execute(Command<T> command,
			AsyncMonitor monitor, AsyncCallback<T> callback) {
		
		if(monitor == null) {
			execute(command, callback);
		} else {
			execute(command, new MonitoringCallback(monitor, callback));
		}
	}

}
