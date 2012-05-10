package org.activityinfo.client.offline.sync;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.i18n.I18N;

import com.bedatadriven.rebar.appcache.client.AppCache;
import com.bedatadriven.rebar.appcache.client.AppCache.Status;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.bedatadriven.rebar.appcache.client.events.ProgressEventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class AppCacheSynchronizer implements ProgressEventHandler {

	private final EventBus eventBus;
	private final AppCache appCache = AppCacheFactory.get();
	
	@Inject
	public AppCacheSynchronizer(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	public void ensureUpToDate(final AsyncCallback<Void> callback) {
		
		final HandlerRegistration progressRegistration = appCache.addProgressHandler(this);
		appCache.ensureUpToDate(new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				if(appCache.getStatus() == Status.UPDATE_READY) {
					callback.onFailure(new AppOutOfDateException());
				} else {
					progressRegistration.removeHandler();
					callback.onSuccess(result);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				progressRegistration.removeHandler();
				callback.onFailure(new SynchronizerConnectionException());
			}
		});
	}

	@Override
	public void onProgress(int filesComplete, int filesTotal) {
		eventBus.fireEvent(new SyncStatusEvent(I18N.CONSTANTS.appCacheProgress(),
				(double)filesComplete / (double)filesTotal * 100d));
	}
	
}
