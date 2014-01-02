package org.activityinfo.server.database.hibernate;

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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.activityinfo.server.database.hibernate.dao.FixGeometryTask;
import org.activityinfo.server.database.hibernate.dao.HibernateDAOModule;
import org.activityinfo.server.database.hibernate.dao.TransactionModule;
import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.validator.HibernateValidator;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * Guice module that provides Hibernate-based implementations for the DAO-layer
 * interfaces.
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
        serve(SchemaServlet.ENDPOINT).with(SchemaServlet.class);
        
        configureEmf();
        configureEm();
        install(new HibernateDAOModule());
        install(new TransactionModule());
        
        // temporary fix for geometry types
        bind(FixGeometryTask.class);
        filter("/tasks/fixGeometry").through(GuiceContainer.class);

    }

    protected void configureEmf() {
        bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryProvider.class).in(Singleton.class);
    }

    protected void configureEm() {
        bind(EntityManager.class).toProvider(EntityManagerProvider.class).in(HibernateSessionScoped.class);
    }

    @Provides
    public Session provideSession(EntityManager em) {
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

    protected static class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {
        private org.activityinfo.server.util.config.DeploymentConfiguration deploymentConfig;

        @Inject
        public EntityManagerFactoryProvider(DeploymentConfiguration deploymentConfig) {
            this.deploymentConfig = deploymentConfig;
        }

        @Override
        public EntityManagerFactory get() {
            Ejb3Configuration config = new Ejb3Configuration();
            config.addProperties(deploymentConfig.asProperties());
            for (Class clazz : getPersistentClasses()) {
                config.addAnnotatedClass(clazz);
            }
            // ensure that hibernate does NOT do schema updating--liquibase is
            // in charge
            config.setProperty(Environment.HBM2DDL_AUTO, "");
            config.setNamingStrategy(new AINamingStrategy());
            EntityManagerFactory emf = config.buildEntityManagerFactory();

            return emf;
        }
    }

    @Provides
    public static SessionFactory getSessionFactory(EntityManagerFactory emf) {
        HibernateEntityManagerFactory hemf = (HibernateEntityManagerFactory) emf;
        return hemf.getSessionFactory();
    }


    public static List<Class> getPersistentClasses() {
        try {
            List<Class> list = Lists.newArrayList();
            List<String> lines =
                Resources.readLines(HibernateModule.class.getResource("/persistent.classes"), Charsets.UTF_8);
            for (String line : lines) {
                list.add(Class.forName(line));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Exception loading list of persistent classes", e);
        }
    }

    @Provides
    protected HibernateEntityManager provideHibernateEntityManager(EntityManager entityManager) {
        return (HibernateEntityManager) entityManager;
    }
}
