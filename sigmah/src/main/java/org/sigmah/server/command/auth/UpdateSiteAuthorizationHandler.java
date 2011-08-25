package org.sigmah.server.command.auth;

import org.sigmah.shared.command.UpdateSite;
import org.sigmah.shared.command.handler.AuthorizationHandler;
import org.sigmah.shared.command.handler.ExecutionContext;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class UpdateSiteAuthorizationHandler implements AuthorizationHandler<UpdateSite> {

	@Override
	public void authorize(UpdateSite command, ExecutionContext context,
			AsyncCallback<Void> callback) {
		
		callback.onSuccess(null);
		
	}

}
