package org.activityinfo.server.database;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Singleton;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.bedatadriven.rebar.sql.client.query.MySqlDialect;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Provider;

@Singleton
public class HibernateDatabase extends SqlDatabase {

	private final Provider<HibernateEntityManager> entityManager;
	
	public HibernateDatabase(Provider<HibernateEntityManager> entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@Override
	public void transaction(SqlTransactionCallback callback) {
		entityManager.get().getSession().doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				
			}
		});
	}

	@Override
	public SqlDialect getDialect() {
		return new MySqlDialect();
	}

	@Override
	public void executeUpdates(String bulkOperationJsonArray,
			AsyncCallback<Integer> callback) {
		callback.onFailure(new UnsupportedOperationException());
	}

	@Override
	public String getName() {
		return "Hibernate";
	}
}
