/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.ui;

import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;

/**
 * @author Alex Bertram
 */
class StatusWindow extends Window {

    private ProgressBar progressBar;
    private Button syncNowButton;
    private Button toggleOfflineButton;

    public StatusWindow() {
        setHeading(I18N.CONSTANTS.statusOfflineMode());
        setWidth(300);
        setHeight(200);
        setLayout(new CenterLayout());

        progressBar = new ProgressBar();
        progressBar.setWidth(200);
        add(progressBar);

        syncNowButton = new Button("Synchronize Now", IconImageBundle.ICONS.onlineSynced());
        toggleOfflineButton = new Button(I18N.CONSTANTS.switchToOnline());

        addButton(syncNowButton);
        addButton(toggleOfflineButton);
   }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public Button getSyncNowButton() {
        return syncNowButton;
    }

    public Button getToggleOfflineButton() {
        return toggleOfflineButton;
    }
}
