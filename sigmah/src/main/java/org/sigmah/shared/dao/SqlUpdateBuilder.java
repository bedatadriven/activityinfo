package org.sigmah.shared.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.datatype.DatatypeElement;

public class SqlUpdateBuilder {
	private String tableName;
	private String action;
	private List<Object> values = new ArrayList<Object>();
	private List<String> columns = new ArrayList<String>();
	
	private StringBuilder where = new StringBuilder();
	private List<Object> whereParameters = new ArrayList<Object>();
	
	private static String UPDATE = "UPDATE";
	private static String DELETE = "DELETE FROM";
	
	private SqlUpdateBuilder(String action, String tableName) {
		this.action = action;
		this.tableName = tableName;
	}

	public static SqlUpdateBuilder update(String tableName) {
		return new SqlUpdateBuilder(UPDATE, tableName);
	}
	
	public static SqlUpdateBuilder delete(String tableName) {
		return new SqlUpdateBuilder(DELETE, tableName);
	}
	
	public SqlUpdateBuilder where(String columnName, Object value) {
		if(where.length() > 0) {
			where.append(" AND ");
		} 
		where.append(columnName).append("=?");
		whereParameters.add(value);
		
		return this;
	}
	
	public SqlUpdateBuilder value(String columnName, Object value) {
		columns.add(columnName);
		values.add(value);
		return this;
	}
	
	public SqlUpdateBuilder value(String columnName, Map<String, Object> properties, String propertyName) {
		if(properties.containsKey(propertyName)) {
			columns.add(columnName);
			values.add(properties.get(propertyName));
		}
		return this;
	}

	public SqlUpdateBuilder value(String columnName, Map<String, Object> properties) {
		return value(columnName, properties, columnName);
	}
	
	public void execute(Connection connection) throws SQLException {

		if(where.length() == 0) {
			throw new RuntimeException("Where clause not specified");
		}

		if(UPDATE.equals(action) && values.isEmpty()) {
			return; // nothing to do.
		}
		
		
		int nextParamIndex = 1;
		
		StringBuilder sql = new StringBuilder(action)
			.append(" ")
			.append(tableName);
		
		
		if(UPDATE.equals(action)) {
			sql.append(" SET ");
			
			for(int i=0;i!=columns.size();++i) {
				if(i>0) {
					sql.append(", ");
				}
				sql.append(columns.get(i)).append("=?");
			}
		}
		
		sql.append(" WHERE ").append(where.toString());
		
		
	    PreparedStatement stmt = connection.prepareStatement(sql.toString());
        for(Object value : values) {
            stmt.setObject(nextParamIndex++, value);
        }
        for(Object param : whereParameters) {
        	stmt.setObject(nextParamIndex++, param);
        }
        
        stmt.execute();
	}
}
