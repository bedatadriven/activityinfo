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

package org.activityinfo.client.report;

import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.Serializer;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.http.client.Request;
import com.google.gwt.core.client.GWT;

/**
 * @author Alex Bertram
 */
public class ExportServiceProxy extends RemoteServiceProxy {

    public ExportServiceProxy(String moduleBaseURL, String remoteServiceRelativePath, String serializationPolicyName, Serializer serializer) {
        super(moduleBaseURL, remoteServiceRelativePath, serializationPolicyName, serializer);
    }

    @Override
    protected <T> Request doInvoke(RequestCallbackAdapter.ResponseReader responseReader, String methodName, int invocationCount, String requestData, AsyncCallback<T> callback) {

        StringBuilder url = new StringBuilder(requestData.length()+25);
        url.append(GWT.getModuleBaseURL()).append("cmd/download?").append(requestData);

        Window.open(url.toString(), "_downloadFrame", null);

        return null;
    }
}
