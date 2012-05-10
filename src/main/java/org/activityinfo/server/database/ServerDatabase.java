package org.activityinfo.server.database;

import java.sql.Connection;

import com.bedatadriven.rebar.sql.client.query.MySqlDialect;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcDatabase;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcExecutor;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ServerDatabase extends JdbcDatabase {

	private final Provider<Connection> connectionProvider;
	
	@Inject
	public ServerDatabase(Provider<Connection> connectionProvider) {
		super(""); 
		this.connectionProvider = connectionProvider;
	}

	@Override
	protected JdbcExecutor newExecutor() {
		return new JdbcExecutor() {
			
			@Override
			protected Connection openConnection() throws Exception {
				return connectionProvider.get();
			}
		};
	}

	@Override
	public SqlDialect getDialect() {
		return MySqlDialect.INSTANCE;
	}
	
	
	
}
