/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import com.bedatadriven.rebar.sync.client.BulkUpdaterAsync;
import com.bedatadriven.rebar.sync.client.impl.GearsBulkUpdater;
import com.google.gwt.inject.client.AbstractGinModule;
import org.sigmah.client.offline.ui.OfflineMenu;

import java.sql.Connection;

/**
 * @author Alex Bertram
 */
public class OfflineModule extends AbstractGinModule {

    @Override
    protected void configure() {

        bind(OfflineManager.View.class).to(OfflineMenu.class);
        bind(Connection.class).toProvider(ConnectionProvider.class);
        bind(BulkUpdaterAsync.class).to(GearsBulkUpdater.class);

    }
}
