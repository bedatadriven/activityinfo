package org.activityinfo.client.page.common.dialog;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import org.activityinfo.client.Application;
import org.activityinfo.client.command.monitor.AsyncMonitor;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SavePromptMessageBox extends Window implements AsyncMonitor {

    private Status status;
    private SaveChangesCallback callback;
    private Button saveButton;
    private Button discardButton;
    private Button cancelButton;
    private boolean asyncCallCancelled;

    public enum Choice {
        SAVE,
        DISCARD,
        CANCEL
    };


    public SavePromptMessageBox() {
   /*
		 * Configure this window
		 */
		setModal(true);
		setClosable(false);
		setBodyStyle("padding: 5px;");
        setWidth(450);
        setHeight(200);
        setHeading(Application.CONSTANTS.save());
        setLayout(new CenterLayout());
		add(new Html(Application.CONSTANTS.promptSave()));


		/*
		 * Create the status button bar
		 */

		status = new Status();
		this.getButtonBar().add(status);

        saveButton = new Button(Application.CONSTANTS.save());
		saveButton.setIcon(Application.ICONS.save());
		saveButton.addListener(Events.Select, new Listener<ButtonEvent>() {
			public void handleEvent(ButtonEvent be) {
				callback.save(SavePromptMessageBox.this);
			}
		});
		addButton(saveButton);

        discardButton = new Button(Application.CONSTANTS.discardChanges());
		discardButton.setIcon(Application.ICONS.cancel());
        discardButton.addListener(Events.Select, new Listener<ButtonEvent>() {
            @Override
            public void handleEvent(ButtonEvent be) {
                hide();
                callback.discard();
            }
        });
        addButton(discardButton);


        cancelButton = new Button(Application.CONSTANTS.cancel());
		addButton(cancelButton);

		cancelButton.addListener(Events.Select, new Listener<ButtonEvent>() {
			public void handleEvent(ButtonEvent be) {
                hide();
                callback.cancel();
			}
		});
	}

	public void show(SaveChangesCallback callback) {
		this.callback = callback;

		show();
	}

	@Override
	public void beforeRequest() {

		saveButton.disable();
		discardButton.disable();

		asyncCallCancelled = false;

		status.setBusy(Application.CONSTANTS.saving());
	}


	@Override
	public void onConnectionProblem() {
		status.setBusy(Application.CONSTANTS.connectionProblem());
	}


	@Override
	public boolean onRetrying() {
		if(asyncCallCancelled)
			return false;

		cancelButton.disable();
		status.setBusy(Application.CONSTANTS.retrying());

		return true;
	}


	@Override
	public void onServerError() {

    	saveButton.enable();
		cancelButton.enable();
		status.clearStatus("");

		MessageBox.alert(this.getHeading(), Application.CONSTANTS.serverError(), null);
	}


	@Override
	public void onCompleted() {
		hide();
	}
}

