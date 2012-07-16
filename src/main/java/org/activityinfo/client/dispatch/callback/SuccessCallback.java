package org.activityinfo.client.dispatch.callback;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class SuccessCallback<T> implements AsyncCallback<T>{

	@Override
	public final void onFailure(Throwable caught) {
		Log.error("Uncaught exception reached SuccessCallback.onFailure(), should have been" +
				" caught be wrappers already.", caught);
	}
	
}
