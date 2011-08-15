package org.sigmah.server.database;

import java.sql.Connection;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.google.inject.AbstractModule;

public class ServerDatabaseModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Connection.class).toProvider(BoneCpConnectionProvider.class);
		bind(SqlDatabase.class).to(ServerDatabase.class);
	}
}
