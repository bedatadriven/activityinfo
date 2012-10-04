/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.database.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.activityinfo.server.database.hibernate.dao.HibernateDAOModule;
import org.activityinfo.server.database.hibernate.dao.TransactionModule;
import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.HibernateEntityManager;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
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
        public EntityManagerFactoryProvider(DeploymentConfiguration configProperties) {
            this.configProperties = configProperties;
        }

        @Override
        public EntityManagerFactory get() {
        	// ensure that hibernate does NOT do schema updating--liquibase is in charge
        	Ejb3Configuration config = new Ejb3Configuration();
        	config.addProperties(configProperties.asProperties());
        	for(Class clazz : getPersistentClasses()) {
        		config.addAnnotatedClass(clazz);
        	}
        	config.setProperty(Environment.HBM2DDL_AUTO, "");
        	config.setNamingStrategy(new AINamingStrategy());
        	return config.buildEntityManagerFactory();
        }
    }

    public static List<Class> getPersistentClasses() {
    	try {
        	List<Class> list = Lists.newArrayList();
        	List<String> lines = Resources.readLines(
        			HibernateModule.class.getResource("/persistent.classes"), Charsets.UTF_8);
        	for(String line : lines) {
        		list.add(Class.forName(line));
        	}
        	return list;
    	} catch(Exception e) {
    		throw new RuntimeException("Exception loading list of persistent classes", e);
    	}
    }
    
    @Provides
    protected HibernateEntityManager provideHibernateEntityManager(EntityManager entityManager) {
        return (HibernateEntityManager)entityManager;
    }
   
}
