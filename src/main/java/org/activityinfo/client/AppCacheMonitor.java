package org.activityinfo.client;

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
