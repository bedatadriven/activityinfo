package org.activityinfo.server.command.handler.sync;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;

public class SqliteInsertBuilder {

	private StringBuilder sql;
	private String tableName;
	private StringBuilder insert;
	private ColumnAppender appenders[];
	private int numColumns;
	private ResultSet rs;
	private SqlQuery query;
	private SqliteBatchBuilder batch;
	
	public SqliteInsertBuilder(SqliteBatchBuilder batch) {
		this.batch = batch;
	}
	
	public SqliteInsertBuilder into(String tableName) {
		this.tableName = tableName;
		return this;
	}
	
	public SqliteInsertBuilder from(SqlQuery query) {
		this.query = query;
		return this;
	}
	
	public void execute(EntityManager entityManager) {
		((HibernateEntityManager)entityManager).getSession().doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				rs = SqlQueryUtil.query(connection, query);
 				numColumns = rs.getMetaData().getColumnCount();
				setupAppenders();
				composeInsertStatement();
				
				try {
					appendRows();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}
	
	private void composeInsertStatement() throws SQLException {
		insert = new StringBuilder();
		insert.append("INSERT OR REPLACE INTO ").append(tableName).append(" (");
		for(int i=0;i!=numColumns;++i) {
			if(i > 0) {
				insert.append(",");
			}
			insert.append(rs.getMetaData().getColumnName(i+1));
		}
		insert.append(") ");
	}

	private void setupAppenders()
			throws SQLException {
		this.appenders = new ColumnAppender[numColumns];
		for(int i=0;i!=numColumns;++i) {
			appenders[i] = ColumnAppender.forType(rs.getMetaData().getColumnType(i+1));
		}
	}


	
	public void appendRows() throws SQLException, IOException {
		sql = new StringBuilder();
		int rowCount = 0;
		while(rs.next()) {
			if(rowCount == 0) {
				sql.append(insert);
			} else {
				sql.append(" UNION ");
			}
			sql.append("SELECT ");
			for(int i=0;i!=numColumns;++i) {
				if(i > 0) {
					sql.append(',');
				}
				appenders[i].append(sql, rs, i+1);
			}
			rowCount ++;
			if(rowCount > 450) {
				batch.addStatement(sql.toString());
				sql.setLength(0);
				rowCount = 0;
			}
		}
		if(rowCount > 0) {
			batch.addStatement(sql.toString());
		}
	}
}
