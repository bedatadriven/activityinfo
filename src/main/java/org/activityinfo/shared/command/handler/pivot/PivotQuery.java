package org.activityinfo.shared.command.handler.pivot;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.activityinfo.client.Log;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.handler.pivot.bundler.AttributeBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.Bundler;
import org.activityinfo.shared.command.handler.pivot.bundler.EntityBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.MonthBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.MySqlYearWeekBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.OrderedEntityBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.QuarterBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.SimpleBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.YearBundler;
import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.report.model.AdminDimension;
import org.activityinfo.shared.report.model.AttributeGroupDimension;
import org.activityinfo.shared.report.model.DateDimension;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Sets;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PivotQuery {
    private final Filter filter;
    private final Set<Dimension> dimensions;

    private final BaseTable baseTable;

    private final int userId;
    private final SqlDialect dialect;

    private final SqlTransaction tx;

    private SqlQuery query;
    private final List<Bundler> bundlers = new ArrayList<Bundler>();

    private PivotSites command;
    private PivotQueryContext context;

    private Set<String> dimColumns = Sets.newHashSet();

    public PivotQuery(PivotQueryContext context, BaseTable baseTable) {
        super();
        this.context = context;
        this.command = context.getCommand();
        this.filter = context.getCommand().getFilter();
        this.dimensions = context.getCommand().getDimensions();
        this.dialect = context.getDialect();
        this.userId = context.getExecutionContext().getUser().getUserId();
        this.tx = context.getExecutionContext().getTransaction();
        this.baseTable = baseTable;

        this.query = baseTable.createSqlQuery();
    }

    private String appendDimColumn(String expr) {
        String alias = expr.replace('.', '_');
        return appendDimColumn(alias, expr);
    }

    private String appendDimColumn(String alias, String expr) {
        if (!dimColumns.contains(alias)) {
            if (baseTable.groupDimColumns()) {
                query.groupBy(expr);
            }
            query.appendColumn(expr, alias);
            dimColumns.add(alias);
        }
        return alias;
    }

    public void execute(final AsyncCallback<Void> callback) {

        baseTable.setupQuery(command, query);

        if (command.isPivotedBy(DimensionType.Location)) {
            query.leftJoin(Tables.LOCATION, "Location")
                .on("Location.LocationId=" + baseTable.getDimensionIdColumn(DimensionType.Location));
        }
        if (command.isPivotedBy(DimensionType.Partner)) {
            query.leftJoin(Tables.PARTNER, "Partner")
                .on("Partner.PartnerId=" + baseTable.getDimensionIdColumn(DimensionType.Partner));

        }
        if (command.isPivotedBy(DimensionType.Project)) {
            query.leftJoin(Tables.PROJECT, "Project")
                .on("Project.ProjectId=" + baseTable.getDimensionIdColumn(DimensionType.Project));
        }

        addDimensionBundlers();

        // Only allow results that are visible to this user if we are on the
        // server,
        // otherwise permissions have already been taken into account during
        // synchronization
        if (isRemote()) {
            appendVisibilityFilter();
        }

        if (filter.getMinDate() != null) {
            query.where(baseTable.getDateCompleteColumn())
                .greaterThanOrEqualTo(filter.getMinDate());
        }
        if (filter.getMaxDate() != null) {
            query.where(baseTable.getDateCompleteColumn()).lessThanOrEqualTo(
                filter.getMaxDate());
        }

        appendDimensionRestrictions();

        if (Log.isDebugEnabled()) {
            Log.debug("PivotQuery executing query: " + query.sql());
        }

        query.execute(tx, new SqlResultCallback() {

            @Override
            public void onSuccess(SqlTransaction tx, SqlResultSet results) {
                for (SqlResultSetRow row : results.getRows()) {
                    Bucket bucket = new Bucket();
                    bucket.setAggregationMethod(row.getInt(ValueFields.AGGREGATION));
                    bucket.setCount(row.getInt(ValueFields.COUNT));
                    if (!row.isNull(ValueFields.SUM)) {
                        bucket.setSum(row.getDouble(ValueFields.SUM));
                    }
                    bucket.setCategory(new Dimension(DimensionType.Target),
                        baseTable.getTargetCategory());

                    for (Bundler bundler : bundlers) {
                        bundler.bundle(row, bucket);
                    }
                    context.addBucket(bucket);
                }

                callback.onSuccess(null);
            }
        });
    }

    private boolean isRemote() {
        return this.context.getExecutionContext().isRemote();
    }

    private void addDimensionBundlers() {
        /* Now add any other dimensions */
        for (Dimension dimension : dimensions) {

            if (dimension == null) {
                Log.error("NULL dimension provided to pivot query: dimensions = "
                    + dimensions);

            } else if (dimension.getType() == DimensionType.Activity) {
                addOrderedEntityDimension(dimension, "Activity.ActivityId",
                    "Activity.Name", "Activity.SortOrder");

            } else if (dimension.getType() == DimensionType.ActivityCategory) {
                addSimpleDimension(dimension, "Activity.Category");

            } else if (dimension.getType() == DimensionType.Database) {
                addEntityDimension(dimension, "Activity.DatabaseId",
                    "UserDatabase.Name");

            } else if (dimension.getType() == DimensionType.Partner) {
                addEntityDimension(dimension, "Partner.PartnerId",
                    "Partner.Name");

            } else if (dimension.getType() == DimensionType.Project) {
                addEntityDimension(dimension, "Project.ProjectId",
                    "Project.Name");

            } else if (dimension.getType() == DimensionType.Location) {
                addEntityDimension(dimension, "Site.LocationId",
                    "Location.Name");

            } else if (dimension.getType() == DimensionType.Indicator) {
                addOrderedEntityDimension(dimension, "Indicator.IndicatorId",
                    "Indicator.Name", "Indicator.SortOrder");

            } else if (dimension.getType() == DimensionType.IndicatorCategory) {
                addSimpleDimension(dimension, "Indicator.Category");

            } else if (dimension instanceof DateDimension) {
                DateDimension dateDim = (DateDimension) dimension;

                if (dateDim.getUnit() == DateUnit.YEAR) {
                    String yearAlias = appendDimColumn("year",
                        dialect.yearFunction(baseTable.getDateCompleteColumn()));

                    bundlers.add(new YearBundler(dimension, yearAlias));

                } else if (dateDim.getUnit() == DateUnit.MONTH) {
                    String yearAlias = appendDimColumn("year",
                        dialect.yearFunction(baseTable.getDateCompleteColumn()));
                    String monthAlias = appendDimColumn(
                        "month",
                        dialect.monthFunction(baseTable.getDateCompleteColumn()));

                    bundlers.add(new MonthBundler(dimension, yearAlias,
                        monthAlias));

                } else if (dateDim.getUnit() == DateUnit.QUARTER) {
                    String yearAlias = appendDimColumn("year",
                        dialect.yearFunction(baseTable.getDateCompleteColumn()));
                    String quarterAlias = appendDimColumn("quarter",
                        dialect.quarterFunction(baseTable
                            .getDateCompleteColumn()));

                    bundlers.add(new QuarterBundler(dimension, yearAlias,
                        quarterAlias));
                } else if (dateDim.getUnit() == DateUnit.WEEK_MON) {
                    // Mode = 3 means
                    // "Monday 1-53   with more than 3 days this year"
                    // see
                    // http://dev.mysql.com/doc/refman/5.5/en/date-and-time-functions.html#function_week
                    if (dialect.isMySql()) {
                        String weekAlias = appendDimColumn("yearweek",
                            "YEARWEEK(" + baseTable.getDateCompleteColumn()
                                + ", 3)");
                        bundlers.add(new MySqlYearWeekBundler(dimension,
                            weekAlias));
                    }
                    // TODO: sqlite
                }

            } else if (dimension instanceof AdminDimension) {
                AdminDimension adminDim = (AdminDimension) dimension;

                String tableAlias = "AdminLevel" + adminDim.getLevelId();

                query
                    .from(new StringBuilder(
                        " LEFT JOIN "
                            +
                            "(SELECT L.LocationId, E.AdminEntityId, E.Name "
                            +
                            "FROM locationadminlink L "
                            +
                            "LEFT JOIN adminentity E ON (L.AdminEntityId=E.AdminEntityID) "
                            +
                            "WHERE E.AdminLevelId=")
                        .append(adminDim.getLevelId())
                        .append(") AS ")
                        .append(tableAlias)
                        .append(" ON (")
                        .append(
                            baseTable
                                .getDimensionIdColumn(DimensionType.Location))
                        .append(" =").append(tableAlias).append(".LocationId)")
                        .toString());

                addEntityDimension(dimension, tableAlias + ".AdminEntityId",
                    tableAlias + ".Name");

            } else if (dimension.getType() == DimensionType.Attribute) {
                addEntityDimension(dimension, "AttributeValue.AttributeId", "Attribute.Name");

            } else if (dimension.getType() == DimensionType.AttributeGroup) {
                if (dimension instanceof AttributeGroupDimension) {
                    // specific attributegroup
                    defineAttributeDimension((AttributeGroupDimension) dimension);
                } else {
                    // pivot on attributegroups
                    addEntityDimension(dimension, "Attribute.AttributeGroupId", "AttributeGroup.name");
                }
            }
        }
    }
    
    /**
     * Defines an a dimension based on an Attribute Group defined
     * by the user. This is essentially a custom dimension
     * @param dimension
     */
    private void defineAttributeDimension(AttributeGroupDimension dim) {
        // this pivots the data by a single-valued attribute group

        String valueQueryAlias = "attributeValues"
            + dim.getAttributeGroupId();

        // this first query gives us the single chosen attribute for
        // each site, arbitrarily taking the attribute with the minimum
        // id if more than one attribute has been selected (i.e db is inconsistent)
        
        // note that we select attributes by NAME rather than
        // the attribute group id itself. This permits merging 
        // of attributes from other activities/dbs with the same name
        
        SqlQuery groupNameQuery = SqlQuery
            .select()
            .appendColumn("name")
            .from(Tables.ATTRIBUTE_GROUP)
            .whereTrue("AttributeGroupId=" + dim.getAttributeGroupId());
        
        SqlQuery derivedValueQuery = SqlQuery
            .select()
            .appendColumn("v.siteId", "siteId")
            .appendColumn("min(a.name)", "value")
            .appendColumn("min(a.sortOrder)", "sortOrder")
            .from("attributevalue", "v")
            .leftJoin("attribute", "a").on("v.AttributeId = a.AttributeId")
            .leftJoin("attributegroup", "g").on("a.AttributeGroupId=g.AttributeGroupId")
            .whereTrue("v.value=1")
            .where("g.name").in(groupNameQuery)
            .groupBy("v.siteId");

        query.leftJoin(derivedValueQuery, valueQueryAlias)
            .on(baseTable.getDimensionIdColumn(DimensionType.Site)
                + "=" + valueQueryAlias + ".SiteId");
        
        String valueAlias = appendDimColumn(valueQueryAlias + ".value");
        String sortOrderAlias = appendDimColumn(valueQueryAlias + ".sortOrder");

        bundlers.add(new AttributeBundler(dim, valueAlias, sortOrderAlias));
    }

    private void addEntityDimension(Dimension dimension, String id, String label) {
        String idAlias = appendDimColumn(id);
        String labelAlias = appendDimColumn(label);

        bundlers.add(new EntityBundler(dimension, idAlias, labelAlias));
    }

    private void addOrderedEntityDimension(Dimension dimension, String id,
        String label, String sortOrder) {
        String idAlias = appendDimColumn(id);
        String labelAlias = appendDimColumn(label);
        String sortOrderAlias = appendDimColumn(sortOrder);

        bundlers.add(new OrderedEntityBundler(dimension, idAlias, labelAlias,
            sortOrderAlias));
    }

    private void addSimpleDimension(Dimension dimension, String label) {
        String labelAlias = appendDimColumn(label);
        bundlers.add(new SimpleBundler(dimension, labelAlias));
    }

    private void appendVisibilityFilter() {
        StringBuilder securityFilter = new StringBuilder();
        securityFilter.append("(UserDatabase.OwnerUserId = ").append(userId)
            .append(" OR ")
            .append(userId)
            .append(" in (select p.UserId from userpermission p " +
                "where p.AllowView and " +
                "p.UserId=").append(userId)
            .append(" AND p.DatabaseId = UserDatabase.DatabaseId))");

        query.whereTrue(securityFilter.toString());
    }

    private void appendDimensionRestrictions() {
        if (filter != null) {
            if (filter.getRestrictedDimensions() != null
                && filter.getRestrictedDimensions().size() > 0) {
                query.where("(");
                boolean isFirst = true;
                for (DimensionType type : filter.getRestrictedDimensions()) {
                    addJoint(query, filter.isLenient(), isFirst);

                    if (isFirst) {
                        isFirst = false;
                    }

                    if (type == DimensionType.AdminLevel) {
                        query
                            .onlyWhere(
                                baseTable
                                    .getDimensionIdColumn(DimensionType.Location))
                            .in(
                                SqlQuery
                                    .select("Link.LocationId")
                                    .from(Tables.LOCATION_ADMIN_LINK, "Link")
                                    .where("Link.AdminEntityId")
                                    .in(filter
                                        .getRestrictions(DimensionType.AdminLevel)));
                    } else {
                        query.onlyWhere(baseTable.getDimensionIdColumn(type))
                            .in(filter.getRestrictions(type));
                    }
                }
                query.onlyWhere(")");
            }
        }
    }

    private void addJoint(SqlQuery query, boolean lenient, boolean first) {
        if (!first) {
            if (lenient) {
                query.onlyWhere(" OR ");
            } else {
                query.onlyWhere(" AND ");
            }
        }
    }
}
