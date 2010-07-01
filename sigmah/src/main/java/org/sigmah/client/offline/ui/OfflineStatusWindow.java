/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.ui;

import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import org.sigmah.client.i18n.I18N;

/**
 * @author Alex Bertram
 */
public class OfflineStatusWindow extends Window {
    private Text appStatus;
    private Text schemaStatus;

    public OfflineStatusWindow() {
        setHeading(I18N.CONSTANTS.statusOfflineMode());
        setWidth(300);
        setHeight(200);

        TableLayout layout = new TableLayout(2);
        setLayout(layout);

        appStatus = new Text();

        add(new Text(I18N.CONSTANTS.loadingSoftware()));
        add(appStatus);

        schemaStatus = new Text();
        add(new Text(I18N.CONSTANTS.schema()));
        add(schemaStatus);
    }

    public void setAppStatus(String message) {
        appStatus.setText(message);
    }

}
