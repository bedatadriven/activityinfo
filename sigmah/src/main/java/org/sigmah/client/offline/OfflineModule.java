/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import org.sigmah.client.offline.command.HandlerRegistry;
import org.sigmah.client.offline.sync.Synchronizer;
import org.sigmah.client.offline.sync.SynchronizerImpl;
import org.sigmah.client.offline.ui.OfflineView;
import org.sigmah.shared.auth.AuthenticatedUser;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlDatabaseFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;


public class OfflineModule extends AbstractGinModule {

    @Override
    protected void configure() {

        bind(OfflineController.View.class).to(OfflineView.class);
        bind(Synchronizer.class).to(SynchronizerImpl.class);
        bind(HandlerRegistry.class).toProvider(HandlerRegistryProvider.class);
    }
    
    @Provides
    @Singleton
    protected SqlDatabase provideSqlDatabase(AuthenticatedUser auth) {
    	SqlDatabaseFactory factory = GWT.create(SqlDatabaseFactory.class);
    	return factory.open("user" + auth.getUserId());
    }
}
