package org.activityinfo.server.command.handler.sync;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;

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
			sb.append(rs.getString(column));
		}
	},
	DATE {
		@Override
		void append(StringBuilder sb, ResultSet rs, int column)
				throws SQLException {
			LocalDate date = new LocalDate(rs.getDate(column));
			
			sb.append('\'').append(date.toString()).append('\'');
		}
	},
	REAL {

		@Override
		void append(StringBuilder sb, ResultSet rs, int column)
				throws SQLException {
			sb.append(rs.getDouble(column));
		}
		
	};
	
	abstract void append(StringBuilder sb, ResultSet rs, int column) throws SQLException;
	
	public static ColumnAppender forType(int columnType) {
		switch( columnType ) {
		case Types.VARCHAR:
		case Types.NVARCHAR:
		case Types.LONGVARCHAR:
		case Types.LONGNVARCHAR:
			return ColumnAppender.STRING;
			
		case Types.BIT:
		case Types.INTEGER:
		case Types.NUMERIC:
			return ColumnAppender.INTEGER;
			
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
			return ColumnAppender.REAL;
			
		case Types.DATE:
			return ColumnAppender.DATE;
			
		default:
			throw new UnsupportedOperationException("type: " + columnType);
		}

	}
}