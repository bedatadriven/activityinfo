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

import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.activityinfo.server.dao.hibernate.DataModule;
import org.activityinfo.test.TestScoped;
import org.junit.AfterClass;
import org.junit.Ignore;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Ignore("not a test")
public class TestingDataModule extends DataModule {
    private EntityManagerFactory emf;

    @Provides
    @Singleton
    public EntityManagerFactory provideEntityManagerFactory() {
//        if(emf == null) {
//            // to save time, do this only once per test run --  no matter
//            // how many test classes we run. The tests are not going to
//            // change the schema (if they do we have a more serious problem)
//            // so we don't effect the independence of tests.
//
//            // however, to deal with the possibility of a schema (design) change between
//            // tests, we want to drop and recreate the database each time
//
//            try {
//                Class.forName("net.sourceforge.jtds.jdbc.Driver");
//                Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://localhost:1433/", "sa", "adminpwd");
//                Statement stmt = conn.createStatement();
//
//                try {
//                    stmt.execute("drop database [act-test]");
//                } catch (SQLException ignore) {
//                    ignore.printStackTrace();
//                }
//                stmt.execute("create database [act-test]");
//            } catch (Exception e) {
//                e.printStackTrace();        
//                throw new Error("Error while dropping/creating database");
//            }

        emf = Persistence.createEntityManagerFactory("activityInfo-test");
        System.err.println("GUICE: EntityManagerFACTORY created");
//        }
        return emf;
    }

    @Provides
    @TestScoped
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
