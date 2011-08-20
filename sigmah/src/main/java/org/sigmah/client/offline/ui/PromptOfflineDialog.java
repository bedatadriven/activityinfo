package org.sigmah.client.offline.ui;

import java.util.Date;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.offline.capability.OfflineCapabilityProfile;
import org.sigmah.client.offline.capability.ProfileResources;
import org.sigmah.client.offline.ui.OfflinePresenter.EnableCallback;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Inject;


/**
 * Dialog presented to the user on startup to prompt 
 * activation of offline mode
 * 
 */
public class PromptOfflineDialog extends Dialog {

	static {
		ProfileResources.INSTANCE.style().ensureInjected();
	}
	
	private final OfflineCapabilityProfile capabilityProfile = GWT.create(OfflineCapabilityProfile.class);
	
	private static final String COOKIE_NAME = "offlineSilent";
	private static final String DONT_ASK_COOKIE_VALUE = "true";
	private static final long THREE_MONTHS = 1000 * 60 * 60 * 24 * 90;
	
	private final EnableCallback callback;
	
	
	@Inject
	public PromptOfflineDialog(EnableCallback callback) {
		super();
		
		this.callback = callback;
		
		setWidth(500);
		setHeight(350);
		setHeading(I18N.CONSTANTS.installOffline());
		setModal(true);
		setLayout(new FitLayout());
		
		Html bodyHtml = new Html(capabilityProfile.getStartupMessageHtml());
		bodyHtml.addStyleName(ProfileResources.INSTANCE.style().startupDialogBody());
        
		add(bodyHtml);

		getButtonBar().removeAll();
		
		if(capabilityProfile.isOfflineModeSupported()) {
			addButton(new Button(I18N.CONSTANTS.installOffline(), new SelectionListener<ButtonEvent>() {
				
				@Override
				public void componentSelected(ButtonEvent ce) {
					enableOffline();
				}
			}));
		}
		addButton(new Button(I18N.CONSTANTS.notNow(), new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				askMeLater();
			}
			
		}));
		addButton(new Button(I18N.CONSTANTS.dontAskAgain(), new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				dontAskAgain();
			}
		}));		
	}
	
	private void askMeLater() {
		hide();
	}
	
	private void dontAskAgain() {
		Cookies.setCookie(COOKIE_NAME, DONT_ASK_COOKIE_VALUE, new Date(new Date().getTime() + THREE_MONTHS));
		hide();
	}

	private void enableOffline() {
		hide();
		callback.enableOffline();
	}
	
	public static boolean shouldAskAgain() {
		return !DONT_ASK_COOKIE_VALUE.equals(Cookies.getCookie(COOKIE_NAME));
	}
}
