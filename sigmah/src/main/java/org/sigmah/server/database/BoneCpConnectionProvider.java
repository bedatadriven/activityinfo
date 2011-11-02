package org.sigmah.server.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

@Singleton
public class BoneCpConnectionProvider implements Provider<Connection> {

	public static final String SCHEMA_MIGRATION = "hibernate.hbm2ddl.auto";

	private static final Logger logger = Logger.getLogger(BoneCpConnectionProvider.class);
	
	private BoneCP connectionPool;

	@Inject
	public BoneCpConnectionProvider(Properties configProperties) {
		try {
			Class.forName(configProperties.getProperty("hibernate.connection.driver_class"));
		 	BoneCPConfig poolConfig = new BoneCPConfig();	
		 	poolConfig.setJdbcUrl(configProperties.getProperty("hibernate.connection.url"));
			poolConfig.setUsername(configProperties.getProperty("hibernate.connection.username"));
			poolConfig.setPassword(configProperties.getProperty("hibernate.connection.password"));
			
			connectionPool = new BoneCP(poolConfig);
			
			maybeMigrateSchema(configProperties);
			
			
		} catch(Exception e) {
			throw new RuntimeException("Exception thrown while creating connection pool", e);
		}
	}
	
	public void maybeMigrateSchema(Properties config) {
		if("update".equals(config.get(SCHEMA_MIGRATION))) {
			try {		
				Connection connection;
				try {
					connection = connectionPool.getConnection();
				} catch (SQLException e) {
					throw new RuntimeException("Could not open connection for schema migration", e);
				}
				Liquibase liquibase = new Liquibase("org/activityinfo/database/changelog/db.changelog-master.xml",
						new ClassLoaderResourceAccessor(), new JdbcConnection(connection));
				
				liquibase.update(null);
			} catch (LiquibaseException e) {
				logger.error("Liquibase schema migration failed", e);
			}
		}
	}	
	
	@Override
	public Connection get() {
		try {
			return connectionPool.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("Exception thrown while obtaining connection", e);
		}
	}
}
