package org.activityinfo.server.database.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.activityinfo.server.database.BoneCpConnectionProvider;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.util.PropertiesHelper;

/**
 * Wrapper class to allow us to use the bone cp provider as the 
 * primary connection provider.
 *
 */
public class HibernateConnectionProvider implements ConnectionProvider {

	public static BoneCpConnectionProvider DELEGATE;

	private static Logger LOGGER = Logger.getLogger(HibernateConnectionProvider.class);

	private Integer isolation;
	private boolean autocommit;

	public HibernateConnectionProvider() {
		LOGGER.info("HibernateConnectionProvider created");
	}

	@Override
	public void configure(Properties props) throws HibernateException {		
		LOGGER.info("HibernateConnectionProvider configured");
		this.isolation = PropertiesHelper.getInteger(Environment.ISOLATION, props);
		this.autocommit = PropertiesHelper.getBoolean(Environment.AUTOCOMMIT, props);

	}

	@Override
	public Connection getConnection() throws SQLException {
		if(DELEGATE == null) {
			throw new RuntimeException("Bone connection pool has not yet been initialized!");
		}
		
		Connection connection = DELEGATE.get();

		// set the Transaction Isolation if defined
		try {
			// set the Transaction Isolation if defined
			if ((this.isolation != null) && (connection.getTransactionIsolation() != this.isolation.intValue())) {
				connection.setTransactionIsolation (this.isolation.intValue());
			}

			// toggle autoCommit to false if set
			if ( connection.getAutoCommit() != this.autocommit ){
				connection.setAutoCommit(this.autocommit);
			}

			return connection;
		} catch (SQLException e){
			try {
				connection.close();
			} catch (Exception e2) {
				LOGGER.warn("Setting connection properties failed and closing this connection failed again", e);
			}

			throw e;
		} 
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
