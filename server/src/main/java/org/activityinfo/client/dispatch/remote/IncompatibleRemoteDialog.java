package org.activityinfo.client.dispatch.remote;

import org.activityinfo.client.i18n.I18N;

import com.bedatadriven.rebar.appcache.client.AppCache;
import com.bedatadriven.rebar.appcache.client.AppCache.Status;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Singleton;

@Singleton
public class IncompatibleRemoteDialog extends Dialog implements IncompatibleRemoteHandler {

	private AppCache appCache;
	private boolean needToJettisonCacheBeforeReloading;

	public IncompatibleRemoteDialog() {
		appCache = AppCacheFactory.get();

		setHeading(I18N.CONSTANTS.newVersion());
		setModal(true);
		setBodyStyle("padding: 15px");
		add(new Text(I18N.CONSTANTS.newVersionPrompt()));
		setButtons(OK);
	}

	@Override
	public void handle() {
		switch(appCache.getStatus()) {
		case UNSUPPORTED:
		case OBSOLETE:
		case UNCACHED:
		case UPDATE_READY:
			needToJettisonCacheBeforeReloading = false;
			break;
		default:
			needToJettisonCacheBeforeReloading = true;
			startRemovingCache();
			break;
		}
		show();
	}

	private void startRemovingCache() {
		appCache.removeCache(new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) { }
			
			@Override
			public void onFailure(Throwable caught) { }
		});
	}
	
	@Override
	protected void onButtonPressed(Button button) {
		if(needToJettisonCacheBeforeReloading) {
			waitUntilCacheIsRemovedThenReload();
		} else {
			Window.Location.reload();
		}
	}

	private Button getOkButton() {
		return getButtonById(OK);
	}
	
	private void waitUntilCacheIsRemovedThenReload() {
		getOkButton().disable();
		MessageBox progress = MessageBox.progress("Clearing cache", "Removing old version from cache", "Clearing cache");
		progress.getProgressBar().auto();
		appCache.removeCache(new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				Window.Location.reload();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				if(appCache.getStatus() == Status.UPDATE_READY) {
					Window.Location.reload();
				} else {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						
						@Override
						public void execute() {
							waitUntilCacheIsRemovedThenReload();
						}
					});
				}
			}
		});
	}
}
