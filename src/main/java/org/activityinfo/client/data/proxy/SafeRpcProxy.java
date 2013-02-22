package org.activityinfo.client.data.proxy;

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

import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Clone for RPC Proxy that does not swallow exceptions.
 * 
 * @param <D>
 */
public abstract class SafeRpcProxy<D> implements DataProxy<D> {

    @Override
    public void load(final DataReader<D> reader, final Object loadConfig,
        final AsyncCallback<D> callback) {
        load(loadConfig, new AsyncCallback<D>() {

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            @SuppressWarnings("unchecked")
            public void onSuccess(Object result) {
                try {
                    D data = null;
                    if (reader != null) {
                        data = reader.read(loadConfig, result);
                    } else {
                        data = (D) result;
                    }
                    callback.onSuccess(data);
                } catch (Exception e) {
                    Log.error("Rpc load failed: " + e.getMessage(), e);
                    callback.onFailure(e);
                }
            }

        });
    }

    /**
     * Subclasses should make RPC call using the load configuration.
     * 
     * @param callback
     *            the callback to be used when making the rpc call.
     */
    protected abstract void load(Object loadConfig, AsyncCallback<D> callback);
}
