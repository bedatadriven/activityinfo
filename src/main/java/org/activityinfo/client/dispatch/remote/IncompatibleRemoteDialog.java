package org.activityinfo.client.dispatch.remote;

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
