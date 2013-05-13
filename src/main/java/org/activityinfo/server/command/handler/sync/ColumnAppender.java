package org.activityinfo.server.command.handler.sync;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.bedatadriven.rebar.time.calendar.LocalDate;

enum ColumnAppender {

    STRING {
        @Override
        void append(StringBuilder sb, ResultSet rs, int column)
            throws SQLException {
            String value = rs.getString(column);
            if (value == null) {
                sb.append("NULL");
            } else {
                sb.append('\'');
                for (int i = 0; i != value.length(); ++i) {
                    int cp = value.codePointAt(i);
                    if (cp == '\'') {
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
            if (rs.wasNull()) {
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
            if (date == null) {
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
            if (rs.wasNull()) {
                sb.append("NULL");
            } else {
                sb.append(value);
            }
        }
    };

    abstract void append(StringBuilder sb, ResultSet rs, int column)
        throws SQLException;

    public static ColumnAppender forType(int columnType) {
        switch (columnType) {
        case Types.VARCHAR:
        case Types.NVARCHAR:
        case Types.LONGVARCHAR:
        case Types.LONGNVARCHAR:
        case Types.CLOB:
            return ColumnAppender.STRING;

        case Types.BIT:
        case Types.TINYINT:
        case Types.SMALLINT:
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