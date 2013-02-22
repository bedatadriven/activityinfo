

package org.activityinfo.client.dispatch.monitor;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.i18n.I18N;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.MessageBox;

/**
 * Uses a GXT loading mask on a component to keep the user updated on
 * the progress of an asynchronous call

 * The monitor allows a limited number of retries (defaults to two) before giving up.
 */
public class MaskingAsyncMonitor implements AsyncMonitor {

    private String maskingText;
    private Component panel;
    private String connectionText;

    public MaskingAsyncMonitor(Component panel, String connectionText) {
        this.panel = panel;
        this.connectionText = connectionText;
    }

    public MaskingAsyncMonitor(Component panel, String connectionText, int maxRetries) {
        this.panel = panel;
        this.connectionText = connectionText;
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
        if (panel.isRendered()) {
            panel.el().mask(maskingText);
        } else {
            panel.addListener(panel instanceof Container ? Events.AfterLayout : Events.Render,
                    new Listener<ComponentEvent>() {
                        @Override
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
        maskingText = I18N.CONSTANTS.connectionProblem();
        mask();
    }

    @Override
    public boolean onRetrying() {
        maskingText = I18N.CONSTANTS.retrying();
        mask();
        return true;
    }


    @Override
    public void onServerError() {

        MessageBox.alert(I18N.CONSTANTS.error(), I18N.CONSTANTS.serverError(), null);

        unmask();
    }

    private void unmask() {
        if (this.panel.isRendered()) {
            this.panel.unmask();
        }
        maskingText = null;
    }

    @Override
    public void onCompleted() {
        unmask();
    }
}
