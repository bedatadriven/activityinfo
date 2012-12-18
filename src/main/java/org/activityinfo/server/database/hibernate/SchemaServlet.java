package org.activityinfo.server.database.hibernate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class SchemaServlet extends HttpServlet {

	public static final String ENDPOINT = "/tasks/migrateSchema";

	private static final Logger LOGGER = Logger.getLogger(SchemaServlet.class.getName());
	
	private final Provider<EntityManager> entityManager;
	
	@Inject
	public SchemaServlet(Provider<EntityManager> entityManager) {
		super();
		this.entityManager = entityManager;
	}



	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		performMigration((HibernateEntityManager) this.entityManager.get());
		
	}



	public static void performMigration(HibernateEntityManager entityManager) {
		entityManager.getSession().doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {

				Liquibase liquibase;
				try {
					liquibase = new Liquibase("org/activityinfo/database/changelog/db.changelog-master.xml",
							new ClassLoaderResourceAccessor(), new CloudSqlConnection(connection));
					liquibase.update(null);
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, "Exception whilst migrating schema", e);
				}
			}
		});
	}
	
	private static class CloudSqlConnection extends JdbcConnection {

		public CloudSqlConnection(Connection connection) {
			super(connection);
		}

		@Override
		public String getDatabaseProductName() throws DatabaseException {
			return "MySQL";
		}
		
	}
	
}
