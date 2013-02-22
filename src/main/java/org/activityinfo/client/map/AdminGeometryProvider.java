package org.activityinfo.client.map;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
		RequestBuilder request = new RequestBuilder(RequestBuilder.GET, "//geometry.activityinfo.org/" + levelId + ".json");
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
