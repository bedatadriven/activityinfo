package org.activityinfo.client.command.monitor;


import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.MessageBox;
import org.activityinfo.client.Application;

/**
 * Uses a GXT loading mask on a component to keep the user updated on
 * the progress of an asynchronous call
 *
 * The monitor allows a limited number of retries (defaults to two) before giving up.
 */
public class MaskingAsyncMonitor implements AsyncMonitor {

    String maskingText;
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

        maskingText = connectionText;

        mask();

	}

    private void mask() {
        /* If the component is not yet rendered, wait until after it is all layed out
         * before applying the mask.
         */
        if(panel.isRendered()) {
		    panel.el().mask(maskingText);
        } else {
            panel.addListener(panel instanceof Container ? Events.AfterLayout : Events.Render, 
                    new Listener<ComponentEvent>() {
                public void handleEvent(ComponentEvent be) {
                    /*
                     * If the call is still in progress,
                     * apply the mask now.
                     */
                    if (maskingText != null) {
                        panel.el().mask(maskingText);
                    }
                    panel.removeListener(Events.Render, this);
                }
            });
        }
    }

    @Override
	public void onConnectionProblem() {
	    maskingText = Application.CONSTANTS.connectionProblem();
        mask();
	}

	@Override
	public boolean onRetrying() {
		maskingText = Application.CONSTANTS.retrying();
        mask();
		return true;
	}


	@Override
	public void onServerError() {

		MessageBox.alert("Erreur", Application.CONSTANTS.serverError(), null);  // TODO: i18n

        unmask();
	}

    private void unmask() {
        if(this.panel.isRendered()) {
		    this.panel.unmask();
        }
        maskingText = null;
    }

    @Override
	public void onCompleted() {
		unmask();
	}
}
