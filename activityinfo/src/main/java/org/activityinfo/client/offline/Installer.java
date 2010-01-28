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

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.gears.client.database.DatabaseException;
import com.google.gwt.user.client.Window;
import org.activityinfo.client.Application;
import org.activityinfo.client.command.Authentication;
import org.activityinfo.client.offline.dao.AuthDAO;

import java.util.Date;

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


            // Add a desktop shortcut for ActivityInfo
//                                 Desktop desktop = Factory.getInstance().createDesktop();
//                    desktop.createShortcut();

            if (GWT.isScript()) {
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
     */
    public void downloadCommon() {
    }

    public void promptReload() {
        MessageBox prompt = new MessageBox();
        prompt.setButtons(MessageBox.OKCANCEL);
        prompt.setIcon(MessageBox.INFO);
        prompt.setTitle(Application.CONSTANTS.loadingComplets());
        prompt.setMessage(Application.CONSTANTS.loadingCompletsMsg());
        prompt.addCallback(new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked().getItemId().equals(Dialog.OK)) {
                    Window.Location.reload();
                }
            }
        });
        prompt.show();
    }

}


