package org.activityinfo.desktop.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.Window;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
/*
 * @author Alex Bertram
 */

public class DesktopApp implements EntryPoint {

    public void onModuleLoad() {

        Button btn = new Button("Hello World", new ClickHandler() {
            public void onClick(ClickEvent event) {
                Window.alert(GWT.getModuleBaseURL());
            }
        });
        RootPanel.get().add(btn);
    }
}