package org.activityinfo.client.dispatch.monitor;

import org.activityinfo.client.dispatch.AsyncMonitor;

import org.activityinfo.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;

public class MonitoringCallback<T> implements AsyncCallback<T> {

	private final AsyncMonitor monitor;
	private final AsyncCallback<T> inner;
	
	public MonitoringCallback(AsyncMonitor monitor, AsyncCallback<T> inner) {
		super();
		this.monitor = monitor;
		this.inner = inner;
		try {
			this.monitor.beforeRequest();
		} catch(Exception e) {
		}
	}

	@Override
	public void onFailure(Throwable caught) {
		if(caught instanceof InvocationException) {
			try {
				monitor.onConnectionProblem();
			} catch(Exception e) {
				Log.error("Exception calling monitor.onConnectionProblem", e);
			}
		} else {
			try {
				monitor.onServerError();
			} catch(Exception e) {
				Log.error("Exception calling monitor.onServerError()", e);
			}
			inner.onFailure(caught);
		}
	}

	@Override
	public void onSuccess(T result) {
		try {
			monitor.onCompleted();
		} catch(Exception e) {
		}
		inner.onSuccess(result);
	}
}
