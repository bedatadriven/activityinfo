package org.activityinfo.server.database;

import java.sql.Connection;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.query.MySqlDialect;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.google.inject.servlet.ServletModule;

public class ServerDatabaseModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(Connection.class).toProvider(BoneCpConnectionPool.class);
		bind(SqlDatabase.class).to(ServerDatabase.class);
		bind(SqlDialect.class).to(MySqlDialect.class);
		filter("/*").through(DatabaseFilter.class);
	}	
}
