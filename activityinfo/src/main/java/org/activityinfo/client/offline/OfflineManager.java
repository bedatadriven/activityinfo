/**
 * Permits offline access to ActivityInfo with the help of Gears
 */
package org.activityinfo.client.offline;


import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.GearsException;
import com.google.gwt.gears.client.localserver.ManagedResourceStore;
import com.google.gwt.gears.client.localserver.LocalServer;
import com.google.gwt.gears.client.localserver.ManagedResourceStoreErrorHandler;
import com.google.gwt.gears.client.localserver.ManagedResourceStoreCompleteHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import org.activityinfo.client.AppEvents;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.command.Authentication;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.offline.ui.OfflineStatusWindow;

/**
 * With regards to offline functionality, the application can be in one of three
 * states:
 *
 * <ol>
 * <li>Gears is not installed -- No possibility of going offline. The "Activate Offline Mode" button
 *    should simply direct users to Google's Gears web site. See
 *      {@link org.activityinfo.client.offline.ui.OfflineMenuDisabled}</li>
 *
 * <li>Gears is installed, but the user has not initiated offline mode. Just because gears is installed,
 *    we can't assume the user is prepared to download their innermost secrets to the computer they happen
 *    to be using. When the user clicks "Activate Offline Mode", we set the cookie to the magic value
 *    "offline=enabled" and prompt the user to restart. 
 *    {@link org.activityinfo.client.offline.ui.OfflineMenuDisabled}</li>
 *
 * <li> Gears is installed, offline is enabled, everything is great! The workaday
 *    {@link org.activityinfo.client.Application} class is replaced with
 *    {@link org.activityinfo.client.offline.OfflineApplication} Present the user with options to manage
 *    offline mode and check the update status.</li>
 * </ol>
 *
 * Note that this state is handled at COMPILE time and GWT will generate a set of permutations for each
 * of the three possibilities, substituting different implementations according to the rules in
 * Application.gwt.xml
 *
 *
 * @author Alex Bertram
 */
public class OfflineManager {

    private final EventBus eventBus;
    private final CommandService service;
    private final Authentication auth;
    private final OfflineStatusWindow statusWindow;

    @Inject
    public OfflineManager(EventBus eventBus, CommandService service, Authentication auth, OfflineStatusWindow statusWindow) {
        this.eventBus = eventBus;
        this.service = service;
        this.auth = auth;
        this.statusWindow = statusWindow;

        // first thing: do a sanity check. If OfflineManager is started, it means that
        // that we're in the "offline mode enabled" permutations, and to have gotten here,
        // we had to ask for permissions first. However, the universe is a crazy place,
        // (See, Douglas, A.) so double check that we have permission to access the database

        if(!Factory.getInstance().hasPermission()) {
            // au cas contraire, bail with extreme prejudice.

            Cookies.removeCookie("offline");
            Window.Location.reload();
            return;
        }

        // ok, the next thing, verify that make sure that we've got a current
        // version of the software stored on the client so we start ActivityInfo

        if(GWT.isScript()) // don't screw around with MRS's in hosted mode -- it doesn't seem to work well
            updateManagedResourceStore();

        // listen for a request to disable offline mode
        eventBus.addListener(AppEvents.DisableOfflineMode, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent baseEvent) {
                disableOfflineMode();
            }
        });

    }

    /**
     *
     * Checks for updates to the common <code>ManagedResourceStore</code> and assures that we
     * have loaded the permutation-specific ManagedResourceStore
     *
     * <ul>
     * <li>{@link org.activityinfo.linker.ApplicationLinker} for notes on how the manifests are generated
     * <li>{@link com.google.gwt.gears.client.localserver.ManagedResourceStore}
     * <li>http://code.google.com/p/gwt-google-apis/wiki/GearsGettingStartedOffline</li>
     * <li>http://code.google.com/apis/gears/api_localserver.html</li>
     * </li>
     */
    public void updateManagedResourceStore() {

        // first check on the common MRS
        //ManagedResourceStore common = ManagedResourceStores.getCommon();
        //TODO: check common store (actually, do we need to? should be done automatically
        // and we can prompt the user if we get an RPC error)

        // assure that we have the permutation MRS downloaded
        // (this is not done until the first load)
        if(!ManagedResourceStores.isPermutationAvailable()) {
            ManagedResourceStore store = ManagedResourceStores.getPermutation();
            store.setOnErrorHandler(new ManagedResourceStoreErrorHandler() {
                @Override
                public void onError(ManagedResourceStoreErrorEvent error) {
                    MessageBox.alert("Erreur Chargement Hors Connexion", error.getMessage(), null);
                }
            });
            store.setOnCompleteHandler(new ManagedResourceStoreCompleteHandler() {
                @Override
                public void onComplete(ManagedResourceStoreCompleteEvent event) {
                    MessageBox.alert("Hors Connexion!", "Permutation-specific element is downloaded.", null);
                }
            });
        }
    }

    private void disableOfflineMode() {
        // remove the cookie
        Cookies.removeCookie("offline");

        Window.Location.reload();

        // TODO: prompt for removal of offline database
    }

}
