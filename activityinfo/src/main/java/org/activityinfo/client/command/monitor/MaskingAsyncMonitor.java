package org.activityinfo.client.command.monitor;


import org.activityinfo.client.Application;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.BaseEvent;

/**
 * Uses a GXT loading mask on a component to keep the user updated on
 * the progress of an asynchronous call
 *
 * The monitor allows a certain number of retries before quitting.
 */
public class MaskingAsyncMonitor implements AsyncMonitor {

    String masked;
	Component panel;
	String connectionText;
    int maxRetries = 2;
	
	public MaskingAsyncMonitor(Component panel, String connectionText) {
		this.panel = panel;
		this.connectionText = connectionText;
		this.maxRetries = 2;
	}

    public MaskingAsyncMonitor(Component panel, String connectionText, int maxRetries) {
        this.panel = panel;
        this.connectionText = connectionText;
        this.maxRetries = maxRetries;
    }

    @Override
	public void beforeRequest() {

        masked = connectionText;

        mask();

	}

    private void mask() {
        if(panel.isRendered()) {
		    panel.el().mask(masked);
        } else {
            panel.addListener(panel instanceof Container ? Events.AfterLayout : Events.Render, 
                    new Listener<ComponentEvent>() {
                public void handleEvent(ComponentEvent be) {
                    if (masked != null) {
                        panel.el().mask(masked);
                    }
                    panel.removeListener(Events.Render, this);
                }
            });
        }
    }

    @Override
	public void onConnectionProblem() {
	    masked = Application.CONSTANTS.connectionProblem();
        mask();
	}

	@Override
	public boolean onRetrying() {
		masked = Application.CONSTANTS.retrying();
        mask();
		return true;
	}


	@Override
	public void onServerError() {

		MessageBox.alert("Erreur", Application.CONSTANTS.serverError(), null);

        unmask();
	}

    private void unmask() {
        if(this.panel.isRendered()) {
		    this.panel.unmask();
        }
        masked = null;
    }

    @Override
	public void onCompleted() {
		unmask();
	}
}
