package org.activityinfo.server.command.handler.sync;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.bedatadriven.rebar.time.calendar.LocalDate;

enum ColumnAppender {
	
	STRING {
		@Override
		void append(StringBuilder sb, ResultSet rs, int column) throws SQLException {
			String value = rs.getString(column);
			if(value == null) {
				sb.append("NULL");
			} else {
				sb.append('\'');
				for(int i=0;i!=value.length();++i) {
					int cp = value.codePointAt(i);
					if(cp == '\'') {
						sb.append('\'').append('\'');
					} else {
						sb.appendCodePoint(cp);
					}
				}
				sb.append('\'');
			}
		}			
	},
	INTEGER {

		@Override
		void append(StringBuilder sb, ResultSet rs, int column)
				throws SQLException {
			int value = rs.getInt(column);
			if(rs.wasNull()) {
				sb.append("NULL");
			} else {
				sb.append(value);
			}
		}
	},
	DATE {
		@Override
		void append(StringBuilder sb, ResultSet rs, int column)
				throws SQLException {
			Date date = rs.getDate(column);
			if(date == null) {
				sb.append("NULL");
			} else { 
				LocalDate localDate = new LocalDate(date);
				sb.append('\'').append(localDate.toString()).append('\'');
			}
		}
	},
	REAL {
		@Override
		void append(StringBuilder sb, ResultSet rs, int column)
				throws SQLException {
			double value = rs.getDouble(column);
			if(rs.wasNull()) {
				sb.append("NULL");
			} else {
				sb.append(value);
			}
		}
	};
	
	abstract void append(StringBuilder sb, ResultSet rs, int column) throws SQLException;
	
	public static ColumnAppender forType(int columnType) {
		switch( columnType ) {
		case Types.VARCHAR:
		case Types.NVARCHAR:
		case Types.LONGVARCHAR:
		case Types.LONGNVARCHAR:
		case Types.CLOB:
			return ColumnAppender.STRING;
			
		case Types.BIT:
		case Types.INTEGER:
		case Types.NUMERIC:
			return ColumnAppender.INTEGER;
			
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.BIGINT:
			return ColumnAppender.REAL;
			
		case Types.DATE:
			return ColumnAppender.DATE;
			
		default:
			throw new UnsupportedOperationException("type: " + columnType);
		}

	}
}