package org.activityinfo.client;

import org.activityinfo.client.i18n.I18N;

import com.bedatadriven.rebar.appcache.client.AppCache;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.bedatadriven.rebar.appcache.client.events.UpdateReadyEventHandler;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.Window;

public class AppCacheMonitor {

	public static void start() {
		AppCache appCache = AppCacheFactory.get();
		appCache.addUpdateReadyHandler(new UpdateReadyEventHandler() {
			
			@Override
			public void onAppCacheUpdateReady() {
				MessageBox.confirm(I18N.CONSTANTS.newVersion(), I18N.CONSTANTS.newVersionChoice(), new Listener<MessageBoxEvent>() {
					
					@Override
					public void handleEvent(MessageBoxEvent be) {
						if(be.getButtonClicked().getItemId().equals(Dialog.YES)) {
							Window.Location.reload();
						}
					}
				});
			}
		});
	}
	
}
