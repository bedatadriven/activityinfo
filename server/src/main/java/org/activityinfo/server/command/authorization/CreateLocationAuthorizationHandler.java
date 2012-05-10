package org.activityinfo.server.command.authorization;

import org.activityinfo.shared.command.CreateLocation;
import org.activityinfo.shared.command.handler.AuthorizationHandler;
import org.activityinfo.shared.command.handler.ExecutionContext;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class CreateLocationAuthorizationHandler implements AuthorizationHandler<CreateLocation> {

	@Override
	public void authorize(CreateLocation command, ExecutionContext context,
			AsyncCallback<Void> callback) {
		
		callback.onSuccess(null);
		
	}

}
