package org.sigmah.client.page.app;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AppUpdater {

	void checkForUpdate(AsyncCallback<Boolean> callback);
	
}
