package org.activityinfo.shared.command.handler.pivot;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.PivotSites.PivotResult;
import org.activityinfo.shared.command.PivotSites.ValueType;
import org.activityinfo.shared.command.handler.pivot.bundler.Bundler;
import org.activityinfo.shared.command.handler.pivot.bundler.EntityBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.MonthBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.MySqlYearWeekBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.OrderedEntityBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.QuarterBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.SimpleBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.SiteCountBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.SumAndAverageBundler;
import org.activityinfo.shared.command.handler.pivot.bundler.YearBundler;
import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.report.model.AdminDimension;
import org.activityinfo.shared.report.model.AttributeGroupDimension;
import org.activityinfo.shared.report.model.DateDimension;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PivotQuery {
	private final PivotSites command;
	private final Filter filter;
	
	private final Set<Dimension> dimensions;
	private final int userId;
	private final SqlDialect dialect;
		
	private final SqlTransaction tx;
	
	private List<Bucket> buckets;	
	private final SqlQuery query = new SqlQuery();
	private final List<Bundler> bundlers = new ArrayList<Bundler>();
	
	private AsyncCallback<PivotSites.PivotResult> callback = null;
	
	private int nextColumnIndex = 1;
	
    public PivotQuery(SqlTransaction tx, SqlDialect dialect, PivotSites command, int userId) {
		super();
		this.command = command;
		this.filter = command.getFilter();
		this.dimensions = command.getDimensions();
		this.userId = userId;
		this.dialect = dialect;
		this.tx = tx;
	}
    
	public PivotQuery addTo(List<Bucket> buckets) {
		this.buckets = buckets;
		return this;
	}
    
    
    public PivotQuery callbackTo(AsyncCallback<PivotResult> callback) {
    	this.callback = callback;
    	return this;
    }
    
    private String appendColumn(String expr) {
    	String alias = "c" + (nextColumnIndex++);
    	query.appendColumn(expr, alias);
    	return alias;
    }
    
    private String appendDimColumn(String expr) {
		query.groupBy(expr);
		return appendColumn(expr);
	}

    public void queryForSumAndAverages() {
        /* We're just going to go ahead and add all the tables we need to the SQL statement;
        * this saves us some work and hopefully the SQL server will optimize out any unused
        * tables
        */
    	query.from(" indicatorvalue V " +
                "LEFT JOIN reportingperiod Period ON (Period.ReportingPeriodId=V.ReportingPeriodId) " +
                "LEFT JOIN indicator Indicator ON (Indicator.IndicatorId = V.IndicatorId) " +
                "LEFT JOIN site Site ON (Period.SiteId = Site.SiteId) " +
                "LEFT JOIN partner Partner ON (Site.PartnerId = Partner.PartnerId)" +
                "LEFT JOIN project Project ON (Site.ProjectId = Project.ProjectId) " +
                "LEFT JOIN location Location ON (Location.LocationId = Site.LocationId) " +
                "LEFT JOIN activity Activity ON (Activity.ActivityId = Site.ActivityId) " +
                "LEFT JOIN userdatabase UserDatabase ON (Activity.DatabaseId = UserDatabase.DatabaseId) ");
        /*
         * First add the indicator to the query: we can't aggregate values from different
         * indicators so this is a must
         */
    	String aggregationTypeAlias = appendColumn("Indicator.Aggregation");
    	String sumAlias = appendColumn("SUM(V.Value)");
    	String avgAlias = appendColumn("AVG(V.Value)");
    	
    	query.groupBy("V.IndicatorId");
    	query.groupBy("Indicator.Aggregation");
    	query.whereTrue(" ((V.value <> 0 and Indicator.Aggregation=0) or Indicator.Aggregation=1) ");
        
    	bundlers.add( new SumAndAverageBundler(aggregationTypeAlias, sumAlias, avgAlias) );
       
    	buildAndExecuteRestOfQuery();
    }

    public void queryForSiteCountIndicators() {

        /* We're just going to go ahead and add all the tables we need to the SQL statement;
        * this saves us some work and hopefully the SQL server will optimze out any unused
        * tables
        */

        query.from(" site Site " +
                "LEFT JOIN partner Partner ON (Site.PartnerId = Partner.PartnerId) " +
                "LEFT JOIN project Project ON (Site.ProjectId = Project.ProjectId) " +
                "LEFT JOIN location Location ON (Location.LocationId = Site.LocationId) " +
                "LEFT JOIN activity Activity ON (Activity.ActivityId = Site.ActivityId) " +
                "LEFT JOIN indicator Indicator ON (Indicator.ActivityId = Activity.ActivityId) " +
                "LEFT JOIN userdatabase UserDatabase ON (Activity.DatabaseId = UserDatabase.DatabaseId) " +
                "LEFT JOIN reportingperiod Period ON (Period.SiteId = Site.SiteId) ");

        /* First add the indicator to the query: we can't aggregate values from different
        * indicators so this is a must
        *
        */
        String count = appendColumn("COUNT(DISTINCT Site.SiteId)");
        query.groupBy("Indicator.IndicatorId");
        query.whereTrue("Indicator.Aggregation=2 ");

        bundlers.add(new SiteCountBundler(count));

        buildAndExecuteRestOfQuery();
    }
    
    public void queryForTotalSiteCounts() {
        query.from(" site Site " +
                "LEFT JOIN partner Partner ON (Site.PartnerId = Partner.PartnerId) " +
                "LEFT JOIN project Project ON (Site.ProjectId = Project.ProjectId) " +
                "LEFT JOIN location Location ON (Location.LocationId = Site.LocationId) " +
                "LEFT JOIN activity Activity ON (Activity.ActivityId = Site.ActivityId) " +
                "LEFT JOIN userdatabase UserDatabase ON (Activity.DatabaseId = UserDatabase.DatabaseId) " +
                "LEFT JOIN reportingperiod Period ON (Period.SiteId = Site.SiteId) ");

        /* First add the indicator to the query: we can't aggregate values from different
        * indicators so this is a must
        *
        */
        String count = appendColumn("COUNT(DISTINCT Site.SiteId)");

        bundlers.add(new SiteCountBundler(count));

        buildAndExecuteRestOfQuery();
    }

    protected void buildAndExecuteRestOfQuery() {
        /* Now add any other dimensions  */
        for (Dimension dimension : dimensions) {

        	if (dimension == null) {
        		Log.error("NULL dimension provided to pivot query: dimensions = " + dimensions);
        		
        	} else if (dimension.getType() == DimensionType.Activity) {
            	addOrderedEntityDimension(dimension, "Site.ActivityId", "Activity.Name", "Activity.SortOrder");

            } else if (dimension.getType() == DimensionType.ActivityCategory) {
                addSimpleDimension(dimension, "Activity.Category");

            } else if (dimension.getType() == DimensionType.Database) {
            	addEntityDimension(dimension, "Activity.DatabaseId", "UserDatabase.Name");

            } else if (dimension.getType() == DimensionType.Partner) {
            	addEntityDimension(dimension, "Site.PartnerId", "Partner.Name");
                
            } else if (dimension.getType() == DimensionType.Project) {
            	addEntityDimension(dimension, "Site.ProjectId", "Project.Name");
              
            } else if (dimension.getType() == DimensionType.Location) {
            	addEntityDimension(dimension, "Site.LocationId", "Location.Name");

            } else if (dimension.getType() == DimensionType.Indicator) {
            	addOrderedEntityDimension(dimension, "Indicator.IndicatorId", "Indicator.Name", "Indicator.SortOrder");

            } else if (dimension.getType() == DimensionType.IndicatorCategory) {
            	addSimpleDimension(dimension, "Indicator.Category");

            } else if (dimension instanceof DateDimension) {
                DateDimension dateDim = (DateDimension) dimension;

                if (dateDim.getUnit() == DateUnit.YEAR) {
                	String yearAlias = appendDimColumn(dialect.yearFunction("Period.Date2"));

                    bundlers.add(new YearBundler(dimension, yearAlias));

                } else if (dateDim.getUnit() == DateUnit.MONTH) {
                	String yearAlias = appendDimColumn(dialect.yearFunction("Period.Date2"));
                	String monthAlias = appendDimColumn(dialect.monthFunction("Period.Date2"));
                	
                    bundlers.add(new MonthBundler(dimension, yearAlias, monthAlias));

                } else if (dateDim.getUnit() == DateUnit.QUARTER) {
                	String yearAlias = appendDimColumn(dialect.yearFunction("Period.Date2"));
                	String quarterAlias = appendDimColumn(dialect.quarterFunction("Period.Date2"));

                	bundlers.add(new QuarterBundler(dimension, yearAlias, quarterAlias));
                    nextColumnIndex += 2;
                } else if (dateDim.getUnit() == DateUnit.WEEK_MON) {
                	// Mode = 3 means " Monday	1-53	with more than 3 days this year" 
                	// see http://dev.mysql.com/doc/refman/5.5/en/date-and-time-functions.html#function_week
                	if(dialect.isMySql()) {
	                	String weekAlias = appendDimColumn("YEARWEEK(Period.Date2, 3)");
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
                        .append(" ON (Location.LocationId=").append(tableAlias).append(".LocationId)").toString());

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
            		.on("Site.SiteId=" + valueQueryAlias + ".SiteId");
            	
            	// now we need the names of the attributes we've just selected
            	query.leftJoin("attribute", labelQueryAlias)
            		.on(valueQueryAlias + ".AttributeId=" + labelQueryAlias + ".AttributeId");

            	addEntityDimension(attrGroupDim, valueQueryAlias + ".AttributeId", labelQueryAlias + ".Name");
            }
        }


        /* And start on our where clause... */

        query.where("Site.dateDeleted").isNull();
        query.where("Activity.dateDeleted").isNull();
        query.where("UserDatabase.dateDeleted").isNull();
        if(command.getValueType() == ValueType.INDICATOR) {
        	query.where("Indicator.dateDeleted").isNull();
        }
        
        // and only allow results that are visible to this user.
        if(!GWT.isClient()) {
        	appendVisibilityFilter();
        }


        if (filter.getMinDate() != null) {
            query.where("Period.date2").greaterThanOrEqualTo(filter.getMinDate());
        }
        if (filter.getMaxDate() != null) {
        	query.where("Period.date2").lessThanOrEqualTo(filter.getMaxDate());
        }

        appendDimensionRestrictions();

        
        query.execute(tx, new SqlResultCallback() {
			
			@Override
			public void onSuccess(SqlTransaction tx, SqlResultSet results) {
				for(SqlResultSetRow row : results.getRows()) {
					Bucket bucket = new Bucket();
                    for (Bundler bundler : bundlers) {
                        bundler.bundle(row, bucket);
                    }
                    buckets.add(bucket);
				}
                if(callback != null) {
                	callback.onSuccess(new PivotResult(buckets));
                }
			}
		});    
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
            if (type == DimensionType.Indicator) {
            	query.where("Indicator.IndicatorId").in(filter.getRestrictions(DimensionType.Indicator));
            } else if (type == DimensionType.Activity) {
            	query.where("Site.ActivityId").in(filter.getRestrictions(DimensionType.Activity));
            } else if (type == DimensionType.Database) {
            	query.where("Activity.DatabaseId").in(filter.getRestrictions(DimensionType.Database));
            } else if (type == DimensionType.Partner) {
            	query.where("Site.PartnerId").in(filter.getRestrictions(DimensionType.Partner));
            } else if (type == DimensionType.Project) {
            	query.where("Site.ProjectId").in(filter.getRestrictions(DimensionType.Project));
            } else if (type == DimensionType.Location) {
            	query.where("Site.LocationId").in(filter.getRestrictions(DimensionType.Location));
            } else if (type == DimensionType.AdminLevel) {
            	query.where("Site.LocationId").in(
            			SqlQuery.select("Link.LocationId").from(Tables.LOCATION_ADMIN_LINK, "Link")
            				.where("Link.AdminEntityId").in(filter.getRestrictions(DimensionType.AdminLevel)));
            }
        }
    }
}
