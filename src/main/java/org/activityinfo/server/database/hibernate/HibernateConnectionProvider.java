package org.activityinfo.server.database.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.activityinfo.server.database.BoneCpConnectionProvider;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.connection.ConnectionProvider;

/**
 * Wrapper class to allow us to use the bone cp provider as the 
 * primary connection provider.
 *
 */
public class HibernateConnectionProvider implements ConnectionProvider {
	
	public static BoneCpConnectionProvider DELEGATE;
	
	private static Logger LOGGER = Logger.getLogger(HibernateConnectionProvider.class);
	
	public HibernateConnectionProvider() {
		LOGGER.info("HibernateConnectionProvider created");
	}
	
	@Override
	public void configure(Properties props) throws HibernateException {		
		LOGGER.info("HibernateConnectionProvider configured");
	}

	@Override
	public Connection getConnection() throws SQLException {
		if(DELEGATE == null) {
			throw new RuntimeException("Bone connection pool has not yet been initialized!");
		}
		return DELEGATE.get();
	}

	@Override
	public void closeConnection(Connection conn) throws SQLException {
		conn.close();
	}

	@Override
	public void close() throws HibernateException {
		// noop
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return false;
	}
}
