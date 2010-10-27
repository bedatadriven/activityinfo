/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import com.bedatadriven.rebar.sql.client.GearsConnectionFactory;
import com.bedatadriven.rebar.sync.client.BulkUpdaterAsync;
import com.bedatadriven.rebar.sync.client.impl.GearsBulkUpdater;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.offline.command.LocalDispatcher;
import org.sigmah.client.offline.command.handler.LocalGetAdminEntitiesHandler;
import org.sigmah.client.offline.command.handler.LocalGetSchemaHandler;
import org.sigmah.client.offline.ui.OfflinePresenter;
import org.sigmah.client.offline.ui.OfflineView;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.handler.GetSitesHandler;
import org.sigmah.shared.dao.SQLDialect;
import org.sigmah.shared.dao.SiteTableDAO;
import org.sigmah.shared.dao.SqlSiteTableDAO;
import org.sigmah.shared.dao.SqliteDialect;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Alex Bertram
 */
public class OfflineModule extends AbstractGinModule {

    @Override
    protected void configure() {

        bind(OfflinePresenter.View.class).to(OfflineView.class);
        bind(OfflineGateway.class).to(OfflineImpl.class);
        bind(BulkUpdaterAsync.class).to(GearsBulkUpdater.class);

        //DAOs for off-line
        bind(SQLDialect.class).to(SqliteDialect.class).in(Singleton.class);
        bind(SiteTableDAO.class).to(SqlSiteTableDAO.class).in(Singleton.class);

    }

    @Provides
    @Singleton
    protected Connection provideConnection(Authentication auth) {
        try {
            return GearsConnectionFactory.getConnection(auth.getLocalDbName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    protected LocalDispatcher provideLocalDispatcher(Authentication auth,
            LocalGetSchemaHandler schemaHandler,
            GetSitesHandler sitesHandler,
            LocalGetAdminEntitiesHandler adminHandler) {

        LocalDispatcher dispatcher = new LocalDispatcher(auth);
        dispatcher.registerHandler(GetSchema.class, schemaHandler);
        dispatcher.registerHandler(GetSites.class, sitesHandler);
        dispatcher.registerHandler(GetAdminEntities.class, adminHandler);

        return dispatcher;
    }



}
