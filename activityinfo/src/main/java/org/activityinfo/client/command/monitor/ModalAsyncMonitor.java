package org.activityinfo.client.command.monitor;


import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.WaitMessageBox;
import com.extjs.gxt.ui.client.widget.MessageBox.MessageBoxType;
import com.google.gwt.core.client.GWT;
import org.activityinfo.client.Application;
import org.activityinfo.shared.i18n.UIConstants;

/**
 * Uses a GXT modal dialog box to keep the user updated on
 * the progress of an asynchronous call
 *
 * The monitor allows a limited number of retries (defaults to two) before giving up.
 */
public class ModalAsyncMonitor implements AsyncMonitor {

	protected MessageBox box;
	protected boolean cancelled;
    protected boolean inProgress = false;
	
	public ModalAsyncMonitor() {
		
	}


	@Override
	public void beforeRequest() {

        inProgress = true;

		box = new WaitMessageBox();
		box.setTitle("ActivityInfo");
		box.setModal(true);
		box.setMessage(Application.CONSTANTS.loading());
		box.setButtons(MessageBox.CANCEL);
		
		box.addCallback(new Listener<MessageBoxEvent>() {
			public void handleEvent(MessageBoxEvent be) {
				cancelled = true;
				
				/* 
				 * we can't close the box right away because the 
				 * call could still succeed at this point, and the monitor
				 * doesn't have any say over whether the result is sent
				 * back to the original caller.
				 */
                if(!inProgress) {
                    box.close();
                }
			}
		});
		box.show();
	}

	
	public void onConnectionProblem() {

        inProgress = false;

		UIConstants constants = GWT.create(UIConstants.class);
		
		box.updateText(constants.connectionProblem());
	}

	public boolean onRetrying() {
		if(cancelled) {

			box.close();
			
			return false;
		
		} else {

            inProgress = true;
		
			UIConstants constants = GWT.create(UIConstants.class);
			box.updateText(constants.retrying());
			
			return true;
		}
	}


	@Override
	public void onServerError() {

        inProgress = false;

		box.close();

		MessageBox.alert("ActivityInfo", Application.CONSTANTS.serverError(), null);

	}

	@Override
	public void onCompleted() {
		box.close();
	}
}
