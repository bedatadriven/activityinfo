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

package org.activityinfo.server.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.RequestScoped;
import org.junit.AfterClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ServletTestingDataModule extends AbstractModule {

    private EntityManagerFactory emf;

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public EntityManagerFactory provideEntityManagerFactory() {

        emf = Persistence.createEntityManagerFactory("activityInfo-test");
        System.err.println("GUICE: EntityManagerFACTORY created");
        return emf;
    }

    @Provides
    @RequestScoped
    public EntityManager provideEntityManager(EntityManagerFactory emf) {
        System.err.println("GUICE: EntityManager created");
        return emf.createEntityManager();
    }


    @AfterClass
    public void afterClass() {
        System.err.println("Shutting down EMF");
        if (emf != null) {
            emf.close();
            emf = null;
        }
    }
}
