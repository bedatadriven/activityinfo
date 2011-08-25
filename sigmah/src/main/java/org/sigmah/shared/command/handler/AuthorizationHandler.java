package org.sigmah.shared.command.handler;

import org.sigmah.shared.command.Command;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthorizationHandler <C extends Command<?>> {

	void authorize(C command, ExecutionContext context, AsyncCallback<Void> callback);
	
}
