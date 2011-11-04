package org.sigmah.server.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

@Singleton
public class BoneCpConnectionProvider implements Provider<Connection> {

	private static final Logger logger = Logger.getLogger(BoneCpConnectionProvider.class);
	
	private BoneCP connectionPool;

	@Inject
	public BoneCpConnectionProvider(Properties configProperties) {
		try {
		 	String url = configProperties.getProperty("hibernate.connection.url");

			logger.info("Starting connection pool to " + url);
			
			Class.forName(configProperties.getProperty("hibernate.connection.driver_class"));
		 	BoneCPConfig poolConfig = new BoneCPConfig();	
			poolConfig.setJdbcUrl(url);
			poolConfig.setUsername(configProperties.getProperty("hibernate.connection.username"));
			poolConfig.setPassword(configProperties.getProperty("hibernate.connection.password"));
			
			connectionPool = new BoneCP(poolConfig);			
			
		} catch(Exception e) {
			throw new RuntimeException("Exception thrown while creating connection pool", e);
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
