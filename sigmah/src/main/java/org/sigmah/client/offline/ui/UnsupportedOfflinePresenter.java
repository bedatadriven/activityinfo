package org.sigmah.client.offline.ui;

import org.sigmah.client.i18n.I18N;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Alternative for OfflinePresenter to be used if 
 * offline mode is unsupported in this browser. 
 * 
 * 
 * @author alexander
 *
 */
@Singleton
public class UnsupportedOfflinePresenter {
	
	private OfflineView view;
		
	@Inject
	public UnsupportedOfflinePresenter(OfflineView view) {
		this.view = view;	
		this.view.setButtonTextToInstall();
		this.view.getButton().addListener(Events.Select, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				MessageBox.confirm(I18N.CONSTANTS.gearsRequired(), I18N.CONSTANTS.gearsInstall(), new Listener<MessageBoxEvent>() {

					@Override
					public void handleEvent(MessageBoxEvent be) {
						if("yes".equals(be.getButtonClicked().getItemId())) {
							Window.open("http://gears.google.com", "_blank", null);
						}
					}
				});				
			}
			
		});
	}
}
