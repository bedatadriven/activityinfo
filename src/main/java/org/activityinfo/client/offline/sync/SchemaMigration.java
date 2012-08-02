package org.activityinfo.client.offline.sync;

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
	
	// This should be generated automatically from the liquibase change set logs.
	// This will do until then:
	
	public static final String[] MIGRATION_DDL = new String[] {
		"ALTER TABLE Site ADD COLUMN timeEdited REAL",
		"ALTER TABLE Location ADD COLUMN timeEdited REAL",
		"CREATE TABLE IF NOT EXISTS  indicatorlink (SourceIndicatorId int, DestinationIndicatorId int)",
		"CREATE TABLE IF NOT EXISTS  siteattachment (blobid TEXT, siteid INT, filename TEXT, uploadedBy INT, blobSize REAL, contentType TEXT)",
		"ALTER TABLE UserDatabase ADD COLUMN timeEdited REAL",
		"ALTER TABLE UserPermission ADD COLUMN timeEdited REAL"
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
				for(String ddl : MIGRATION_DDL) {
					tx.executeSql(ddl, new SqlResultCallback() {

						@Override
						public void onSuccess(SqlTransaction tx,
								SqlResultSet results) { 	}

						@Override
						public boolean onFailure(SqlException e) {
							// ignore errors resulting from ddl that has already been appsavelied
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
