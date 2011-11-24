/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.test;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.sigmah.server.database.TestConnectionProvider;
import org.sigmah.server.database.TestDatabaseModule;
import org.sigmah.server.database.hibernate.dao.HibernateModule;

import com.google.inject.Provider;
import com.google.inject.Singleton;

public class MockHibernateModule extends HibernateModule {
    private static EntityManagerFactory emf = null;

    @Override
	protected void configure() {
		super.configure();
		
		install(new TestDatabaseModule());
	}

	@Override
    protected void configureEmf() {
        bind(EntityManagerFactory.class).toProvider(new Provider<EntityManagerFactory>() {
            @Override
            public EntityManagerFactory get() {
                // we are assuming that the tests do not affect the database schema, so there is no
                // need to restart hibernate for each test class, and we save quite a bit of time
                if (emf == null) {
                	Properties properties = new Properties();
                	properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
                	properties.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
                	properties.setProperty("hibernate.connection.url",  TestConnectionProvider.getUrl());
                	properties.setProperty("hibernate.connection.username", TestConnectionProvider.getUsername());
                	properties.setProperty("hibernate.connection.password", TestConnectionProvider.getPassword());
                	//properties.setProperty("hibernate.hbm2ddl.auto", "update");
                                	
                	emf = Persistence.createEntityManagerFactory("activityInfo", properties);
                	
                    System.err.println("GUICE: EntityManagerFACTORY created");
                }
                return emf;
            }
        }).in(Singleton.class);
    }

    @Override
    protected void configureEm() {
        bind(EntityManager.class).toProvider(EntityManagerProvider.class)
                .in(TestScoped.class);
    }
    
}
