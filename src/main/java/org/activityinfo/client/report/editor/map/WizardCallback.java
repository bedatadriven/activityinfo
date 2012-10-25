package org.activityinfo.client.report.editor.map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class WizardCallback {

	public void onFinished()  {
		
	}
	
	public void onCanceled() {
		
	}

	public void finish(AsyncCallback<Void> callback) {
		callback.onSuccess(null);
	}
	
}
