/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.client.offline;

import org.activityinfo.client.offline.dao.AuthDAO;
import org.activityinfo.client.command.Authentication;

import java.util.Date;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.gears.client.database.DatabaseException;
import com.google.gwt.gears.client.localserver.ManagedResourceStore;
import com.google.gwt.gears.client.localserver.ManagedResourceStoreProgressHandler;
import com.google.gwt.gears.client.localserver.ManagedResourceStoreErrorHandler;
import com.google.gwt.gears.client.localserver.ManagedResourceStoreCompleteHandler;
import com.google.gwt.gears.client.GearsException;
import com.google.gwt.core.client.GWT;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.Listener;

/**
 * Installs the offline application
 *
 * @author Alex Bertram
 */
public class Installer {

    public void install(Authentication auth) {

        // first store the current user's authorization in the gears database
        try {
            AuthDAO authDAO = new AuthDAO();
            authDAO.updateOrInsert(auth);

            // this cookie tells the startup script to use the GWT permutation
            // that includes the offline implementation of stuff
            Date expiryDate = new Date(60l * 365l * 24 * 60 * 60 * 1000);
            Cookies.setCookie("offline", "enabled", expiryDate);

            // Add a desktop shortcut for ActivityInfo
//                                 Desktop desktop = Factory.getInstance().createDesktop();
//                    desktop.createShortcut();

            if(GWT.isScript()) {
                downloadCommon();
            } else {
                promptReload();
            }
        } catch (DatabaseException e) {
            GWT.log("Could not store authentication in the database", e);
        }
    }

    /**
     * Downloads the common <code>ManagedResourceStore</code>, which contains the
     * general resources from the public/ folder as well as the host page and selection script
     *
     * @param callback Callback upon completion of download
     */
    public void downloadCommon() {

        final MessageBox box = new MessageBox();
        box.setType(MessageBox.MessageBoxType.PROGRESSS);
        box.setMessage("Chargement de module hors connexion en cours...");  // TODO: i18n

        ManagedResourceStore common = ManagedResourceStores.getCommon();

        common.setOnProgressHandler(new ManagedResourceStoreProgressHandler() {
            @Override
            public void onProgress(ManagedResourceStoreProgressEvent event) {
                box.updateProgress(event.getFilesComplete() / event.getFilesTotal(),
                        "Chargement de module hors connexion en cours...");
            }
        });
        common.setOnErrorHandler(new ManagedResourceStoreErrorHandler() {
            @Override
            public void onError(final ManagedResourceStoreErrorEvent error) {
                MessageBox.alert("Chargement", "Il y avait une error lors de chargement de module hors connexion:<br> "
                        + error.getMessage(), new Listener<MessageBoxEvent>() {
                    @Override
                    public void handleEvent(MessageBoxEvent be) {
                        box.close();
                  }
                });
            }
        });
        common.setOnCompleteHandler(new ManagedResourceStoreCompleteHandler() {
            @Override
            public void onComplete(ManagedResourceStoreCompleteEvent event) {
                box.close();
                promptReload();
            }
        });

        box.show();
        common.setEnabled(true);
        common.checkForUpdate();
    }

    public void promptReload() {
        MessageBox prompt = new MessageBox();
        prompt.setButtons(MessageBox.OKCANCEL);
        prompt.setType(MessageBox.MessageBoxType.ALERT);
        prompt.setTitle("Chargement initial complet");
        prompt.setMessage("Chargement initiale de module hors connexion a terminé. " +
                "Veuillez reactualizer le page pour completer l'installation.");
        prompt.addCallback(new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                if(be.getButtonClicked().getItemId().equals(Dialog.OK)) {
                    Window.Location.reload();
                }
            }
        });
        prompt.show();
    }

}


