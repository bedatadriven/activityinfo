package org.sigmah.client.offline.ui;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.offline.ui.OfflinePresenter.PromptCallback;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;

public class ConnectionDialog extends Window {
	
	private PromptCallback callback;
	private Status status;
	private Button tryToConnect;
	private Button cancelButton;
	
	public ConnectionDialog() {
	
		setHeading(I18N.CONSTANTS.connectionRequired());
		setModal(true);
		setClosable(false);
		setWidth(400);
		setHeight(150);
        setBodyStyle("padding: 5px; font-size: 12px");
		
		add(new Html(I18N.CONSTANTS.connectionRequiredExplanation()));
		
		tryToConnect = new Button(I18N.CONSTANTS.tryToConnect(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				callback.onTryToConnect();
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
        status.setWidth(200);
        this.getButtonBar().add(status);
		
		addButton(tryToConnect);
		addButton(cancelButton);
	}

	public void setCallback(PromptCallback callback) {
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
	
	public void setFailed() {
		tryToConnect.enable();
		status.clearStatus(I18N.CONSTANTS.couldNotConnect());
	}

}
