package org.activityinfo.server.command.handler.sync;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;

import org.activityinfo.server.database.hibernate.HibernateExecutor;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Lists;

public class SqlQueryUtil {

	public static ResultSet query(Connection connection, final SqlQuery query)
			throws SQLException {
		PreparedStatement statement = connection.prepareStatement(query.sql());
		Object[] params = query.parameters();
		for(int i=0;i!=params.length;++i) {
			statement.setObject(i+1, params[i]);
		}
		ResultSet rs = statement.executeQuery();
		return rs;
	}

	public static String queryIdSet(EntityManager entityManager, final SqlQuery query) {
		final StringBuilder set = new StringBuilder();
		((HibernateEntityManager)entityManager).getSession().doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				set.append("(");
				boolean needsComma = false;
				ResultSet rs = query(connection, query);
				ColumnAppender appender = ColumnAppender.forType(rs.getMetaData().getColumnType(1));
				while(rs.next()) {
					if(needsComma) {
						set.append(',');
					}
					appender.append(set, rs, 1);
					needsComma = true;
				}
				set.append(")");
			}
		});
		return set.toString();
	}

	public static long queryLong(EntityManager entityManager, final SqlQuery query) {
		final List<Long> result = Lists.newArrayList();
		((HibernateEntityManager)entityManager).getSession().doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				ResultSet rs = query(connection, query);
				rs.next();
				result.add(rs.getLong(1));
			}
		});
		return result.get(0);
	}

}
