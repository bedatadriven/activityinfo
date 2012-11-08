package org.activityinfo.shared.command.handler.pivot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activityinfo.client.Log;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.handler.pivot.bundler.Bundler;
import org.activityinfo.shared.command.handler.pivot.bundler.EntityBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.MonthBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.MySqlYearWeekBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.OrderedEntityBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.QuarterBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.SimpleBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.YearBundler;
import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.report.content.DimensionCategory;
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
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PivotQuery {
	private final Filter filter;
	private final Set<Dimension> dimensions;

	private final BaseTable baseTable;

	private final int userId;
	private final SqlDialect dialect;
		
	private final SqlTransaction tx;
	
	private final SqlQuery query = new SqlQuery();
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
	}
    
    private String appendDimColumn(String expr) {
		String alias = expr.replace('.', '_');
		return appendDimColumn(alias, expr);
	}

	private String appendDimColumn(String alias, String expr) {
		if(!dimColumns.contains(alias)) {
			query.groupBy(expr);
			query.appendColumn(expr, alias);
			dimColumns.add(alias);
		}
		return alias;
	}
    
	public void execute(final AsyncCallback<Void> callback) {
		
		baseTable.setupQuery(command, query);
		
		if(command.isPivotedBy(DimensionType.Location)) {
			query.leftJoin(Tables.LOCATION, "Location").on("Location.LocationId=" + 
					baseTable.getDimensionIdColumn(DimensionType.Location));
		}
		if(command.isPivotedBy(DimensionType.Partner)) {
			query.leftJoin(Tables.PARTNER, "Partner").on("Partner.PartnerId=" + 
					baseTable.getDimensionIdColumn(DimensionType.Partner));

		}
		if(command.isPivotedBy(DimensionType.Project)) {
			query.leftJoin(Tables.PROJECT, "Project").on("Project.ProjectId=" + 
					baseTable.getDimensionIdColumn(DimensionType.Project));
		}
		
		
        addDimensionBundlers();
               
        // and only allow results that are visible to this user.
        if (!GWT.isClient()) {
        	appendVisibilityFilter();
        }


        if (filter.getMinDate() != null) {
            query.where(baseTable.getDateCompleteColumn()).greaterThanOrEqualTo(filter.getMinDate());
        }
        if (filter.getMaxDate() != null) {
        	query.where(baseTable.getDateCompleteColumn()).lessThanOrEqualTo(filter.getMaxDate());
        }

        appendDimensionRestrictions();
                
        query.execute(tx, new SqlResultCallback() {
			
			@Override
			public void onSuccess(SqlTransaction tx, SqlResultSet results) {
				for(SqlResultSetRow row : results.getRows()) {
					Bucket bucket = new Bucket();
					bucket.setAggregationMethod(row.getInt(ValueFields.AGGREGATION));
					bucket.setCount(row.getInt(ValueFields.COUNT));
					if(!row.isNull(ValueFields.SUM)) {
						bucket.setSum(row.getInt(ValueFields.SUM));
					}
					bucket.setCategory(new Dimension(DimensionType.Target), baseTable.getTargetCategory());
					
                    for (Bundler bundler : bundlers) {
                        bundler.bundle(row, bucket);
                    }
                    context.addBucket(bucket);
				}
				
				callback.onSuccess(null);
			}
		});    
    }

	private void addDimensionBundlers() {
		/* Now add any other dimensions  */
        for (Dimension dimension : dimensions) {

        	if (dimension == null) {
        		Log.error("NULL dimension provided to pivot query: dimensions = " + dimensions);
        		
        	} else if (dimension.getType() == DimensionType.Activity) {
            	addOrderedEntityDimension(dimension, "Activity.ActivityId", "Activity.Name", "Activity.SortOrder");

            } else if (dimension.getType() == DimensionType.ActivityCategory) {
                addSimpleDimension(dimension, "Activity.Category");

            } else if (dimension.getType() == DimensionType.Database) {
            	addEntityDimension(dimension, "Activity.DatabaseId", "UserDatabase.Name");

            } else if (dimension.getType() == DimensionType.Partner) {
				addEntityDimension(dimension, "Partner.PartnerId", "Partner.Name");
                
            } else if (dimension.getType() == DimensionType.Project) {
				addEntityDimension(dimension, "Project.ProjectId", "Project.Name");
              
            } else if (dimension.getType() == DimensionType.Location) {
            	addEntityDimension(dimension, "Site.LocationId", "Location.Name");

            } else if (dimension.getType() == DimensionType.Indicator) {
            	addOrderedEntityDimension(dimension, "Indicator.IndicatorId", "Indicator.Name", "Indicator.SortOrder");

            } else if (dimension.getType() == DimensionType.IndicatorCategory) {
            	addSimpleDimension(dimension, "Indicator.Category");

            } else if (dimension instanceof DateDimension) {
                DateDimension dateDim = (DateDimension) dimension;

                if (dateDim.getUnit() == DateUnit.YEAR) {
                	String yearAlias = appendDimColumn("year", dialect.yearFunction(baseTable.getDateCompleteColumn()));

                    bundlers.add(new YearBundler(dimension, yearAlias));

                } else if (dateDim.getUnit() == DateUnit.MONTH) {
                	String yearAlias = appendDimColumn("year", dialect.yearFunction(baseTable.getDateCompleteColumn()));
                	String monthAlias = appendDimColumn("month", dialect.monthFunction(baseTable.getDateCompleteColumn()));
                	
                    bundlers.add(new MonthBundler(dimension, yearAlias, monthAlias));

                } else if (dateDim.getUnit() == DateUnit.QUARTER) {
                	String yearAlias = appendDimColumn("year", dialect.yearFunction(baseTable.getDateCompleteColumn()));
                	String quarterAlias = appendDimColumn("quarter", dialect.quarterFunction(baseTable.getDateCompleteColumn()));

                	bundlers.add(new QuarterBundler(dimension, yearAlias, quarterAlias));
                } else if (dateDim.getUnit() == DateUnit.WEEK_MON) {
                	// Mode = 3 means " Monday	1-53	with more than 3 days this year" 
                	// see http://dev.mysql.com/doc/refman/5.5/en/date-and-time-functions.html#function_week
                	if (dialect.isMySql()) {
	                	String weekAlias = appendDimColumn("yearweek", "YEARWEEK(" + baseTable.getDateCompleteColumn() + ", 3)");
	                	bundlers.add(new MySqlYearWeekBundler(dimension, weekAlias));
                	}
                	// TODO: sqlite
                }

            } else if (dimension instanceof AdminDimension) {
                AdminDimension adminDim = (AdminDimension) dimension;

                String tableAlias = "AdminLevel" + adminDim.getLevelId();

                query.from(new StringBuilder(" LEFT JOIN " +
                        "(SELECT L.LocationId, E.AdminEntityId, E.Name " +
                        "FROM locationadminlink L " +
                        "LEFT JOIN adminentity E ON (L.AdminEntityId=E.AdminEntityID) " +
                        "WHERE E.AdminLevelId=").append(adminDim.getLevelId())
                        .append(") AS ").append(tableAlias)
                        .append(" ON (").append(baseTable.getDimensionIdColumn(DimensionType.Location))
                        .append(" =").append(tableAlias).append(".LocationId)").toString());

                addEntityDimension(dimension, tableAlias + ".AdminEntityId", tableAlias + ".Name");
                
            
            } else if (dimension instanceof AttributeGroupDimension) {
            	// this pivots the data by a single-valued attribute group
            	
            	AttributeGroupDimension attrGroupDim = (AttributeGroupDimension) dimension;
            	
            	String valueQueryAlias = "attributeValues" + attrGroupDim.getAttributeGroupId();
            	String labelQueryAlias = "attributeLabels" + attrGroupDim.getAttributeGroupId();
            	
            	// this first query gives us the single chosen attribute for each 
            	// site, arbitrarily taking the attribute with the minimum id if more
            	// than one attribute has been selected (i.e db is inconsistent)
            	SqlQuery derivedValueQuery = SqlQuery.select()
            		.appendColumn("v.siteId", "siteId")
            		.appendColumn("min(v.attributeId)", "attributeId")
            		.from("attributevalue", "v")
            		.leftJoin("attribute", "a").on("v.AttributeId = a.AttributeId")
            		.whereTrue("v.value=1")
            		.whereTrue("a.attributeGroupId=" + attrGroupDim.getAttributeGroupId())
            		.groupBy("v.siteId");
            	
            	query.leftJoin(derivedValueQuery, valueQueryAlias)
            		.on(baseTable.getDimensionIdColumn(DimensionType.Site) + "="+ valueQueryAlias + ".SiteId");
            	
            	// now we need the names of the attributes we've just selected
            	query.leftJoin("attribute", labelQueryAlias)
            		.on(valueQueryAlias + ".AttributeId=" + labelQueryAlias + ".AttributeId");

            	addEntityDimension(attrGroupDim, valueQueryAlias + ".AttributeId", labelQueryAlias + ".Name");
            }
        }
	}


	private void addEntityDimension(Dimension dimension, String id, String label) {
		String idAlias = appendDimColumn(id);
		String labelAlias = appendDimColumn(label);
		
		bundlers.add(new EntityBundler(dimension, idAlias, labelAlias));
	}

	private void addOrderedEntityDimension(Dimension dimension, String id, String label, String sortOrder) {
		String idAlias = appendDimColumn(id);
		String labelAlias = appendDimColumn(label);
		String sortOrderAlias = appendDimColumn(sortOrder);
		
		bundlers.add(new OrderedEntityBundler(dimension, idAlias, labelAlias, sortOrderAlias));
	}

	private void addSimpleDimension(Dimension dimension, String label) {
		String labelAlias = appendDimColumn(label);
		bundlers.add(new SimpleBundler(dimension, labelAlias));
	}	

	private void appendVisibilityFilter() {
        StringBuilder securityFilter = new StringBuilder();
        securityFilter.append("(UserDatabase.OwnerUserId = ").append(userId).append(" OR ")
             .append(userId).append(" in (select p.UserId from userpermission p " +
                "where p.AllowView and " +
                "p.UserId=").append(userId).append(" AND p.DatabaseId = UserDatabase.DatabaseId))");
        
        query.whereTrue(securityFilter.toString());
    }

    private void appendDimensionRestrictions() {
        for (DimensionType type : filter.getRestrictedDimensions()) {
        	if (type == DimensionType.AdminLevel) {
            	query.where(baseTable.getDimensionIdColumn(DimensionType.Location)).in(
            			SqlQuery.select("Link.LocationId").from(Tables.LOCATION_ADMIN_LINK, "Link")
            				.where("Link.AdminEntityId").in(filter.getRestrictions(DimensionType.AdminLevel)));
            } else {
            	query.where(baseTable.getDimensionIdColumn(type)).in(filter.getRestrictions(type));
            }
        }
    }

}
