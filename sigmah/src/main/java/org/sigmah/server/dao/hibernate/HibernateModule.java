/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.ejb.HibernateEntityManager;
import org.sigmah.server.dao.AuthenticationDAO;
import org.sigmah.server.dao.LocationDAO;
import org.sigmah.server.dao.PartnerDAO;
import org.sigmah.server.dao.ReportDefinitionDAO;
import org.sigmah.server.dao.ReportingPeriodDAO;
import org.sigmah.server.dao.SiteDAO;
import org.sigmah.server.dao.Transactional;
import org.sigmah.shared.dao.ActivityDAO;
import org.sigmah.shared.dao.AdminDAO;
import org.sigmah.shared.dao.CountryDAO;
import org.sigmah.shared.dao.DAO;
import org.sigmah.shared.dao.IndicatorDAO;
import org.sigmah.shared.dao.ProjectDAO;
import org.sigmah.shared.dao.SiteTableDAO;
import org.sigmah.shared.dao.SqlDialect;
import org.sigmah.shared.dao.UserDAO;
import org.sigmah.shared.dao.UserDatabaseDAO;
import org.sigmah.shared.dao.UserPermissionDAO;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.RequestScoped;

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
        configureDialects();
        configureDAOs();
        configureTransactions();
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

    private void configureDialects() {
        bind(SqlDialect.class).toProvider(SQLDialectProvider.class).in(Singleton.class);
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
        bind(SiteTableDAO.class).to(HibernateSiteTableDAO.class);
        bind(SiteDAO.class).to(SiteHibernateDAO.class);
        bind(ProjectDAO.class).to(ProjectHibernateDAO.class);
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

    @Provides
    protected HibernateEntityManager provideHibernateEntityManager(EntityManager entityManager) {
        return (HibernateEntityManager)entityManager;
    }

}
