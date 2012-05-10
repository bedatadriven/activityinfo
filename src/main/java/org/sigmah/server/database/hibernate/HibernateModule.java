/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.database.hibernate;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.ejb.HibernateEntityManager;
import org.sigmah.server.database.hibernate.dao.ActivityDAO;
import org.sigmah.server.database.hibernate.dao.AdminDAO;
import org.sigmah.server.database.hibernate.dao.AdminHibernateDAO;
import org.sigmah.server.database.hibernate.dao.AuthenticationDAO;
import org.sigmah.server.database.hibernate.dao.CountryDAO;
import org.sigmah.server.database.hibernate.dao.DAO;
import org.sigmah.server.database.hibernate.dao.HibernateDAOModule;
import org.sigmah.server.database.hibernate.dao.HibernateDAOProvider;
import org.sigmah.server.database.hibernate.dao.IndicatorDAO;
import org.sigmah.server.database.hibernate.dao.PartnerDAO;
import org.sigmah.server.database.hibernate.dao.ReportDefinitionDAO;
import org.sigmah.server.database.hibernate.dao.TransactionModule;
import org.sigmah.server.database.hibernate.dao.UserDAO;
import org.sigmah.server.database.hibernate.dao.UserDAOImpl;
import org.sigmah.server.database.hibernate.dao.UserDatabaseDAO;
import org.sigmah.server.database.hibernate.dao.UserPermissionDAO;
import org.sigmah.server.util.config.DeploymentConfiguration;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

/**
 * Guice module that provides Hibernate-based implementations for the DAO-layer interfaces.
 *
 * @author Alex Bertram
 */
public class HibernateModule extends ServletModule {


    @Override
	protected void configureServlets() {

    	HibernateSessionScope sessionScope = new HibernateSessionScope();
    	bindScope(HibernateSessionScoped.class, sessionScope);
    	
    	bind(HibernateSessionScope.class).toInstance(sessionScope);
    	
    	filter("/*").through(HibernateSessionFilter.class);

    	
        configureEmf();
        configureEm();
        install(new HibernateDAOModule());
        install(new TransactionModule());
	}

	protected void configureEmf() {
        bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryProvider.class).in(Singleton.class);
    }

    protected void configureEm() {
        bind(EntityManager.class).toProvider(EntityManagerProvider.class).in(HibernateSessionScoped.class);
    }
   

    protected static class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {
        private org.sigmah.server.util.config.DeploymentConfiguration configProperties;

        @Inject
        public EntityManagerFactoryProvider(DeploymentConfiguration configProperties) {
            this.configProperties = configProperties;
        }

        @Override
        public EntityManagerFactory get() {
        	// ensure that hibernate does do schema updating--liquibase is in charge
        	Properties config = configProperties.asProperties();
        	config.setProperty("hibernate.hbm2ddl.auto", "");
        	
            return Persistence.createEntityManagerFactory("activityInfo", config);
        }
    }

    @Provides
    protected HibernateEntityManager provideHibernateEntityManager(EntityManager entityManager) {
        return (HibernateEntityManager)entityManager;
    }
   
}
