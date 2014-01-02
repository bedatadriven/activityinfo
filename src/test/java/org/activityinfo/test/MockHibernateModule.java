package org.activityinfo.test;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.activityinfo.server.database.TestConnectionProvider;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.server.database.hibernate.AINamingStrategy;
import org.activityinfo.server.database.hibernate.EntityManagerProvider;
import org.activityinfo.server.database.hibernate.HibernateModule;
import org.activityinfo.server.database.hibernate.dao.HibernateDAOModule;
import org.activityinfo.server.database.hibernate.dao.TransactionModule;
import org.hibernate.Session;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.validator.HibernateValidator;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
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

    @Provides
    public Session provideHibernateSession(EntityManager em) {
        HibernateEntityManager hem = (HibernateEntityManager) em;
        return hem.getSession();
    }
    
    @Provides @Singleton
    public Validator provideValidator() {
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
            .configure()
            .buildValidatorFactory();
        return validatorFactory.getValidator();
    }

    protected void configureEmf() {
        bind(EntityManagerFactory.class).toProvider(
            new Provider<EntityManagerFactory>() {
                @Override
                public EntityManagerFactory get() {
                    // we are assuming that the tests do not affect the database
                    // schema, so there is no
                    // need to restart hibernate for each test class, and we
                    // save quite a bit of time
                    if (emf == null) {
                        Ejb3Configuration config = new Ejb3Configuration();
                        config.setProperty("hibernate.dialect",
                            "org.hibernate.spatial.dialect.mysql.MySQLSpatialInnoDBDialect");
                        config.setProperty("hibernate.connection.driver_class",
                            "com.mysql.jdbc.Driver");
                        config.setProperty("hibernate.connection.url",
                            TestConnectionProvider.URL);
                        config.setProperty("hibernate.connection.username",
                            TestConnectionProvider.USERNAME);
                        config.setProperty("hibernate.connection.password",
                            TestConnectionProvider.PASSWORD);
                        config.setProperty("hibernate.hbm2ddl.auto", "none");
                        config.setProperty("hibernate.show_sql", "true");
                        config.setProperty("hibernate.connection.pool_size", "0");
                        config.setNamingStrategy(new AINamingStrategy());
                        for (Class clazz : HibernateModule
                            .getPersistentClasses()) {
                            config.addAnnotatedClass(clazz);
                        }

                        emf = config.buildEntityManagerFactory();

                        System.err
                            .println("GUICE: EntityManagerFACTORY created");
                    }
                    return emf;
                }
            }).in(Singleton.class);
    }

}
