package org.sigmah.server.command.authorization;

import org.sigmah.shared.command.CreateLocation;
import org.sigmah.shared.command.handler.AuthorizationHandler;
import org.sigmah.shared.command.handler.ExecutionContext;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class CreateLocationAuthorizationHandler implements AuthorizationHandler<CreateLocation> {

	@Override
	public void authorize(CreateLocation command, ExecutionContext context,
			AsyncCallback<Void> callback) {
		
		callback.onSuccess(null);
		
	}

}
