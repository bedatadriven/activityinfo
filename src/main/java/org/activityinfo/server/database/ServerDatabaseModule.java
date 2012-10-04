package org.activityinfo.server.database;


import com.bedatadriven.rebar.sql.client.query.MySqlDialect;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.google.inject.servlet.ServletModule;

public class ServerDatabaseModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(SqlDialect.class).to(MySqlDialect.class);
		//filter("/*").through(DatabaseFilter.class);
	}	
}
