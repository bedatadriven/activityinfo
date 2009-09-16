package org.activityinfo.client.command.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;

import org.activityinfo.shared.command.result.VoidResult;

public abstract class Deleted implements AsyncCallback<VoidResult> {

	@Override
	public void onFailure(Throwable caught) {
	}


	@Override
	public void onSuccess(VoidResult result) {
		deleted();
	}
	
	public abstract void deleted();

}
