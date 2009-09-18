package org.activityinfo.client.command.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;

import org.activityinfo.shared.command.result.VoidResult;

public abstract class Updated implements AsyncCallback<VoidResult> {

	@Override
	public void onFailure(Throwable caught) {

	}

	@Override
	public void onSuccess(VoidResult result) {
		updated();
	}

	public abstract void updated();
}
