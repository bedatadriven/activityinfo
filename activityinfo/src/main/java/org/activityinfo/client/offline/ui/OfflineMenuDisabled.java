package org.activityinfo.client.offline.ui;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.gears.client.Factory;
import com.google.inject.Singleton;
import org.activityinfo.client.Application;
import org.activityinfo.client.offline.Installer;

@Singleton
public class OfflineMenuDisabled extends OfflineMenu {

    public OfflineMenuDisabled() {
    }

    @Override
    protected void init() {
        this.setText(Application.CONSTANTS.enableOffline());
        this.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {

                // check to see if Gears is installed and accessible.

                if (Factory.getInstance() != null) {
                    Installer installer = new Installer();
                    installer.install(auth);
                } else {
                    MessageBox.alert(Application.CONSTANTS.offlineMode(), Application.CONSTANTS.gearsRequired(), null);
                }
            }
        });
    }
}
