/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.sigmah.server.dao.hibernate;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.RequestScoped;
import org.sigmah.server.dao.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
    }

    protected void configureEm() {
        bind(EntityManager.class).toProvider(EntityManagerProvider.class).in(RequestScoped.class);
    }

    protected void configureEmf() {
        bind(EntityManagerFactory.class).toProvider(new Provider<EntityManagerFactory>() {
            @Override
            public EntityManagerFactory get() {
                return Persistence.createEntityManagerFactory("activityInfo");
            }
        }).in(Singleton.class);
    }

    protected void configureTransactions() {
        TransactionalInterceptor interceptor = new TransactionalInterceptor();
        requestInjection(interceptor);

        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class),
                interceptor);
    }

    protected void configureDAOs() {
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
    }

    private <T extends DAO> void bindDAOProxy(Class<T> daoClass) {
        HibernateDAOProvider<T> provider = new HibernateDAOProvider<T>(daoClass);
        requestInjection(provider);

        bind(daoClass).toProvider(provider);
    }

    protected static class EntityManagerProvider implements Provider<EntityManager> {
        private final EntityManagerFactory emf;

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
