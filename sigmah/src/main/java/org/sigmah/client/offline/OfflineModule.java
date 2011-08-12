/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import java.sql.Connection;
import java.sql.SQLException;

import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.offline.command.LocalDispatcher;
import org.sigmah.client.offline.command.handler.LocalCreateEntityHandler;
import org.sigmah.client.offline.command.handler.LocalGetAdminEntitiesHandler;
import org.sigmah.client.offline.command.handler.LocalGetSchemaHandler;
import org.sigmah.client.offline.ui.OfflinePresenter;
import org.sigmah.client.offline.ui.OfflineView;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.handler.GetSitesHandler;
import org.sigmah.shared.dao.SqlDialect;
import org.sigmah.shared.dao.SiteTableDAO;
import org.sigmah.shared.dao.SqlSiteTableDAO;
import org.sigmah.shared.dao.SqliteDialect;

import com.bedatadriven.rebar.sql.client.GearsConnectionFactory;
import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlDatabaseFactory;
import com.bedatadriven.rebar.sync.client.BulkUpdaterAsync;
import com.bedatadriven.rebar.sync.client.impl.GearsBulkUpdater;
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

        bind(OfflinePresenter.View.class).to(OfflineView.class);
        bind(OfflineGateway.class).to(OfflineImpl.class);

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
            LocalGetSchemaHandler schemaHandler,
            GetSitesHandler sitesHandler,
            LocalGetAdminEntitiesHandler adminHandler,
            LocalCreateEntityHandler createEntityHandler) {

        LocalDispatcher dispatcher = new LocalDispatcher(auth);
       /* dispatcher.registerHandler(GetSchema.class, schemaHandler);
        dispatcher.registerHandler(GetSites.class, sitesHandler);
        dispatcher.registerHandler(GetAdminEntities.class, adminHandler);
        dispatcher.registerHandler(CreateEntity.class, createEntityHandler);*/
        
        return dispatcher;
    }



}
