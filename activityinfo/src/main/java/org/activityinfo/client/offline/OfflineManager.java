package org.activityinfo.client.offline;

import org.activityinfo.client.AppEvents;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.command.Authentication;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.offline.dao.AuthDAO;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.GXT;
import com.google.gwt.gears.client.localserver.ManagedResourceStore;
import com.google.gwt.gears.client.GearsException;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.DatabaseException;
import com.google.gwt.gears.client.database.ResultSet;
import com.google.gwt.gears.offline.client.Offline;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;

import java.util.List;
import java.util.ArrayList;
/*
 * @author Alex Bertram
 */

public class OfflineManager {

    private final EventBus eventBus;
    private final CommandService service;
    private final Authentication auth;

    private boolean offlineEnabled;

    private Database userDb;
    private OfflineSchemaCache schemaCache;

    @Inject
    public OfflineManager(EventBus eventBus, CommandService service, Authentication auth,
                          OfflineMenuButton button) {
        this.eventBus = eventBus;
        this.service = service;
        this.auth = auth;

        offlineEnabled = Factory.getInstance().hasPermission();
        if(offlineEnabled) {
            enableOffline();
        }


        button.setText(offlineEnabled ? "Désactiver Mode Hors Connexion" : "Activer Mode Hors Connexion");

        eventBus.addListener(AppEvents.GoOffline, new Listener<BaseEvent>() {

            public void handleEvent(BaseEvent be) {
                if(!offlineEnabled) {
                    enableOffline();
                }
            }
        });
    }

    public void enableOffline() {

        try {
            if(!Factory.getInstance().getPermission())
                return;

            // first, store our authentication info
            AuthDAO authDAO = new AuthDAO();
            int userId = authDAO.updateOrInsert(auth);

            // open the database specific to the user
            userDb = Factory.getInstance().createDatabase();
            userDb.open("user" + userId);

            // hook in the schema cache
        } catch (GearsException e) {
            GWT.log("OfflineManager: enableOffline() failed", e);
            offlineEnabled = false;
        }
    }



    public void createManagedResourceStore() {
        try {
            final ManagedResourceStore managedResourceStore = Offline.getManagedResourceStore();

            new Timer() {
                final String oldVersion = managedResourceStore.getCurrentVersion();
                String transferringData = "Transferring data";

                @Override
                public void run() {
                    switch (managedResourceStore.getUpdateStatus()) {
                        case ManagedResourceStore.UPDATE_OK:
                            if (managedResourceStore.getCurrentVersion().equals(oldVersion)) {
                                Info.display("Offline", "No update was available.");
                            } else {
                                Info.display("Offline", "Update to "
                                        + managedResourceStore.getCurrentVersion()
                                        + " was completed.  Please refresh the page to see the changes.");
                            }
                            break;
                        case ManagedResourceStore.UPDATE_CHECKING:
                        case ManagedResourceStore.UPDATE_DOWNLOADING:
                            transferringData += ".";
                            //statusLabel.setText(transferringData);
                            schedule(500);
                            break;
                        case ManagedResourceStore.UPDATE_FAILED:
                            Info.display("Offline", managedResourceStore.getLastErrorMessage());
                            break;
                    }
                }
            }.schedule(500);

        } catch (GearsException e) {            
            Window.alert(e.getMessage());
        }

    }




}
