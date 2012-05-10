package org.activityinfo.client.offline.ui;

import org.activityinfo.client.SessionUtil;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.offline.OfflineController.PromptConnectCallback;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;

public class ConnectionDialog extends Window {
	
	private PromptConnectCallback callback;
	private Status status;
	private Button tryToConnect;
	private Button cancelButton;
	
	private interface State {
		void buttonClicked();
	}
	
	private State currentState;
	private Html messageHtml;
	
	public ConnectionDialog() {
	
		setHeading(I18N.CONSTANTS.connectionRequired());
		setModal(true);
		setClosable(false);
		setWidth(475);
		setHeight(150);
        setBodyStyle("padding: 5px; font-size: 12px");
		
        currentState = new TryingToConnectState();
        
		messageHtml = new Html(I18N.CONSTANTS.connectionRequiredExplanation());
		add(messageHtml);
		
		tryToConnect = new Button(I18N.CONSTANTS.tryToConnect(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				currentState.buttonClicked();
			}
		});
		
		cancelButton = new Button(I18N.CONSTANTS.cancel(), new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				hide();
				callback.onCancel();
			}
			
		});
	
		status = new Status();
        status.setWidth(150);
        this.getButtonBar().add(status);
		
		addButton(tryToConnect);
		addButton(cancelButton);
	}

	public void setCallback(PromptConnectCallback callback) {
		this.callback = callback;
	}
	
	public void clearStatus() {
		tryToConnect.enable();
		status.clearStatus("");
	}
	
	public void setBusy() {
		tryToConnect.disable();
		status.setBusy(I18N.CONSTANTS.connecting());
	}
	
	public void setConnectionFailed() {
		tryToConnect.enable();
		status.clearStatus(I18N.CONSTANTS.couldNotConnect());
	}
	

	public void setServerUnavailable() {
		tryToConnect.enable();
		status.clearStatus(I18N.CONSTANTS.couldNotConnect());		
	}

	public void setSessionExpired() {
		messageHtml.setHtml(I18N.CONSTANTS.authenticateToGoOnline());

		tryToConnect.setText(I18N.CONSTANTS.login());
		tryToConnect.enable();

		currentState = new SessionExpiredState();

		status.clearStatus(null);

	}
	
	private class TryingToConnectState implements State {
		@Override
		public void buttonClicked() {
			callback.onTryToConnect();
		}
	}

	private class SessionExpiredState implements State {
		@Override
		public void buttonClicked() {
			SessionUtil.forceLogin();
		}
	}

	
}
