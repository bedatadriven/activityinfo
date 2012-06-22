package org.activityinfo.embed.client;

import org.activityinfo.client.dispatch.remote.IncompatibleRemoteHandler;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;

/**
 * Simple handler for {@link IncompatibleRemoteServiceException} that simply refreshes the 
 * page (or iframe) to obtain the new version of the code. Since we're not using appcache and
 * no data is being changed, we don't really need a UI like we do in the full app.
 */
public class SimpleIncompatibleRemoteHandler implements IncompatibleRemoteHandler {

	@Override
	public void handle() {
		Window.Location.reload();
	}

}
