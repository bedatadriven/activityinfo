/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.activityinfo.server.database.TestConnectionProvider;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.server.database.hibernate.AINamingStrategy;
import org.activityinfo.server.database.hibernate.EntityManagerProvider;
import org.activityinfo.server.database.hibernate.HibernateModule;
import org.activityinfo.server.database.hibernate.dao.HibernateDAOModule;
import org.activityinfo.server.database.hibernate.dao.TransactionModule;
import org.hibernate.ejb.Ejb3Configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;

public class MockHibernateModule extends AbstractModule {
    private static EntityManagerFactory emf = null;

	@Override
	protected void configure() {
		
		configureEmf();
		
		 bind(EntityManager.class).toProvider(EntityManagerProvider.class)
         .in(TestScoped.class);
		
		install(new TestDatabaseModule());
        install(new HibernateDAOModule());
        install(new TransactionModule());
	}


    protected void configureEmf() {
        bind(EntityManagerFactory.class).toProvider(new Provider<EntityManagerFactory>() {
            @Override
            public EntityManagerFactory get() {
                // we are assuming that the tests do not affect the database schema, so there is no
                // need to restart hibernate for each test class, and we save quite a bit of time
                if (emf == null) {
                	Ejb3Configuration config = new Ejb3Configuration();
                	config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
                	config.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
                	config.setProperty("hibernate.connection.url",  TestConnectionProvider.URL);
                	config.setProperty("hibernate.connection.username", TestConnectionProvider.USERNAME);
                	config.setProperty("hibernate.connection.password", TestConnectionProvider.PASSWORD);
                	config.setProperty("hibernate.hbm2ddl.auto", "none");
                	config.setProperty("hibernate.show_sql", "true");
                	config.setNamingStrategy(new AINamingStrategy());
                	for(Class clazz : HibernateModule.getPersistentClasses()) {
                		config.addAnnotatedClass(clazz);
                	}
                	
                	emf = config.buildEntityManagerFactory();
                	
                    System.err.println("GUICE: EntityManagerFACTORY created");
                }
                return emf;
            }
        }).in(Singleton.class);
    }

    
}
