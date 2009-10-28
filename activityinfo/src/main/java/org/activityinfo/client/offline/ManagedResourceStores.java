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

import com.google.gwt.gears.client.localserver.ManagedResourceStore;
import com.google.gwt.gears.client.localserver.LocalServer;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.GearsException;
import com.google.gwt.core.client.GWT;

/**
 *
 * Convience class for accessing ActivityInfo <code>ManagedResourceStore</code>s
 *
 * @author Alex Bertram
 */
public class ManagedResourceStores {

    public static String OFFLINE_COOKIE_NAME = "offline";

    /**
     * Gets the <code>ManagedResourceStore</code> common to all permutations of the app.
     *
     * @return the <code>ManagedResourceStore</code> common to all permutations of the app.
     */
    public static ManagedResourceStore getCommon() {
        LocalServer server = Factory.getInstance().createLocalServer();
        ManagedResourceStore store = server.createManagedStore("ActivityInfo");
        store.setManifestUrl(GWT.getModuleBaseURL() + GWT.getModuleName() + ".nocache.manifest");
        store.checkForUpdate();
        return store;
    }

    /**
     * Gets the permutations-specific <code>ManagedResourceStore</code>
     *
     * @return the permutations-specific <code>ManagedResourceStore</code>
     */
    public static ManagedResourceStore getPermutation() {
        LocalServer server = Factory.getInstance().createLocalServer();
        ManagedResourceStore store = server.createManagedStore(GWT.getPermutationStrongName());
        store.setManifestUrl(getPermutationManifestName());
        return store;
    }

    /**
     *
     * Checks the offline availablity of the permutations-specific javascript.
     *
     * @return True if the permutation-specific <code>ManagedResourceStore</code> has
     * already been created and downloaded.
     */
    public static boolean isPermutationAvailable() {
        LocalServer server = Factory.getInstance().createLocalServer();
        try {
            return server.canServeLocally(getPermutationManifestName());
        } catch (GearsException e) {
            return false;
        }
    }

    private static String getPermutationManifestName() {
        return GWT.getModuleBaseURL() +
            GWT.getPermutationStrongName() + ".cache.html";
    }

}
