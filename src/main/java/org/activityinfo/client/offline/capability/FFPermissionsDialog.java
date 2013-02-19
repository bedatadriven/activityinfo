package org.activityinfo.client.offline.capability;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.local.capability.PermissionRefusedException;

import com.bedatadriven.rebar.appcache.client.AppCache;
import com.bedatadriven.rebar.appcache.client.AppCache.Status;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FFPermissionsDialog extends Window {

	private AppCache appCache = AppCacheFactory.get();
	private boolean checking = true;
	private AsyncCallback<Void> callback;
	
	public FFPermissionsDialog(final AsyncCallback<Void> callback) {
		
		setWidth(450);
		setHeight(250);
		
		this.callback = callback;
	
		add(new Html("<p>FireFox requires your permission before enabling offline mode:</p>" +
					"<p>Please click the 'Allow' button at the top of this window.</p>" +
					"<p>If you do not see an 'Allow' button, you may need to reload the page" +
					"before continuing.</p>"));
		
		Scheduler.get().scheduleFinally(new RepeatingCommand() {
			
			@Override
			public boolean execute() {
				return checkPermissions();
			}
		});
		

		getButtonBar().add(new Button("Reload page", new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				com.google.gwt.user.client.Window.Location.reload();
			}
		}));
		
		
		getButtonBar().add(new Button(I18N.CONSTANTS.cancel(), new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				hide();
				callback.onFailure(new PermissionRefusedException());
			}
		}));
		
	}

	private boolean checkPermissions() {
		if(isPermissionGranted()) {
			hide();
			callback.onSuccess(null);
		} else {
			appCache.checkForUpdate();
		}
		return checking;
	}

	private boolean isPermissionGranted() {
		return this.appCache.getStatus() != Status.UNCACHED;
	}
	
	@Override
	public void hide() {
		checking = false;
		super.hide();
	}
}
