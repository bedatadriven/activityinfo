
package org.activityinfo.client.local;

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


import org.activityinfo.client.Log;
import org.activityinfo.client.local.command.HandlerRegistry;
import org.activityinfo.client.local.sync.Synchronizer;
import org.activityinfo.client.local.sync.SynchronizerImpl;
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


public class LocalModule extends AbstractGinModule {

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
