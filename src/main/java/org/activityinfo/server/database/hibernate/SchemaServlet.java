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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class SchemaServlet extends HttpServlet {

    public static final String ENDPOINT = "/tasks/migrateSchema";

    private static final Logger LOGGER = Logger.getLogger(SchemaServlet.class
        .getName());

    private final Provider<EntityManager> entityManager;

    @Inject
    public SchemaServlet(Provider<EntityManager> entityManager) {
        super();
        this.entityManager = entityManager;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        performMigration((HibernateEntityManager) this.entityManager.get());

    }

    public static void performMigration(HibernateEntityManager entityManager) {
        entityManager.getSession().doWork(new Work() {

            @Override
            public void execute(Connection connection) throws SQLException {

                Liquibase liquibase;
                try {
                    liquibase = new Liquibase(
                        "org/activityinfo/database/changelog/db.changelog-master.xml",
                        new ClassLoaderResourceAccessor(),
                        new CloudSqlConnection(connection));
                    liquibase.update(null);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE,
                        "Exception whilst migrating schema", e);
                }
            }
        });
    }

    private static class CloudSqlConnection extends JdbcConnection {

        public CloudSqlConnection(Connection connection) {
            super(connection);
        }

        @Override
        public String getDatabaseProductName() throws DatabaseException {
            return "MySQL";
        }

    }

}
