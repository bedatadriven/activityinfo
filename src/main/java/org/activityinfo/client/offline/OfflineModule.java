/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.offline;

import org.activityinfo.client.Log;
import org.activityinfo.client.offline.command.HandlerRegistry;
import org.activityinfo.client.offline.sync.Synchronizer;
import org.activityinfo.client.offline.sync.SynchronizerImpl;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlDatabaseFactory;
import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.bedatadriven.rebar.sql.client.query.SqliteDialect;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Provides;
import com.google.inject.Singleton;


public class OfflineModule extends AbstractGinModule {

    @Override
    protected void configure() {

        bind(Synchronizer.class).to(SynchronizerImpl.class);
        bind(HandlerRegistry.class).toProvider(HandlerRegistryProvider.class);
        bind(SqlDialect.class).to(SqliteDialect.class);
    }
    
    @Provides
    @Singleton
    protected SqlDatabase provideSqlDatabase(AuthenticatedUser auth) {
    	try {
	    	SqlDatabaseFactory factory = GWT.create(SqlDatabaseFactory.class);
	    	return factory.open("user" + auth.getUserId());
    	} catch(Exception e) {
    		// ensure that an exception does not derail the whole app startup
    		// we SHOULD not be injecting the database into eagerly created 
    		// singletons but it can happen...
    		Log.error("Error opening database", e);
    		return new NullDatabase();
    	}
    }
    
    private static class NullDatabase extends SqlDatabase {

		@Override
		public void transaction(SqlTransactionCallback callback) {
			callback.onError(new SqlException("Database could not be opened"));
		}

		@Override
		public SqlDialect getDialect() {
			return new SqliteDialect();
		}

		@Override
		public void executeUpdates(String bulkOperationJsonArray,
				AsyncCallback<Integer> callback) {
			callback.onFailure(new SqlException("Database could not be opened."));
		}

		@Override
		public String getName() {
			return "nulldb";
		}
    }
}
