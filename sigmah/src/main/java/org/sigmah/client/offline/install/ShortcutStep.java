/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.install;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.desktop.DesktopIcons;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ShortcutStep implements Step {

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void execute(AsyncCallback<Void> callback) {
        DesktopIcons desktopIcons = DesktopIcons.create()
        .set128x128(GWT.getModuleBaseURL() + "desktopicons/128x128.png")
        .set48x48(GWT.getModuleBaseURL() + "desktopicons/48x48.png")
        .set32x32(GWT.getModuleBaseURL() + "desktopicons/32x32.png")
        .set16x16(GWT.getModuleBaseURL() + "desktopicons/16x16.png");

        try {
            Factory.getInstance().createDesktop().createShortcut("ActivityInfo",
                   GWT.getModuleBaseURL() + GWT.getModuleName() + ".offline.html",
                   desktopIcons);
        } catch (Exception e) {
            Log.error("Error creating desktop icons", e);
        }
        callback.onSuccess(null);
    }
}
