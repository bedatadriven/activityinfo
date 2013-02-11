package org.activityinfo.client.offline.ui;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.offline.LocalController;
import org.activityinfo.client.offline.capability.ProfileResources;
import org.activityinfo.client.util.state.CrossSessionStateProvider;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.inject.Inject;


/**
 * Dialog presented to the user on startup to prompt 
 * activation of offline mode
 * 
 */
public class PromptOfflineDialog extends BasePromptDialog {

	private static final String DONT_ASK_STATE_KEY = "offlineSilent";

	private final CrossSessionStateProvider stateProvider;
	private final LocalController offlineController;
	
	@Inject
	public PromptOfflineDialog(CrossSessionStateProvider stateProvider,
			LocalController controller) {
		super(capabilityProfile.getInstallInstructions());
		
		this.stateProvider = stateProvider;
		this.offlineController = controller;
		
		setWidth(500);
		setHeight(350);
		setHeading(I18N.CONSTANTS.installOffline());
		setModal(true);
		
		Html bodyHtml = new Html(capabilityProfile.getInstallInstructions());
		bodyHtml.addStyleName(ProfileResources.INSTANCE.style().startupDialogBody());
        
		add(bodyHtml);

		getButtonBar().removeAll();
		
		if(capabilityProfile.isOfflineModeSupported()) {
			addButton(new Button(I18N.CONSTANTS.installOffline(), new SelectionListener<ButtonEvent>() {
				
				@Override
				public void componentSelected(ButtonEvent ce) { 
					offlineController.install();
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
		stateProvider.set(DONT_ASK_STATE_KEY, Boolean.TRUE.toString());
		hide();
	}

	
	public static boolean shouldAskAgain(CrossSessionStateProvider stateProvider ) {
		return !Boolean.TRUE.toString().equals(stateProvider.getString(DONT_ASK_STATE_KEY));
	}
}
