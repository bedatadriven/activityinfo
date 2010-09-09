/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.RequestScoped;
import org.sigmah.server.dao.*;
import org.sigmah.server.endpoint.gwtrpc.handler.GetSitesHandlerHibernate;
import org.sigmah.server.util.DozerMapper;
import org.sigmah.shared.command.handler.GetSitesHandler;
import org.sigmah.shared.dao.ActivityDAO;
import org.sigmah.shared.dao.AdminDAO;
import org.sigmah.shared.dao.CountryDAO;
import org.sigmah.shared.dao.DAO;
import org.sigmah.shared.dao.IndicatorDAO;
import org.sigmah.shared.dao.UserDAO;
import org.sigmah.shared.dao.UserDatabaseDAO;
import org.sigmah.shared.dao.UserPermissionDAO;
import org.sigmah.shared.dto.DTOMapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

/**
 * Guice module that provides Hibernate-based implementations for the DAO-layer interfaces.
 *
 * @author Alex Bertram
 */
public class HibernateModule extends AbstractModule {

    @Override
    protected void configure() {
        configureEmf();
        configureEm();
        configureDAOs();
        configureTransactions();
        configureEndPoints();
    }

    
    protected void configureEndPoints(){
    	//bind(GetSitesHandler.class).to(GetSitesHandlerHibernate.class);
    }
    
    protected void configureEmf() {
        bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryProvider.class).in(Singleton.class);
    }

    protected void configureEm() {
        bind(EntityManager.class).toProvider(EntityManagerProvider.class).in(RequestScoped.class);
    }

    protected void configureTransactions() {
        TransactionalInterceptor interceptor = new TransactionalInterceptor();
        requestInjection(interceptor);

        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class),
                interceptor);
    }

    protected void configureDAOs() {
    	bind(AdminDAO.class).to(AdminHibernateDAO.class);
        bindDAOProxy(ActivityDAO.class);
        bindDAOProxy(AuthenticationDAO.class);
        bindDAOProxy(CountryDAO.class);
        bindDAOProxy(IndicatorDAO.class);
        bind(LocationDAO.class).to(LocationHibernateDAO.class);
        bind(ReportingPeriodDAO.class).to(ReportingPeriodHibernateDAO.class);
        bindDAOProxy(ReportDefinitionDAO.class);
        bindDAOProxy(PartnerDAO.class);
        bind(SiteDAO.class).to(SiteHibernateDAO.class);
        bindDAOProxy(UserDatabaseDAO.class);
        bindDAOProxy(UserPermissionDAO.class);
        bind(UserDAO.class).to(UserDAOImpl.class);
    }

    private <T extends DAO> void bindDAOProxy(Class<T> daoClass) {
        HibernateDAOProvider<T> provider = new HibernateDAOProvider<T>(daoClass);
        requestInjection(provider);

        bind(daoClass).toProvider(provider);
    }


    protected static class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {
        private Properties configProperties;

        @Inject
        public EntityManagerFactoryProvider(Properties configProperties) {
            this.configProperties = configProperties;
        }

        @Override
        public EntityManagerFactory get() {
            return Persistence.createEntityManagerFactory("activityInfo", configProperties);
        }
    }

    protected static class EntityManagerProvider implements Provider<EntityManager> {
        private EntityManagerFactory emf;

        @Inject
        public EntityManagerProvider(EntityManagerFactory emf) {
            this.emf = emf;
        }

        @Override
        public EntityManager get() {
            return emf.createEntityManager();
        }
    }

}
