package org.activityinfo.client.offline.ui;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.offline.OfflineController;
import org.activityinfo.client.offline.OfflineStateChangeEvent;
import org.activityinfo.client.offline.OfflineStateChangeEvent.State;
import org.activityinfo.client.offline.capability.ProfileResources;
import org.activityinfo.client.util.state.CrossSessionStateProvider;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.inject.Inject;
import com.google.inject.Singleton;


/**
 * Dialog presented to the user on startup to prompt 
 * activation of offline mode
 * 
 */
@Singleton
public class PromptOfflineDialog extends BasePromptDialog {

	private static final String DONT_ASK_STATE_KEY = "offlineSilent2";

	private final CrossSessionStateProvider stateProvider;
	private final OfflineController offlineController;
	
	private boolean promptedThisSession = false;
	
	@Inject
	public PromptOfflineDialog(EventBus eventBus, CrossSessionStateProvider stateProvider,
			OfflineController controller) {
		super(composeHtml());
		
		this.stateProvider = stateProvider;
		this.offlineController = controller;
		
		setWidth(500);
		setHeight(350);
		setHeading(I18N.CONSTANTS.installOffline());
		setModal(true);

		getButtonBar().removeAll();
		
		eventBus.addListener(OfflineStateChangeEvent.TYPE, new Listener<OfflineStateChangeEvent>() {
			@Override
			public void handleEvent(OfflineStateChangeEvent be) {
				onOfflineStatusChange(be.getState());
			}
		});
		
		if(capabilityProfile.isOfflineModeSupported()) {
			addButton(new Button(I18N.CONSTANTS.installOffline(), new SelectionListener<ButtonEvent>() {
				
				@Override
				public void componentSelected(ButtonEvent ce) { 
					offlineController.install();
					hide();
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

	private static String composeHtml() {
		StringBuilder html = new StringBuilder();
		html.append("<p>").append(I18N.CONSTANTS.offlineIntro1()).append("</p>");
		if(capabilityProfile.isOfflineModeSupported()) {
			html.append("<p>").append(I18N.CONSTANTS.offlineIntro2()).append("</p>");
			html.append("<p>").append(I18N.CONSTANTS.offlineIntro3()).append("</p>");
			html.append("<p>").append(I18N.CONSTANTS.offlineIntro4()).append("</p>");
		} else {
			html.append(capabilityProfile.isOfflineModeSupported());
		}
		return html.toString();
	}

	private void onOfflineStatusChange(State state) {
		if(state == State.UNINSTALLED && !promptedThisSession && shouldAskAgain()) {
			promptedThisSession = true;
			show();
		}
	}
	
	private void askMeLater() {
		hide();
	}
	
	private void dontAskAgain() {
		stateProvider.set(DONT_ASK_STATE_KEY, Boolean.TRUE.toString());
		hide();
	}
	
	public boolean shouldAskAgain() {
		return !Boolean.TRUE.toString().equals(stateProvider.getString(DONT_ASK_STATE_KEY));
	}
}
