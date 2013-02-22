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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;
import org.hibernate.jdbc.util.FormatStyle;

import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcExecutor;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class HibernateExecutor extends JdbcExecutor {

    private final HibernateEntityManager entityManager;

    private final static Logger LOGGER = Logger
        .getLogger(HibernateExecutor.class.getName());

    @Inject
    public HibernateExecutor(EntityManager em) {
        this.entityManager = (HibernateEntityManager) em;
    }

    public HibernateExecutor(HibernateEntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }

    @Override
    public SqlResultSet execute(final String statement, final Object[] params)
        throws Exception {
        final List<SqlResultSet> result = Lists.newArrayList();
        entityManager.getSession().doWork(new Work() {

            @Override
            public void execute(Connection connection) throws SQLException {
                try {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.finest("Starting query: " + format(statement));
                    }

                    long startTime = System.currentTimeMillis();
                    result.add(doExecute(connection, statement, params));
                    long elapsed = System.currentTimeMillis() - startTime;

                    LOGGER.finest("Query complete");

                    if (elapsed > 100) {
                        String formatted = format(statement);
                        LOGGER.warning("Slow query completed in " + elapsed
                            + "ms:\n" + formatted);
                    }
                } catch (Throwable e) {
                    LOGGER
                        .log(Level.SEVERE,
                            "Exception occured while executing query: "
                                + statement, e);
                    throw new SQLException(e);
                }
            }
        });
        if (result.size() != 1) {
            throw new AssertionError();
        }
        return result.get(0);
    }

    @Override
    public boolean begin() throws Exception {
        return true;
    }

    @Override
    public void commit() throws Exception {
    }

    @Override
    public void rollback() throws Exception {

    }

    private String format(final String statement) {
        return FormatStyle.BASIC.getFormatter().format(statement);
    }
}
