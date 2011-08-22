/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import java.sql.Connection;

import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.offline.command.LocalDispatcher;
import org.sigmah.client.offline.command.handler.LocalCreateEntityHandler;
import org.sigmah.client.offline.sync.Synchronizer;
import org.sigmah.client.offline.sync.SynchronizerImpl;
import org.sigmah.client.offline.ui.OfflineView;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.handler.GetAdminEntitiesHandler;
import org.sigmah.shared.command.handler.GetSchemaHandler;
import org.sigmah.shared.command.handler.GetSitesHandler;
import org.sigmah.shared.dao.SiteTableDAO;
import org.sigmah.shared.dao.SqlDialect;
import org.sigmah.shared.dao.SqlSiteTableDAO;
import org.sigmah.shared.dao.SqliteDialect;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlDatabaseFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * @author Alex Bertram
 */
public class OfflineModule extends AbstractGinModule {

    @Override
    protected void configure() {

        bind(OfflineController.View.class).to(OfflineView.class);
        bind(Synchronizer.class).to(SynchronizerImpl.class);

        //DAOs for off-line
        bind(SqlDialect.class).to(SqliteDialect.class).in(Singleton.class);
        bind(SiteTableDAO.class).to(SqlSiteTableDAO.class).in(Singleton.class);

    }

    @Provides
    protected Connection provideConnection(Authentication auth) {
        return new DummyConnection();
    }
    
    @Provides
    @Singleton
    protected SqlDatabase provideSqlDatabase(Authentication auth) {
    	SqlDatabaseFactory factory = GWT.create(SqlDatabaseFactory.class);
    	return factory.open(auth.getLocalDbName());
    }

    @Provides
    protected LocalDispatcher provideLocalDispatcher(Authentication auth,
            GetSchemaHandler schemaHandler,
            GetSitesHandler sitesHandler,
            GetAdminEntitiesHandler adminHandler,
            LocalCreateEntityHandler createEntityHandler) {

        LocalDispatcher dispatcher = new LocalDispatcher(auth);
        dispatcher.registerHandler(GetSchema.class, schemaHandler);
        dispatcher.registerHandler(GetSites.class, sitesHandler);
        dispatcher.registerHandler(GetAdminEntities.class, adminHandler);
      /*  dispatcher.registerHandler(CreateEntity.class, createEntityHandler);*/
        
        return dispatcher;
    }
}
