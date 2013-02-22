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

public class SqliteInsertBuilder {

    private StringBuilder sql;
    private String tableName;
    private StringBuilder insert;
    private ColumnAppender appenders[];
    private int numColumns;
    private ResultSet rs;
    private SqlQuery query;
    private SqliteBatchBuilder batch;

    public SqliteInsertBuilder(SqliteBatchBuilder batch) {
        this.batch = batch;
    }

    public SqliteInsertBuilder into(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SqliteInsertBuilder from(SqlQuery query) {
        this.query = query;
        return this;
    }

    public void execute(EntityManager entityManager) {
        ((HibernateEntityManager) entityManager).getSession().doWork(
            new Work() {

                @Override
                public void execute(Connection connection) throws SQLException {
                    rs = SqlQueryUtil.query(connection, query);
                    numColumns = rs.getMetaData().getColumnCount();
                    setupAppenders();
                    composeInsertStatement();

                    try {
                        appendRows();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
    }

    private void composeInsertStatement() throws SQLException {
        insert = new StringBuilder();
        insert.append("INSERT OR REPLACE INTO ").append(tableName).append(" (");
        for (int i = 0; i != numColumns; ++i) {
            if (i > 0) {
                insert.append(",");
            }
            insert.append(rs.getMetaData().getColumnName(i + 1));
        }
        insert.append(") ");
    }

    private void setupAppenders()
        throws SQLException {
        this.appenders = new ColumnAppender[numColumns];
        for (int i = 0; i != numColumns; ++i) {
            appenders[i] = ColumnAppender.forType(rs.getMetaData()
                .getColumnType(i + 1));
        }
    }

    public void appendRows() throws SQLException, IOException {
        sql = new StringBuilder();
        int rowCount = 0;
        while (rs.next()) {
            if (rowCount == 0) {
                sql.append(insert);
            } else {
                sql.append(" UNION ");
            }
            sql.append("SELECT ");
            for (int i = 0; i != numColumns; ++i) {
                if (i > 0) {
                    sql.append(',');
                }
                appenders[i].append(sql, rs, i + 1);
            }
            rowCount++;
            if (rowCount > 450) {
                batch.addStatement(sql.toString());
                sql.setLength(0);
                rowCount = 0;
            }
        }
        if (rowCount > 0) {
            batch.addStatement(sql.toString());
        }
    }
}
