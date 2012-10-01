package org.activityinfo.client.data.proxy;

import org.activityinfo.client.Log;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Clone for RPC Proxy that does not swallow exceptions.
 * @param <D>
 */
public abstract class SafeRpcProxy<D> implements DataProxy<D>  {

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
	 * @param callback the callback to be used when making the rpc call.
	 */
	protected abstract void load(Object loadConfig, AsyncCallback<D> callback);
}
