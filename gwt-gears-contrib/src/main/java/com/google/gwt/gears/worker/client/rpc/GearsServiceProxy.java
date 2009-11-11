/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package com.google.gwt.gears.worker.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.httprequest.HttpRequest;
import com.google.gwt.gears.client.httprequest.RequestCallback;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter;
import com.google.gwt.user.client.rpc.impl.Serializer;

/**
 * Superclass for <code>RemoteServiceProxy</code>s in worker threads that use the Gears
 * <code>HttpRequest</code> object rather than GWT's <code>RequestBuilder</code> API,
 * which is unavailable in the worker thread.
 *
 * @author Alex Bertram
 */
public class GearsServiceProxy extends RemoteServiceProxy {


  public GearsServiceProxy(String moduleBaseURL, String remoteServiceRelativePath, String serializationPolicyName, Serializer serializer) {
    super(moduleBaseURL, remoteServiceRelativePath, serializationPolicyName, serializer);
  }

  /**
   * @return
   */
  protected native Factory getFactory() /*-{
        if($wnd) {
            return $wnd.google && $wnd.google.gears && $wnd.google.gears.factory;
        } else {
            return google.gears.factory;
        }
    }-*/;


  @SuppressWarnings({"ThrowableInstanceNeverThrown"})
  @Override
  protected <T> Request doInvoke(final RequestCallbackAdapter.ResponseReader responseReader, String methodName,
                                 int invocationCount, String requestData, final AsyncCallback<T> callback) {

    try {
      Factory factory = getFactory();
      final HttpRequest httpRequest = factory.createHttpRequest();
      final Request request = new RequestAdapter(httpRequest);

      final RequestCallbackAdapter<T> handler = new RequestCallbackAdapter<T>(
          this, methodName, invocationCount,
          callback, responseReader);

      httpRequest.open("POST", getServiceEntryPoint());
      httpRequest.setRequestHeader("Content-Type", "text/x-gwt-rpc; charset=utf-8");
      httpRequest.setRequestHeader(RpcRequestBuilder.STRONG_NAME_HEADER, GWT.getPermutationStrongName());
      httpRequest.setRequestHeader(RpcRequestBuilder.MODULE_BASE_HEADER, GWT.getModuleBaseURL());

      httpRequest.send(requestData, new RequestCallback() {
        @Override
        public void onResponseReceived(HttpRequest httpRequest) {
          handler.onResponseReceived(request, new ResponseAdapter(httpRequest));
        }
      });
      return request;
    } catch (Throwable t) {
      callback.onFailure(new InvocationException("could not initiate request", t));
    }
    return null;
  }
}
