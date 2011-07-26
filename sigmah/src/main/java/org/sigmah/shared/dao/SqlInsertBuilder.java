package org.sigmah.shared.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlInsertBuilder {
	
	private String tableName;
	private List<Object> values = new ArrayList<Object>();
	private List<String> columns = new ArrayList<String>();
	
	public SqlInsertBuilder(String tableName) {
		this.tableName = tableName;
	}

	public static SqlInsertBuilder insertInto(String tableName) {
		return new SqlInsertBuilder(tableName);
	}
	
	public SqlInsertBuilder value(String columnName, Object value) {
		if(value != null) {
			columns.add(columnName);
			values.add(value);
		}
		return this;
	}
	
	public void execute(Connection connection) throws SQLException {
		StringBuilder sql = new StringBuilder("INSERT INTO ")
			.append(tableName)
			.append(" (");
		
		for(int i=0;i!=columns.size();++i) {
			if(i>0) {
				sql.append(", ");
			}
			sql.append(columns.get(i));
		}
		sql.append(") VALUES (");
		
		for(int i=0;i!=columns.size();++i) {
			if(i>0) {
				sql.append(", ");
			}
			sql.append("?");
		}
		sql.append(")");
		
	    PreparedStatement stmt = connection.prepareStatement(sql.toString());
        for(int i=0;i!=values.size();++i) {
            stmt.setObject(i+1, values.get(i));
        }
        
        stmt.execute();
	}

}
