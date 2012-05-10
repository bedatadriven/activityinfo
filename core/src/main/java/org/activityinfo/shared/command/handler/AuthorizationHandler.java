package org.activityinfo.shared.command.handler;

import org.activityinfo.shared.command.Command;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthorizationHandler <C extends Command<?>> {

	void authorize(C command, ExecutionContext context, AsyncCallback<Void> callback);
	
}
