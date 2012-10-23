package org.activityinfo.client.map;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

public final class AdminGeometryProvider {

	public static AdminGeometryProvider INSTANCE = new AdminGeometryProvider();
	
	public void get(int levelId, final AsyncCallback<AdminGeometry> callback) {
		RequestBuilder request = new RequestBuilder(RequestBuilder.GET, "/1383.json");
		request.setCallback(new RequestCallback() {
			
			@Override
			public void onResponseReceived(Request request, Response response) {
				try {
					callback.onSuccess(AdminGeometry.fromJson(response.getText()));
				} catch(Exception e) {
					callback.onFailure(e);
				}
			}
			
			@Override
			public void onError(Request request, Throwable exception) {
				callback.onFailure(exception);
			}
		});
		try {
			request.send();
		} catch (RequestException e) {
			callback.onFailure(e);
		}
	}
}
