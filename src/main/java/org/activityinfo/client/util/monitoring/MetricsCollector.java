package org.activityinfo.client.util.monitoring;

import java.util.logging.Logger;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

/**
 * 
 * Periodically sends batches of metrics to the server
 */
public class MetricsCollector {

	private static final int BATCH_INTERVAL_MS = 60000;
	
	private StringBuilder toSend = new StringBuilder();

	public MetricsCollector() {
		Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
			
			@Override
			public boolean execute() {
				doNextBatch();
				return true;
			}
		}, BATCH_INTERVAL_MS);
	}
	
	private void doNextBatch() {
		final String batch = nextBatch() + toSend.toString();
		toSend.setLength(0);
		
		if(!Strings.isNullOrEmpty(batch)) {
			RequestBuilder request = new RequestBuilder(RequestBuilder.POST, 
					GWT.getModuleBaseURL() + "clientMetrics");
			request.setRequestData(batch);
			request.setCallback(new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					// done!
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					// add back on to the list to send
					toSend.append(batch);
				}
			});		
			try {
				request.send();
			} catch (RequestException e) {
				Log.error("Exception while trying to send client metrics", e);
			}
		}
	}
	
	public static native String nextBatch() /*-{
		var m = $wnd["__metrics"];
		$wnd["__metrics"] = "";
		return m;
	}-*/;
}
