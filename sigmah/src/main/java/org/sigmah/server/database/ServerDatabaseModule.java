package org.sigmah.server.database;

import java.sql.Connection;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.google.inject.servlet.ServletModule;

public class ServerDatabaseModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(Connection.class).toProvider(BoneCpConnectionProvider.class);
		bind(SqlDatabase.class).to(ServerDatabase.class);
		filter("/*").through(DatabaseInitFilter.class);
	}	
}
