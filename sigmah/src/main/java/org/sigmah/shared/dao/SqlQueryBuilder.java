/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dao;

import org.sigmah.shared.report.model.DimensionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Lightweight DSL for building native SQL queries.
 */
public class SqlQueryBuilder {

    protected StringBuilder fieldList = new StringBuilder();
    protected StringBuilder tableList = new StringBuilder();
    protected StringBuilder whereClause = new StringBuilder();
    protected StringBuilder orderByClause = new StringBuilder();
    protected List<Object> parameters = new ArrayList<Object>();

    private String limitClause = "";

    public SqlQueryBuilder() {
    }

    /**
     * Appends a table list to the {@code FROM} clause
     * @param fromClause valid SQL table list, can include joins
     */
    public SqlQueryBuilder from(String fromClause) {
        tableList.append(fromClause);
        return this;
    }

    /**
     * Appends a left join to the {@code FROM} clause
     * @param tableName
     * @return
     */
    public JoinBuilder leftJoin(String tableName) {
        tableList.append(" LEFT JOIN ").append(tableName);
        return new JoinBuilder();
    }

    /**
     * Appends a left join to derived table to the {@code FROM} clause
     */
    public JoinBuilder leftJoin(SqlQueryBuilder derivedTable, String alias) {
        parameters.addAll(derivedTable.parameters);
        tableList.append(" LEFT JOIN (")
                .append(derivedTable.sql())
                .append(")")
                .append(" AS ")
                .append(alias);

        return new JoinBuilder();
    }

    /**
     * Appends a field or a comma separated list of fields to the field list.
     *
     */
    public SqlQueryBuilder appendField(String expr) {
        if(fieldList.length() != 0) {
            fieldList.append(", ");
        }
        fieldList.append(expr);
        return this;
    }

    public SqlQueryBuilder orderBy(String expr) {
        if(orderByClause.length() > 0) {
            orderByClause.append(", ");
        }
        orderByClause.append(expr);
        return this;
    }

    public void setLimitClause(String clause) {
        this.limitClause = clause;
    }

    public WhereClauseBuilder where(String expr) {
        if(whereClause.length() > 0) {
            whereClause.append(" AND ");
        }
        whereClause.append(expr);
        return new WhereClauseBuilder();
    }


    public SqlQueryBuilder whereTrue(String expr) {
        if(whereClause.length() > 0) {
            whereClause.append(" AND ");
        }
        whereClause.append(expr);
        return this;
    }

    public SqlQueryBuilder and(String expr) {
        whereClause.append(" AND (").append(expr).append(") ");
        return this;
    }

    public SqlQueryBuilder filteredBy(Filter filter) {
        for (DimensionType type : filter.getRestrictedDimensions()) {
            if (type == DimensionType.Indicator) {
                where("Indicator.IndicatorId").in(filter.getRestrictions(type));

            } else if (type == DimensionType.Activity) {
                where("Site.ActivityId").in(filter.getRestrictions(type));

            } else if (type == DimensionType.Database) {
                where("Activity.DatabaseId").in(filter.getRestrictions(type));

            } else if (type == DimensionType.Partner) {
                where("Site.PartnerId").in(filter.getRestrictions(type));

            } else if (type == DimensionType.AdminLevel) {
                where("Site.LocationId").in(
                        select("Link.LocationId").from("LocationAdminLink Link").where("Link.AdminEntityId")
                                .in(filter.getRestrictions(type)));

            } else if(type == DimensionType.Site) {
                where("Site.SiteId").in(filter.getRestrictions(type));
            }
        }
        return this;
    }

    public String sql() {
        StringBuilder sql = new StringBuilder("SELECT ")
                .append(fieldList.toString())
                .append(" FROM ")
                .append(tableList.toString());

        if(whereClause.length() > 0) {
            sql.append(" WHERE ")
                    .append(whereClause.toString());
        }
        if(orderByClause.length() > 0) {
            sql.append(" ORDER BY ")
                    .append(orderByClause.toString());
        }
        sql.append(" ")
                .append(limitClause);

        return sql.toString();
    }

    public ResultSet executeQuery(Connection connection) throws SQLException {
        String sql = sql();
        PreparedStatement stmt = prepareStatement(connection, sql);
        return stmt.executeQuery();
    }

    private PreparedStatement prepareStatement(Connection connection, String sql) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(sql);
        for(int i=0;i!=parameters.size();++i) {
            stmt.setObject(i+1, parameters.get(i));
        }
        return stmt;
    }

    public void forEachResult(Connection connection, ResultHandler handler) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = sql();

        try {
            stmt = prepareStatement(connection, sql);
            rs = stmt.executeQuery();

            handler.init(rs);

            while(rs.next()) {
                handler.handle(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Exception thrown while processing SQL: '" + sql + "'", e);
        } finally {
            if(rs != null) { try { rs.close(); } catch(SQLException ignored) {} }
            if(stmt != null) { try { stmt.close(); } catch(SQLException ignored) {} }
        }
    }

    /**
     * Executes the statement and returns the value of the first column of the first row,
     * or null if there are no results.
     *
     * @param conn JDBC connection
     */
    public Date singleDateResultOrNull(Connection conn) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = sql();

        try {
            stmt = prepareStatement(conn, sql);
            rs = stmt.executeQuery();

            if(rs.next()) {
                return rs.getDate(1);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Exception thrown while processing SQL: '" + sql + "'", e);
        } finally {
            if(rs != null) { try { rs.close(); } catch(SQLException ignored) {} }
            if(stmt != null) { try { stmt.close(); } catch(SQLException ignored) {} }
        }

    }


    public class WhereClauseBuilder {

        public SqlQueryBuilder in(Collection<?> ids) {
            if (ids.isEmpty()) {
                throw new IllegalArgumentException("Cannot match against empty list.");
            }
            if (ids.size() == 1) {
                whereClause.append(" = ?");
            } else {
                whereClause.append(" IN (? ");
                for (int i = 1; i < ids.size(); ++i) {
                    whereClause.append(", ?");
                }
                whereClause.append(")");
            }
            parameters.addAll(ids);
            return SqlQueryBuilder.this;
        }

        public SqlQueryBuilder in(SqlQueryBuilder subquery) {
            whereClause.append(" IN (")
                    .append(subquery.sql())
                    .append(") ");

            parameters.addAll(subquery.parameters);

            return SqlQueryBuilder.this;
        }

        public SqlQueryBuilder equalTo(Object value) {
            whereClause.append(" = ? ");
            parameters.add(value);

            return SqlQueryBuilder.this;
        }
    }

    public class JoinBuilder {
        public SqlQueryBuilder on(String expr) {
            tableList.append(" ON (").append(expr).append(") ");
            return SqlQueryBuilder.this;
        }
    }

    public static SqlQueryBuilder select(String... fieldList) {
        SqlQueryBuilder builder = new SqlQueryBuilder();
        for(String e : fieldList) {
            builder.appendField(e);
        }
        return builder;
    }

    public static abstract class ResultHandler {

        public void init(ResultSet rs) throws SQLException {

        }

        public abstract void handle(ResultSet rs) throws SQLException;

    }
}
