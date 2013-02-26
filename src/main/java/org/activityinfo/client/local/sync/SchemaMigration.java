package org.activityinfo.client.local.sync;

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

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class SchemaMigration {

    private final SqlDatabase database;

    // This should be generated automatically from the liquibase change set
    // logs.
    // This will do until then:

    public static final String[] MIGRATION_DDL = new String[] {
        "ALTER TABLE Site ADD COLUMN timeEdited REAL",
        "ALTER TABLE Location ADD COLUMN timeEdited REAL",
        "CREATE TABLE IF NOT EXISTS  indicatorlink (SourceIndicatorId int, DestinationIndicatorId int)",
        "CREATE TABLE IF NOT EXISTS  siteattachment (blobid TEXT, siteid INT, filename TEXT, uploadedBy INT, blobSize REAL, contentType TEXT)",
        "ALTER TABLE UserDatabase ADD COLUMN version REAL",
        "ALTER TABLE UserPermission ADD COLUMN version REAL",
        "ALTER TABLE AdminLevel ADD COLUMN polygons INT",
        "ALTER TABLE UserLogin ADD COLUMN emailnotification INT",
        "CREATE INDEX IF NOT EXISTS adminentity_pk on adminentity (adminentityid)",
        "CREATE INDEX IF NOT EXISTS location_link on locationadminlink (locationid)",
        "CREATE INDEX IF NOT EXISTS location_entity on locationadminlink (adminentityid)"
    };

    @Inject
    public SchemaMigration(SqlDatabase database) {
        super();
        this.database = database;
    }

    public void migrate(final AsyncCallback<Void> callback) {
        database.transaction(new SqlTransactionCallback() {

            @Override
            public void begin(SqlTransaction tx) {
                for (String ddl : MIGRATION_DDL) {
                    tx.executeSql(ddl, new SqlResultCallback() {

                        @Override
                        public void onSuccess(SqlTransaction tx,
                            SqlResultSet results) {
                        }

                        @Override
                        public boolean onFailure(SqlException e) {
                            // ignore errors resulting from ddl that has already
                            // been appsavelied
                            return SqlResultCallback.CONTINUE;
                        }

                    });
                }
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(null);
            }

            @Override
            public void onError(SqlException e) {
                callback.onFailure(e);
            }
        });

    }

}
