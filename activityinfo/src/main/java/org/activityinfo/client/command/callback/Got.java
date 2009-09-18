package org.activityinfo.client.command.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class Got<T> implements AsyncCallback<T> {

	@Override
	public void onFailure(Throwable arg0) {
		
	}

	@Override
	public void onSuccess(T result) {
		got(result);
	}
	
	public abstract void got(T result);

}
