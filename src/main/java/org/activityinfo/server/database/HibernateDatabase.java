package org.activityinfo.server.database;

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

import javax.inject.Singleton;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.bedatadriven.rebar.sql.client.query.MySqlDialect;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Provider;

@Singleton
public class HibernateDatabase extends SqlDatabase {

    private final Provider<HibernateEntityManager> entityManager;

    public HibernateDatabase(Provider<HibernateEntityManager> entityManager) {
        super();
        this.entityManager = entityManager;
    }

    @Override
    public void transaction(SqlTransactionCallback callback) {
        entityManager.get().getSession().doWork(new Work() {

            @Override
            public void execute(Connection connection) throws SQLException {

            }
        });
    }

    @Override
    public SqlDialect getDialect() {
        return new MySqlDialect();
    }

    @Override
    public void executeUpdates(String bulkOperationJsonArray,
        AsyncCallback<Integer> callback) {
        callback.onFailure(new UnsupportedOperationException());
    }

    @Override
    public String getName() {
        return "Hibernate";
    }
}
