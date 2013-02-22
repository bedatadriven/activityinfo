package org.activityinfo.client.dispatch;

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

import org.activityinfo.shared.exception.InvalidAuthTokenException;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.StatusCodeException;

public abstract class AsyncFailureCallback<T> implements AsyncCallback<T> {

    @Override
    public final void onFailure(Throwable caught) {
        if (caught instanceof InvalidAuthTokenException) {
            onAuthenticationExpired(caught);

        } else if (caught instanceof IncompatibleRemoteServiceException) {
            onRemoteServiceIncompatible(caught);

        } else if (caught instanceof StatusCodeException) {
            onHttpError((StatusCodeException) caught);

        } else if (caught instanceof InvocationException) {
            onConnectionProblem(caught);

        } else {
            onServerError(caught);
        }
    }

    protected void onHttpError(StatusCodeException caught) {
        switch (caught.getStatusCode()) {
        case 404:
        case 502:
        case 503:
            onServerUnavailable(caught);
            break;
        default:
            onServerError(caught);
        }
    }

    protected void onServerUnavailable(StatusCodeException caught) {
        onTransientException(caught);
    }

    protected void onConnectionProblem(Throwable caught) {
        onTransientException(caught);
    }

    protected void onServerError(Throwable caught) {
        onFatalException(caught);
    }

    protected void onRemoteServiceIncompatible(Throwable caught) {
        onFatalException(caught);
    }

    protected void onAuthenticationExpired(Throwable caught) {
        onFatalException(caught);
    }

    protected void onTransientException(Throwable caught) {

    }

    protected void onFatalException(Throwable caught) {

    }
}
