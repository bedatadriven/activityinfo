package org.activityinfo.server.database.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;

import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcExecutor;
import com.google.common.collect.Lists;

public class HibernateExecutor extends JdbcExecutor {

	private final HibernateEntityManager entityManager;
	
	public HibernateExecutor(HibernateEntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@Override
	public SqlResultSet execute(final String statement, final Object[] params)
			throws Exception {
		final List<SqlResultSet> result = Lists.newArrayList();
		entityManager.getSession().doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				try {
					result.add(doExecute(connection, statement, params));
				} catch (Exception e) {
					throw new SQLException(e);
				}
			}
		});
		if(result.size() != 1) {
			throw new AssertionError();
		}
		return result.get(0);
	}

	@Override
	public boolean begin() throws Exception {
		return true;
	}

	@Override
	public void commit() throws Exception {
	}

	@Override
	public void rollback() throws Exception {
		
	}

}
