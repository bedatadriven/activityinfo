/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.offline;

import org.activityinfo.client.offline.command.HandlerRegistry;
import org.activityinfo.client.offline.sync.Synchronizer;
import org.activityinfo.client.offline.sync.SynchronizerImpl;
import org.activityinfo.login.shared.AuthenticatedUser;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlDatabaseFactory;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.bedatadriven.rebar.sql.client.query.SqliteDialect;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
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
    	SqlDatabaseFactory factory = GWT.create(SqlDatabaseFactory.class);
    	return factory.open("user" + auth.getUserId());
    }
}
