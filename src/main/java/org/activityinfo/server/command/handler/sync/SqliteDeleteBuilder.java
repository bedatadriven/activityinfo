package org.activityinfo.server.command.handler.sync;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;


public class SqliteDeleteBuilder {

	private final SqliteBatchBuilder batch;
	private String tableName;
	private String idName;
	private SqlQuery query;
	protected ResultSet rs;

	public SqliteDeleteBuilder(SqliteBatchBuilder batch) {
		super();
		this.batch = batch;
	}

	public SqliteDeleteBuilder from(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public SqliteDeleteBuilder where(String id) {
		this.idName = id;
		return this;
	}
	
	public SqliteDeleteBuilder in(SqlQuery query) {
		this.query = query;
		return this;
	}

	public void execute(EntityManager entityManager) {
		((HibernateEntityManager)entityManager).getSession().doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				rs = SqlQueryUtil.query(connection, query);
				StringBuilder sql = new StringBuilder();
				sql.append("DELETE FROM ")
					.append(tableName)
					.append(" WHERE ")
					.append(idName)
					.append(" IN (");
				
				boolean needsComma = false;
				while(rs.next()) {
					if(needsComma) {
						sql.append(',');
					}
					sql.append(rs.getInt(1));
					needsComma = true;
				}
				sql.append(")");
				try {
					batch.addStatement(sql.toString());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}
	
	
}
