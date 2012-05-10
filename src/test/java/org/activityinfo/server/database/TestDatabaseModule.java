package org.activityinfo.server.database;

import java.sql.Connection;

import org.activityinfo.server.database.ServerDatabase;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.query.MySqlDialect;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.google.inject.AbstractModule;

public class TestDatabaseModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Connection.class).toProvider(TestConnectionProvider.class);
		bind(SqlDatabase.class).to(ServerDatabase.class);
		bind(SqlDialect.class).to(MySqlDialect.class);
	}

}
