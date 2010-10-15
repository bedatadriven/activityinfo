/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.ui;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Observable;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.inject.Singleton;

import java.util.Date;

/**
 * @author Alex Bertram
 */
@Singleton
public class OfflineView extends Button implements OfflinePresenter.View {

    private OfflineStatusWindow window;


    public OfflineView() {
        window = new OfflineStatusWindow();
        addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                window.show();
            }
        });
    }

    @Override
    public void setInstalled(boolean installed) {

    }

    @Override
    public Observable getInstallButton() {
        return this;
    }

    @Override
    public Observable getSyncNowButton() {
        return window.getSyncNowButton();
    }

    @Override
    public Observable getToggleOfflineButton() {
        return window.getToggleOfflineButton();
    }

    @Override
    public void setOffline(boolean offline) {
        if(offline) {
            setText("OFFLINE");
            window.getToggleOfflineButton().setText("Go Online");
        } else {
            setText("ONLINE");
            window.getToggleOfflineButton().setText("Offline");
        }
    }

    @Override
    public void setProgress(String taskDescription, double percentComplete) {
        window.getProgressBar().updateProgress(percentComplete / 100, taskDescription);
    }

    @Override
    public void setLastSyncDate(Date date) {

    }

}
