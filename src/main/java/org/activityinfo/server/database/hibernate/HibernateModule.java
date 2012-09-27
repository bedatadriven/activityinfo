/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.database.hibernate;

import java.sql.Connection;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.activityinfo.server.database.hibernate.dao.HibernateDAOModule;
import org.activityinfo.server.database.hibernate.dao.TransactionModule;
import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.ejb.HibernatePersistence;

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
        private org.activityinfo.server.util.config.DeploymentConfiguration configProperties;

        @Inject
        public EntityManagerFactoryProvider(DeploymentConfiguration configProperties, 
        		Provider<Connection> connectionProvider) {
            this.configProperties = configProperties;
            
            // can't figure out how to provide an instance of the ConnectionProvider
            // directly to hibernate so we have to do it this way.
            HibernateConnectionProvider.DELEGATE = connectionProvider;
        }

        @Override
        public EntityManagerFactory get() {
        	// ensure that hibernate does NOT do schema updating--liquibase is in charge
        	Properties config = configProperties.asProperties();
        	config.setProperty(Environment.HBM2DDL_AUTO, "");
        	config.setProperty(Environment.CONNECTION_PROVIDER, HibernateConnectionProvider.class.getName());
        	config.setProperty("hibernate.ejb.naming_strategy", AINamingStrategy.class.getName());
        	return Persistence.createEntityManagerFactory("activityInfo", config);
        }
    }

    @Provides
    protected HibernateEntityManager provideHibernateEntityManager(EntityManager entityManager) {
        return (HibernateEntityManager)entityManager;
    }
   
}
