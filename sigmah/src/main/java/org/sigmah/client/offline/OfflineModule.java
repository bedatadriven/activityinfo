/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.persistence.client.ConnectionProvider;
import com.bedatadriven.rebar.persistence.client.PersistenceUnit;
import com.bedatadriven.rebar.sync.client.BulkUpdaterAsync;
import com.bedatadriven.rebar.sync.client.impl.GearsBulkUpdater;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.dto.ClientDTOMapper;
import org.sigmah.client.inject.DummyConnection;
import org.sigmah.client.offline.command.LocalDispatcher;
import org.sigmah.client.offline.command.handler.LocalGetAdminEntitiesHandler;
import org.sigmah.client.offline.command.handler.LocalGetSchemaHandler;
import org.sigmah.client.offline.dao.*;
import org.sigmah.client.offline.ui.OfflinePresenter;
import org.sigmah.client.offline.ui.OfflineView;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.handler.GetSitesHandler;
import org.sigmah.shared.dao.*;
import org.sigmah.shared.domain.ActivityInfoOfflineUnit;
import org.sigmah.shared.dto.DTOMapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
        bind(ConnectionProvider.class).to(LocalConnectionProvider.class).in(Singleton.class);	
        bind(PersistenceUnit.class).to(ActivityInfoOfflineUnit.class).in(Singleton.class);


        //DAOs for off-line
        bind(SQLDialect.class).to(SqliteDialect.class).in(Singleton.class);
        bind(SiteTableDAO.class).to(SqlSiteTableDAO.class).in(Singleton.class);
    	bind(CountryDAO.class).to(CountryLocalDAO.class).in(Singleton.class);
    	bind(UserDatabaseDAO.class).to(UserDatabaseLocalDAO.class).in(Singleton.class);
    	bind(AdminDAO.class).to(AdminLocalDAO.class).in(Singleton.class);
    	bind(UserDAO.class).to(UserLocalDAO.class).in(Singleton.class);
    	bind(ActivityDAO.class).to(ActivityLocalDAO.class).in(Singleton.class);
    	
    	// DTO mapper
    	bind(DTOMapper.class).to(ClientDTOMapper.class);
    }
    
    
    @Provides
    protected EntityManagerFactory provideEntityManagerFactory (PersistenceUnit unit, ConnectionProvider conProvider) {
    	return unit.createEntityManagerFactory( conProvider );
    }

    @Provides
    protected EntityManager providesEntityManager(EntityManagerFactory factory){
    	return factory.createEntityManager();
    }
  
    @Provides
    protected Connection providesConnection(ConnectionProvider conProvider) throws SQLException {
    	try {
    		return conProvider.getConnection();
    	} catch (Exception e) {
    		Log.debug("No gears db connection conneciton: using dummy connection msg:" + e.getMessage());
    	}
    	return new DummyConnection();
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
