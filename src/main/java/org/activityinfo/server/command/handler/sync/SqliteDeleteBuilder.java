package org.activityinfo.server.command.handler.sync;

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
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;

public class SqliteDeleteBuilder {

    private final SqliteBatchBuilder batch;
    private String tableName;
    private String idName;
    private SqlQuery query;
    protected ResultSet rs;

    public SqliteDeleteBuilder(SqliteBatchBuilder batch) {
        super();
        this.batch = batch;
    }

    public SqliteDeleteBuilder from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SqliteDeleteBuilder where(String id) {
        this.idName = id;
        return this;
    }

    public SqliteDeleteBuilder in(SqlQuery query) {
        this.query = query;
        return this;
    }

    public void execute(EntityManager entityManager) {
        ((HibernateEntityManager) entityManager).getSession().doWork(
            new Work() {

                @Override
                public void execute(Connection connection) throws SQLException {
                    rs = SqlQueryUtil.query(connection, query);
                    StringBuilder sql = new StringBuilder();
                    sql.append("DELETE FROM ")
                        .append(tableName)
                        .append(" WHERE ")
                        .append(idName)
                        .append(" IN (");

                    boolean needsComma = false;
                    while (rs.next()) {
                        if (needsComma) {
                            sql.append(',');
                        }
                        sql.append(rs.getInt(1));
                        needsComma = true;
                    }
                    sql.append(")");
                    try {
                        batch.addStatement(sql.toString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
    }

}
