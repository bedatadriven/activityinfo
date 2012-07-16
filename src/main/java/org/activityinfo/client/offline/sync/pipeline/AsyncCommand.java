package org.activityinfo.client.offline.sync.pipeline;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AsyncCommand {

	void execute(AsyncCallback<Void> callback);
	
}
