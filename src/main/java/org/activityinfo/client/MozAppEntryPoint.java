package org.activityinfo.client;

import org.activityinfo.client.util.mozApp.MozApp;
import org.activityinfo.client.util.mozApp.MozAppsApi;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MozAppEntryPoint implements EntryPoint {

	@Override
	public void onModuleLoad() {
		MozAppsApi.getSelf(new AsyncCallback<MozApp>() {
			
			@Override
			public void onSuccess(MozApp result) {
				startApp(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Could not get receipts");
			}
		});
	}

	private void startApp(MozApp app) {
		if(app.getReceipts().length() == 1) {
			Window.alert("Receipt = " + app.getReceipts().get(0));
		} else {
			Window.alert("Could not get data from receipts");
		}
		
	}

}
