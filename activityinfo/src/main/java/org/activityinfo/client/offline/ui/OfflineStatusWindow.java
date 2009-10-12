package org.activityinfo.client.offline.ui;

import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;

/**
 * @author Alex Bertram
 */
public class OfflineStatusWindow extends Window {
    private Text appStatus;
    private Text schemaStatus;

    public OfflineStatusWindow() {

        setHeading("Status Mode Hors Connexion");
        setWidth(300);
        setHeight(200);

        TableLayout layout = new TableLayout(2);
        setLayout(layout);

        appStatus = new Text();

        add(new Text("Chargement de logiciel"));
        add(appStatus);

        schemaStatus = new Text();
        add(new Text("Schema"));
        add(schemaStatus);
    }

    public void setAppStatus(String message) {
        appStatus.setText(message);
    }

}
