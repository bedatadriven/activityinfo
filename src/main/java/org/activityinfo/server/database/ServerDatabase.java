package org.activityinfo.server.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.bedatadriven.rebar.sql.client.query.MySqlDialect;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcDatabase;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcExecutor;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mysql.jdbc.log.LogUtils;

public class ServerDatabase extends JdbcDatabase {

	private static Logger LOGGER = Logger.getLogger(ServerDatabase.class); 

	private final Provider<Connection> connectionProvider;
	private static ThreadLocal<List<Executor>> EXECUTORS = new ThreadLocal<List<Executor>>();
	
	@Inject
	public ServerDatabase(Provider<Connection> connectionProvider) {
		super(""); 
		this.connectionProvider = connectionProvider;
	}

	@Override
	protected JdbcExecutor newExecutor() {
		return new Executor();
	}

	@Override
	public SqlDialect getDialect() {
		return MySqlDialect.INSTANCE;
	}
	
	
	private final class Executor extends JdbcExecutor {
		private Connection connection = null;
		
		@Override
		protected Connection openConnection() throws Exception {
			if(connection != null) {
				throw new AssertionError("openConnection has been called twice!");
			}
			connection = connectionProvider.get();
			if(EXECUTORS.get() == null) {
				EXECUTORS.set(Lists.<Executor>newArrayList());
			}
			EXECUTORS.get().add(this);
			return connection;
		}
		
		private void ensureClosed() {
			if(connection != null) {
				try {
					if(!connection.isClosed()) {
						LOGGER.error("Connection was left open after request! Closing now...");
						try {
							connection.close();
						} catch(Exception e) {
							LOGGER.error("Exception thrown while closing connection", e);
						}
					}
				} catch(Exception e) {
					LOGGER.error("Exception thrown during ensureClosed()");
				}
			}
		}
	}
	
	
	public static void ensureAllConnectionsClosed() {
		LOGGER.debug("Cleaning up...");
		List<Executor> executors = EXECUTORS.get();
		EXECUTORS.remove();
		
		for(Executor executor : executors) {
			executor.ensureClosed();
		}
		LOGGER.debug("Cleaning up complete");
	}
}
