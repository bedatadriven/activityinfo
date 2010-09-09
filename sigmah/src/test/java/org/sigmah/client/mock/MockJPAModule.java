/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.mock;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.sigmah.client.db.UnitConnectionProvider;
import org.sigmah.client.offline.dao.AdminLocalDAO;
import org.sigmah.client.offline.dao.CountryLocalDAO;
import org.sigmah.client.offline.dao.UserDatabaseLocalDAO;
import org.sigmah.shared.dao.AdminDAO;
import org.sigmah.shared.dao.CountryDAO;
import org.sigmah.shared.dao.UserDatabaseDAO;
import org.sigmah.shared.domain.ActivityInfoOfflineUnit;

import com.bedatadriven.rebar.persistence.client.ConnectionProvider;
import com.bedatadriven.rebar.persistence.client.PersistenceUnit;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;


public class MockJPAModule extends AbstractModule {
    
    protected void configure() {
    	bind(ConnectionProvider.class).to(UnitConnectionProvider.class).in(Singleton.class);	
        configureDAOs();
    }

    protected void configureDAOs() {
    	bind(CountryDAO.class).to(CountryLocalDAO.class);
    	bind(UserDatabaseDAO.class).to(UserDatabaseLocalDAO.class);
    	bind(AdminDAO.class).to(AdminLocalDAO.class);
    	/*
    	bindDAOProxy(ActivityDAO.class);
        bindDAOProxy(AuthenticationDAO.class);
        bindDAOProxy(CountryDAO.class);
        bindDAOProxy(IndicatorDAO.class);
      //  bind(LocationDAO.class).to(LocationHibernateDAO.class);
      //  bind(ReportingPeriodDAO.class).to(ReportingPeriodHibernateDAO.class);
        bindDAOProxy(ReportDefinitionDAO.class);
        bindDAOProxy(PartnerDAO.class);
      // bind(SiteDAO.class).to(SiteHibernateDAO.class);
        bindDAOProxy(UserDatabaseDAO.class);
        bindDAOProxy(UserPermissionDAO.class);
        */
    }
    
    @Provides
    protected PersistenceUnit providePersistenceUnit(){
    	 MockPersistenceUnitFactory compiler = new MockPersistenceUnitFactory();
      	 return compiler.create(ActivityInfoOfflineUnit.class);
    }
    
    @Provides
    protected EntityManagerFactory provideEntityManagerFactory (PersistenceUnit unit, ConnectionProvider conProvider) {
    	return unit.createEntityManagerFactory(conProvider);
    }

    @Provides
    protected EntityManager providesEntityManager(EntityManagerFactory factory){
    	return factory.createEntityManager();
    }
}

