package org.activityinfo.server.command.authorization;

import org.activityinfo.shared.command.UpdateSite;
import org.activityinfo.shared.command.handler.AuthorizationHandler;
import org.activityinfo.shared.command.handler.ExecutionContext;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class UpdateSiteAuthorizationHandler implements AuthorizationHandler<UpdateSite> {

	@Override
	public void authorize(UpdateSite command, ExecutionContext context,
			AsyncCallback<Void> callback) {
		
		callback.onSuccess(null);
		
	}

}
