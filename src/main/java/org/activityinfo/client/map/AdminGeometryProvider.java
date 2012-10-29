package org.activityinfo.client.map;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

public final class AdminGeometryProvider {

	public static AdminGeometryProvider INSTANCE = new AdminGeometryProvider();
	
	private Map<Integer, AdminGeometry> cache = Maps.newHashMap();
	
	public void get(final int levelId, final AsyncCallback<AdminGeometry> callback) {
		if(cache.containsKey(levelId)) {
			callback.onSuccess(cache.get(levelId));
		} else {
			fetchGeometry(levelId, callback);
		}
	}

	private void fetchGeometry(final int levelId,
			final AsyncCallback<AdminGeometry> callback) {
		RequestBuilder request = new RequestBuilder(RequestBuilder.GET, "/geometry/" + levelId);
		request.setCallback(new RequestCallback() {
			
			@Override
			public void onResponseReceived(Request request, Response response) {
				try {
					AdminGeometry geometry = AdminGeometry.fromJson(response.getText());
					cache.put(levelId, geometry);
					callback.onSuccess(geometry);
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
