package org.activityinfo.server.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.apache.log4j.Logger;

import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.ConnectionHandle;
import com.jolbox.bonecp.Statistics;
import com.jolbox.bonecp.hooks.AbstractConnectionHook;

@Singleton
public class BoneCpConnectionPool implements Provider<Connection> {

	public static final String SCHEMA_MIGRATION = "schema.migration";

	private static final Logger LOGGER = Logger.getLogger(BoneCpConnectionPool.class);
	
	private BoneCP connectionPool;

	@Inject
	public BoneCpConnectionPool(DeploymentConfiguration configProperties) {

		try {

			Class.forName(configProperties.getProperty("hibernate.connection.driver_class"));
		 	BoneCPConfig poolConfig = new BoneCPConfig(configProperties.asProperties());	
		 	poolConfig.setJdbcUrl(configProperties.getProperty("hibernate.connection.url"));
		 	poolConfig.setDriverProperties(MySqlConfig.get(
		 			configProperties.getProperty("hibernate.connection.username"),	
		 			configProperties.getProperty("hibernate.connection.password")));
			poolConfig.setMaxConnectionAge(4, TimeUnit.HOURS);
			poolConfig.setPartitionCount(3);
			poolConfig.setConnectionHook(new AbstractConnectionHook() {
				
				@Override
				public void onAcquire(ConnectionHandle connection) {
					super.onAcquire(connection);
					MySqlConfig.initConnection(connection.getInternalConnection());
				}
			});
			
			LOGGER.info("Configuring connection pool to " + poolConfig.getJdbcUrl());
			
			connectionPool = new BoneCP(poolConfig);
			
			maybeMigrateSchema(configProperties);
			
			
		} catch(Exception e) {
			throw new RuntimeException("Exception thrown while creating connection pool", e);
		}
	}
	
	public final void maybeMigrateSchema(DeploymentConfiguration config) {
		if("enabled".equals(config.getProperty(SCHEMA_MIGRATION)) ||
	       "update".equals(config.getProperty("hibernate.hbm2ddl.auto"))) {
			Log.info("Schema migration starting...");
			Connection connection=null;
			try {		
				try {
					connection = connectionPool.getConnection();
				} catch (SQLException e) {
					throw new RuntimeException("Could not open connection for schema migration", e);
				}
				Liquibase liquibase = new Liquibase("org/activityinfo/database/changelog/db.changelog-master.xml",
						new ClassLoaderResourceAccessor(), new JdbcConnection(connection));
				
				liquibase.update(null);
				Log.info("Schema migration complete.");

			} catch (LiquibaseException e) {
				LOGGER.error("Liquibase schema migration failed", e);
			} finally {
				if(connection != null) {
					try {
						connection.close();
					} catch(Exception ignored){
					}
				}
			}
		} else{ 
			Log.info("Schema migration disabled");
		}
	}	
	
	public Statistics getStatistics() {
		return connectionPool.getStatistics();
	}
	
	@Override
	public Connection get() {
		try {
			return connectionPool.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("Exception thrown while obtaining connection", e);
		}
	}

	public void shutdown() {
		connectionPool.shutdown();
	}
}
