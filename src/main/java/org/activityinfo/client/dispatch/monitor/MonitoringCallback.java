package org.activityinfo.client.dispatch.monitor;

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

import org.activityinfo.client.Log;
import org.activityinfo.client.dispatch.AsyncMonitor;

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
        } catch (Exception e) {
        }
    }

    @Override
    public void onFailure(Throwable caught) {
        if (caught instanceof InvocationException) {
            try {
                monitor.onConnectionProblem();
            } catch (Exception e) {
                Log.error("Exception calling monitor.onConnectionProblem", e);
            }
        } else {
            try {
                monitor.onServerError();
            } catch (Exception e) {
                Log.error("Exception calling monitor.onServerError()", e);
            }
            inner.onFailure(caught);
        }
    }

    @Override
    public void onSuccess(T result) {
        try {
            monitor.onCompleted();
        } catch (Exception e) {
        }
        inner.onSuccess(result);
    }
}
