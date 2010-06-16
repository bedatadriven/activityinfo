/**
 * Permits offline access to ActivityInfo with the help of Gears
 */
package org.activityinfo.client.offline;


import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.offline.sync.InstallSteps;

/**
 * With regards to offline functionality, the application can be in one of three
 * states:
 * <p/>
 * <ol>
 * <li>Gears is not installed -- No possibility of going offline. The "Activate Offline Mode" button
 * should simply direct users to Google's Gears web site. See
 * <p/>
 * <li>Gears is installed, but the user has not initiated offline mode. Just because gears is installed,
 * we can't assume the user is prepared to download their innermost secrets to the computer they happen
 * to be using. When the user clicks "Activate Offline Mode", we set the cookie to the magic value
 * "offline=enabled" and prompt the user to restart.
 * <p/>
 * <li> Gears is installed, offline is enabled, everything is great!
 * </ol>
 * <p/>
 * Note that this state is handled at COMPILE time and GWT will generate a set of permutations for each
 * of the three possibilities, substituting different implementations according to the rules in
 * Application.gwt.xml
 *
 * @author Alex Bertram
 */
public class OfflineManager {
    private final View view;
    private final AppInjector injector;

    public interface View {
        Observable getEnableOfflineModeMenuItem();
    }

    @Inject
    public OfflineManager(View view, AppInjector injector) {
        this.view = view;
        this.injector = injector;
        this.view.getEnableOfflineModeMenuItem().addListener(Events.Select, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent baseEvent) {
                enableOffline();
            }
        });

    }

    private void enableOffline() {
        Factory gearsFactory = Factory.getInstance();
        if(gearsFactory == null) {
            Log.debug("OfflineManager: failing, Gears not installed");
            gearsNotInstalled();
        } else if(gearsFactory.hasPermission() || gearsFactory.getPermission()) {
            Log.debug("OfflineManager: starting offline install");
            startInstall();
        }
    }

    private void startInstall() {
        Log.debug("Starting offline installation...");
        InstallSteps steps = injector.getInstallSteps();
        steps.run();
    }

    private void gearsNotInstalled() {
        MessageBox.confirm("Offline Mode", "ActivityInfo currently requires the Gears plugin to function offline." +
                " Would you like to download the plugin now?", new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                Window.open("http://gears.google.com", "_blank", null);
            }
        });
    }
}
