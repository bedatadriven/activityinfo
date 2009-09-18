package org.activityinfo.client.command.monitor;


import org.activityinfo.client.Application;
import org.activityinfo.shared.i18n.UIConstants;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.MessageBox.MessageBoxType;
import com.google.gwt.core.client.GWT;

public class ModalAsyncMonitor implements AsyncMonitor {

	protected MessageBox box;
	protected boolean cancelled;
	
	public ModalAsyncMonitor() {
		
	}


	@Override
	public void beforeRequest() {

		box = new MessageBox();
		box.setTitle("ActivityInfo");
		box.setModal(true);
		box.setMessage(Application.CONSTANTS.loading());
		box.setType(MessageBoxType.WAIT);
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
			}
		});
		box.show();
	}

	
	public void onConnectionProblem() {
		UIConstants constants = GWT.create(UIConstants.class);
		
		box.updateText(constants.connectionProblem());
	}

	public boolean onRetrying() {
		if(cancelled) {

			box.close();
			
			return false;
		
		} else {
		
			UIConstants constants = GWT.create(UIConstants.class);
			box.updateText(constants.retrying());
			
			return true;
		}
	}


	@Override
	public void onServerError() {

		box.close();
		
		MessageBox.alert("ActivityInfo", Application.CONSTANTS.serverError(), null);
		
	}

	@Override
	public void onCompleted() {
		box.close();
	}
}
