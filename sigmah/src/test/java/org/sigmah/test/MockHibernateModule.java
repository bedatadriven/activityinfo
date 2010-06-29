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

package org.sigmah.test;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.sigmah.server.dao.hibernate.HibernateModule;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MockHibernateModule extends HibernateModule {
    private static EntityManagerFactory emf = null;

    @Override
    protected void configureEmf() {
        bind(EntityManagerFactory.class).toProvider(new Provider<EntityManagerFactory>() {
            @Override
            public EntityManagerFactory get() {
                // we are assuming that the tests do not affect the database schema, so there is no
                // need to restart hiberate for each test class, and we save quite a bit of time
                if (emf == null) {
                    emf = Persistence.createEntityManagerFactory(getPersistenceUnitName());
                    System.err.println("GUICE: EntityManagerFACTORY created");
                }
                return emf;
            }
        }).in(Singleton.class);
    }

    private String getPersistenceUnitName() {
        String specifiedUnitName = System.getProperty("activityinfo.pu");
        return specifiedUnitName == null ? "h2-test" : specifiedUnitName;
    }

    @Override
    protected void configureEm() {
        bind(EntityManager.class).toProvider(EntityManagerProvider.class)
                .in(TestScoped.class);
    }
}
