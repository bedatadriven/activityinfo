package org.activityinfo.server.database;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class DatabaseInitFilter implements Filter {

	public static final String SCHEMA_MIGRATION = "schema.migration";
	
	private static final Logger logger = Logger.getLogger(BoneCpConnectionProvider.class);
	
	private final Provider<Connection> connectionProvider;
	private final Properties config;
	
	@Inject
	public DatabaseInitFilter(Provider<Connection> connectionProvider, Properties config) {
		super();
		this.connectionProvider = connectionProvider;
		this.config = config;
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		maybeMigrateSchema();
	}
	
	public void maybeMigrateSchema() {
		if("enabled".equals(config.get(SCHEMA_MIGRATION)) ||
	       "update".equals(config.get("hibernate.hbm2ddl.auto"))) {
			Connection connection=null;
			try {		
				connection = connectionProvider.get();
				Liquibase liquibase = new Liquibase("org/activityinfo/database/changelog/db.changelog-master.xml",
						new ClassLoaderResourceAccessor(), new JdbcConnection(connection));
				
				liquibase.update(null);
				
			} catch (Exception e) {
				logger.error("Liquibase schema migration failed, will try to start anyway", e);
			} finally {
				if(connection != null) {
					try {
						connection.close();
					} catch(Exception ignored){
					}
				}
			}
		}
	}	
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain filterChain) throws IOException, ServletException {
		try {
			filterChain.doFilter(req, resp);
		} finally {
			ServerDatabase.ensureAllConnectionsClosed();
		}
	}
}
