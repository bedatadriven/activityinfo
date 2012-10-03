package org.activityinfo.shared.command.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activityinfo.client.Log;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class GetSitesHandler implements CommandHandlerAsync<GetSites, SiteResult> {

	private final SqlDialect dialect;
		
	@Inject
	public GetSitesHandler(SqlDialect dialect) {
		super();
		this.dialect = dialect;
	}

	@Override
	public void execute(final GetSites command, final ExecutionContext context,
			final AsyncCallback<SiteResult> callback) {

		Log.trace("Entering execute()");
		doQuery(command, context, callback);
	}

	private void doQuery(final GetSites command, final ExecutionContext context,
			final AsyncCallback<SiteResult> callback) {
		
		// in order to pull in the linked queries, we want to 
		// to create two queries that we union together. 
		
		// for performance reasons, we want to apply all of the joins
		// and filters on both parts of the union query
				
		SqlQuery unioned = unionedQuery(context, command);
		unioned.appendAllColumns();
		applySort(unioned, command.getSortInfo());
		
		if(isMySql()) {
			// with this feature, MySQL will keep track of the total
			// number of rows regardless of our limit statement.
			// This way we don't have to execute the query twice to
			// get the total count
			// 
			// unfortunately, this is not available on sqlite
			unioned.appendKeyword("SQL_CALC_FOUND_ROWS");
		}
		
		applyPaging(unioned, command);
		applySort(unioned, command.getSortInfo());
		
		final Multimap<Integer, SiteDTO> siteMap = HashMultimap.create();
		final List<SiteDTO> sites = new ArrayList<SiteDTO>();
		
		final SiteResult result = new SiteResult(sites);
		result.setOffset(command.getOffset());
		
		Log.trace("About to execute primary query");

		unioned.execute(context.getTransaction(), new SqlResultCallback() {
			@Override
			public void onSuccess(SqlTransaction tx, SqlResultSet results) {

				if(command.getLimit() <= 0) {
					result.setTotalLength(results.getRows().size());
				} else {
					queryTotalLength(tx, command, context, result);
				} 
				
				Log.trace("Primary query returned, starting to add to map");

				for(SqlResultSetRow row : results.getRows()) {
					SiteDTO site = toSite(row);
					sites.add(site);
					siteMap.put(site.getId(), site);
				}

				Log.trace("Finished adding to map");

				if(!sites.isEmpty()) {
					if(command.isFetchAdminEntities()) {
						joinEntities(tx, siteMap);
					}
					if(command.fetchAnyIndicators()) {
						joinIndicatorValues(command, tx, siteMap);
					}
					if(command.isFetchAttributes()) {
						joinAttributeValues(tx, siteMap);
					}
				}
				callback.onSuccess(result);
			}
		});
	}

	private SqlQuery unionedQuery(ExecutionContext context, GetSites command) {
		
		SqlQuery primaryQuery = primaryQuery(context, command);
		SqlQuery linkedQuery = linkedQuery(context, command);
		
		SqlQuery unioned = SqlQuery.select()
			.from(unionAll(primaryQuery, linkedQuery), "u");
		for(Object param : primaryQuery.parameters()) {
			unioned.appendParameter(param);
		}
		for(Object param : linkedQuery.parameters()) {
			unioned.appendParameter(param);
		}
		return unioned;
	}

	private String unionAll(SqlQuery primaryQuery, SqlQuery linkedQuery) {
		StringBuilder union = new StringBuilder();
		union.append("(")
			 .append(primaryQuery.sql())
			 .append(" UNION ALL ")
			 .append(linkedQuery.sql())
			 .append(")");
		return union.toString();
	}

	private SqlQuery primaryQuery(ExecutionContext context, GetSites command) {
		SqlQuery query = SqlQuery.select()
		.appendColumn("site.SiteId")
		.appendColumn("(0)", "Linked")
		.appendColumn("activity.ActivityId")
		.appendColumn("activity.name", "ActivityName")
		.appendColumn("db.DatabaseId", "DatabaseId")
		.appendColumn("site.Date1", "Date1")
		.appendColumn("site.Date2", "Date2")
		.appendColumn("partner.PartnerId", "PartnerId")
		.appendColumn("partner.name", "PartnerName")
		.appendColumn("site.projectId", "ProjectId")
		.appendColumn("project.name", "ProjectName")
		.appendColumn("location.locationId", "LocationId")
		.appendColumn("location.name", "LocationName")
		.appendColumn("location.axe", "LocationAxe")
		.appendColumn("locationType.name", "LocationTypeName")
		.appendColumn("site.comments", "Comments")
		.appendColumn("location.x", "x")
		.appendColumn("location.y", "y")
		.from(Tables.SITE)
		.whereTrue("site.dateDeleted is null")
		.leftJoin(Tables.ACTIVITY).on("site.ActivityId = activity.ActivityId")
			.leftJoin(Tables.USER_DATABASE, "db").on("activity.DatabaseId = db.DatabaseId")
			.leftJoin(Tables.LOCATION).on("site.LocationId = location.LocationId")
			.leftJoin(Tables.LOCATION_TYPE, "locationType").on("location.LocationTypeId = locationType.LocationTypeId")
			.leftJoin(Tables.PARTNER).on("site.PartnerId = partner.PartnerId")
			.leftJoin(Tables.PROJECT).on("site.ProjectId = project.ProjectId");
				
		applyPermissions(query, context);
		applyFilter(query, command.getFilter());
		
		if(command.getFilter().isRestricted(DimensionType.Indicator)) {
			applyPrimaryIndicatorFilter(query, command.getFilter());
		}
		
		return query;
	}
	
	private SqlQuery linkedQuery(ExecutionContext context, GetSites command) {
		SqlQuery query = SqlQuery.select()
			.appendColumn("DISTINCT site.SiteId", "SiteId")
			.appendColumn("1", "Linked")
			.appendColumn("activity.ActivityId")
			.appendColumn("activity.name", "ActivityName")
			.appendColumn("db.DatabaseId", "DatabaseId")
			.appendColumn("site.Date1", "Date1")
			.appendColumn("site.Date2", "Date2")
			.appendColumn("partner.PartnerId", "PartnerId")
			.appendColumn("partner.name", "PartnerName")
			.appendColumn("site.projectId", "ProjectId")
			.appendColumn("project.name", "ProjectName")
			.appendColumn("location.locationId", "LocationId")
			.appendColumn("location.name", "LocationName")
			.appendColumn("location.axe", "LocationAxe")
			.appendColumn("locationType.name", "LocationTypeName")
			.appendColumn("site.comments", "Comments")
			.appendColumn("location.x", "x")
			.appendColumn("location.y", "y");
		
		if(command.getFilter().isRestricted(DimensionType.Indicator)) {
			/*
			 * When filtering by indicators, restructure the query to fetch
			 * the results more efficiently
			 */
			query
			.from(Tables.INDICATOR_LINK, "link")
			.innerJoin(Tables.INDICATOR_VALUE, "siv").on("link.SourceIndicatorId = siv.IndicatorId")
			.innerJoin(Tables.REPORTING_PERIOD, "srp").on("siv.ReportingPeriodId = srp.ReportingPeriodId")
			.innerJoin(Tables.SITE, "site").on("srp.SiteId=site.SiteId")
			.innerJoin(Tables.INDICATOR, "di").on("link.DestinationIndicatorId=di.IndicatorId")
			.innerJoin(Tables.ACTIVITY, "activity").on("di.ActivityId=activity.ActivityId")
			.where("link.DestinationIndicatorId").in(command.getFilter().getRestrictions(DimensionType.Indicator));
		} else { 
			query
			.from(Tables.SITE)
			.innerJoin(Tables.INDICATOR, "si").on("si.activityid=site.activityid")
			.innerJoin(Tables.INDICATOR_LINK, "link").on("si.indicatorId=link.sourceindicatorid")
			.innerJoin(Tables.INDICATOR, "di").on("link.destinationIndicatorId=di.indicatorid")
			.leftJoin(Tables.ACTIVITY).on("di.ActivityId = activity.ActivityId");
		}
		query
			.leftJoin(Tables.USER_DATABASE, "db").on("activity.DatabaseId = db.DatabaseId")
			.leftJoin(Tables.LOCATION).on("site.LocationId = location.LocationId")
			.leftJoin(Tables.LOCATION_TYPE, "locationType").on("location.LocationTypeId = locationType.LocationTypeId")
			.leftJoin(Tables.PARTNER).on("site.PartnerId = partner.PartnerId")
			.leftJoin(Tables.PROJECT).on("site.ProjectId = project.ProjectId")
			.whereTrue("site.dateDeleted is null");
					
		applyPermissions(query, context);
		applyFilter(query, command.getFilter());
		
		return query;
	}

	private void applyPaging(final SqlQuery query, GetSites command) {
		if(command.getOffset() > 0 || command.getLimit() > 0) {
		    query.setLimitClause(dialect.limitClause(
		    		command.getOffset(),
		    		command.getLimit()));
		}
	}

	private void applyPermissions(final SqlQuery query, ExecutionContext context) {
		// Apply permissions if we are on the server, 
		// otherwise permissions have already been taken into account
		// during synchronization

		if(!GWT.isClient()) {    
			query.whereTrue("activity.DateDeleted IS NULL")
				.and("db.DateDeleted IS NULL");
			query.whereTrue(
					"(db.OwnerUserId = ? OR " +
							"db.DatabaseId in "  +
							"(SELECT p.DatabaseId from userpermission p where p.UserId = ? and p.AllowViewAll) or " +
							"db.DatabaseId in " +
					"(select p.DatabaseId from userpermission p where (p.UserId = ?) and p.AllowView and p.PartnerId = site.PartnerId) " +
					" OR (select count(*) from activity pa where pa.published>0 and pa.ActivityId = site.ActivityId) > 0 )");
			
			query.appendParameter(context.getUser().getId());
			query.appendParameter(context.getUser().getId());
			query.appendParameter(context.getUser().getId());
		}
	}

	private void applySort(SqlQuery query, SortInfo sortInfo) {
        if (sortInfo.getSortDir() != SortDir.NONE) {
            String field = sortInfo.getSortField();
            boolean ascending = sortInfo.getSortDir() == SortDir.ASC;
  
            if (field.equals("date1")) {
            	query.orderBy("Date1", ascending);
            } else if (field.equals("date2")) {
            	query.orderBy("Date2", ascending);
            } else if (field.equals("locationName")) {
            	query.orderBy("LocationName", ascending);
            } else if (field.equals("partner")) {
            	query.orderBy("PartnerName", ascending);
            } else if (field.equals("locationAxe")) {
            	query.orderBy("LocationAxe", ascending);
            } else if (field.startsWith(IndicatorDTO.PROPERTY_PREFIX)) {
            	int indicatorId = IndicatorDTO.indicatorIdForPropertyName(field);
            	query.orderBy(SqlQuery.selectSingle("SUM(v.Value)")
    				.from(Tables.INDICATOR_VALUE, "v")
    				.leftJoin(Tables.REPORTING_PERIOD, "r").on("v.ReportingPeriodId=r.ReportingPeriodId")
    				.whereTrue("v.IndicatorId=" + indicatorId)
    				.and("r.SiteId=u.SiteId"), ascending);
            } else {
            	Log.error("Unimplemented sort on GetSites: '" + field + "");
            }
        }
	}
	
	private void applyFilter(SqlQuery query, Filter filter) {
		if(filter != null) {
	        for (DimensionType type : filter.getRestrictedDimensions()) {
	            if (type == DimensionType.Activity) {
	                query.where("activity.ActivityId").in(filter.getRestrictions(type));
	
	            } else if (type == DimensionType.Database) {
	                query.where("activity.DatabaseId").in(filter.getRestrictions(type));
	
	            } else if (type == DimensionType.Partner) {
	                query.where("site.PartnerId").in(filter.getRestrictions(type));
	                
	            } else if (type == DimensionType.Project) {
	            	query.where("site.ProjectId").in(filter.getRestrictions(type));
	
	            } else if (type == DimensionType.AdminLevel) {
	                query.where("site.LocationId").in(
	                        SqlQuery.select("Link.LocationId")
	                        	.from(Tables.LOCATION_ADMIN_LINK, "Link")
	                        	.where("Link.AdminEntityId")
	                            .in(filter.getRestrictions(type)));
	
	            } else if(type == DimensionType.Site) {
	                query.where("site.SiteId").in(filter.getRestrictions(type));
	            }
	        }
	        if (filter.getMinDate() != null) {
	        	query.where("site.Date2").greaterThanOrEqualTo(filter.getMinDate());
	        }
	        if (filter.getMaxDate() != null) {
	        	query.where("site.Date2").lessThanOrEqualTo(filter.getMaxDate());
	        }
		}
	}

	private void applyPrimaryIndicatorFilter(SqlQuery query, Filter filter) {
		SqlQuery subQuery = new SqlQuery()
			.appendColumn("period.SiteId")
			.from(Tables.INDICATOR_VALUE, "iv")
			.leftJoin(Tables.REPORTING_PERIOD, "period").on("iv.ReportingPeriodId=period.ReportingPeriodId")
			.where("iv.IndicatorId").in(filter.getRestrictions(DimensionType.Indicator))
			.whereTrue("iv.Value IS NOT NULL");
		
		query.where("site.SiteId").in(subQuery);
	}
	
	private void queryTotalLength(SqlTransaction tx, GetSites command, ExecutionContext context, final SiteResult result) {
		
		if(isMySql()) {
			tx.executeSql("SELECT FOUND_ROWS() site_count", new SqlResultCallback() {

				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					result.setTotalLength(results.getRow(0).getInt("site_count"));
				}
			});
		} else {
			// otherwise we have to execute the whole thing again
			SqlQuery query = countQuery(command, context);
			query.execute(tx, new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					result.setTotalLength(results.getRow(0).getInt("site_count"));
				}
			});
		}
	}

	private SqlQuery countQuery(GetSites command, ExecutionContext context) {
		SqlQuery unioned = unionedQuery(context, command);
		unioned.appendColumn("count(*)", "site_count");
		return unioned;
	}
	
	private void joinEntities(SqlTransaction tx,
			final Multimap<Integer, SiteDTO> siteMap) {
		
		Log.trace("Starting joinEntities()");
		
	    SqlQuery.select(
	    		"site.SiteId", 
	    		"Link.adminEntityId",
	    		"e.name",
	    		"e.adminLevelId",
				"e.adminEntityParentId",
				"x1","y1","x2","y2")
	        .from(Tables.SITE)
	        .innerJoin(Tables.LOCATION).on("location.LocationId = site.LocationId")
	        .innerJoin(Tables.LOCATION_ADMIN_LINK, "Link").on("Link.LocationId = location.LocationId")
	        .innerJoin(Tables.ADMIN_ENTITY, "e").on("Link.AdminEntityId = e.AdminEntityId")
	        .where("site.SiteId").in(siteMap.keySet())
	        .execute(tx, new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {

					Log.trace("Received results for joinEntities()");

					Map<Integer, AdminEntityDTO> entities = Maps.newHashMap();
					
					for(SqlResultSetRow row : results.getRows()) {
						
						int adminEntityId = row.getInt("adminEntityId");
						AdminEntityDTO entity = entities.get(adminEntityId);
						if(entity == null) {
							entity = GetAdminEntitiesHandler.toEntity(row);
							entities.put(adminEntityId, entity);
						}
						
						for(SiteDTO site : siteMap.get(row.getInt("SiteId"))) {
							site.setAdminEntity(entity.getLevelId(), entity);
						}
					}	
					
					Log.trace("Done populating results for joinEntities");
				}
			});
	}	
	
	private void joinIndicatorValues(GetSites command, SqlTransaction tx, final Multimap<Integer, SiteDTO> siteMap) {

		Log.trace("Starting joinIndicatorValues()");
		
    	SqlQuery query = SqlQuery.select()
    		.appendColumn("P.SiteId", "SiteId")
    		.appendColumn("V.IndicatorId", "SourceIndicatorId")
    		.appendColumn("I.ActivityId", "SourceActivityId")
    		.appendColumn("D.IndicatorId", "DestIndicatorId")
    		.appendColumn("D.ActivityId", "DestActivityId")
    		.appendColumn("V.Value")
            .from(Tables.REPORTING_PERIOD, "P")
            	.innerJoin(Tables.INDICATOR_VALUE, "V").on("P.ReportingPeriodId = V.ReportingPeriodId")
            	.innerJoin(Tables.INDICATOR, "I").on("I.IndicatorId = V.IndicatorId")
            	.leftJoin(Tables.INDICATOR_LINK, "L").on("L.SourceIndicatorId=I.IndicatorId")
            	.leftJoin(Tables.INDICATOR, "D").on("L.DestinationIndicatorId=D.IndicatorId")
            .where("P.SiteId").in(siteMap.keySet())
            .and("I.dateDeleted IS NULL");
    	
    	
    	Log.info(query.toString());
    	
        query.execute(tx, new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					Log.trace("Received results for join indicators");

					
					for(SqlResultSetRow row : results.getRows()) {
						double indicatorValue = row.getDouble("Value");
						int sourceActivityid = row.getInt("SourceActivityId");

						for(SiteDTO site : siteMap.get( row.getInt("SiteId") )) {
							if(sourceActivityid == site.getActivityId()) {
								int indicatorId = row.getInt("SourceIndicatorId");
								site.setIndicatorValue(indicatorId, indicatorValue);
							} else if(!row.isNull("DestActivityId")) {
								int destActivityId = row.getInt("DestActivityId");
								if(site.getActivityId() == destActivityId) {
									int indicatorId = row.getInt("DestIndicatorId");
									site.setIndicatorValue(indicatorId, indicatorValue);
								}
							}
						}
					}	
					Log.trace("Done populating dtos for join indicators");

				}
			});
	}
	
	private void joinAttributeValues(SqlTransaction tx, final Multimap<Integer, SiteDTO> siteMap) {

		Log.trace("Starting joinAttributeValues() ");

    	SqlQuery.select()
    		.appendColumn("AttributeId", "attributeId")
    		.appendColumn("SiteId", "siteId")
    		.appendColumn("Value", "value")
            .from(Tables.ATTRIBUTE_VALUE)
            .where("SiteId").in(siteMap.keySet())
            .execute(tx, new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					Log.trace("Received results for joinAttributeValues() ");
					
					for(SqlResultSetRow row : results.getRows()) {
						int attributeId = row.getInt("attributeId");
			            boolean value = row.getBoolean("value");

			            for(SiteDTO site : siteMap.get( row.getInt("siteId") )) {
							site.setAttributeValue(attributeId, value);
						}
					}		

					Log.trace("Done populating results for joinAttributeValues()");
				}
			});
	}

	private SiteDTO toSite(SqlResultSetRow row) {
	    SiteDTO model = new SiteDTO();
        model.setId( row.getInt("SiteId") );
        model.setLinked( row.getBoolean("Linked"));
        model.setActivityId( row.getInt("ActivityId") );
        model.setDate1( row.getDate("Date1") );
        model.setDate2( row.getDate("Date2") );
        model.setLocationId(row.getInt("LocationId"));
        model.setLocationName( row.getString("LocationName") );
        model.setLocationAxe( row.getString("LocationAxe") );
        
        if(!row.isNull("x") && !row.isNull("y")) {
        	model.setX(row.getDouble("x"));
        	model.setY(row.getDouble("y"));
        }
        model.setComments( row.getString("Comments") );

        PartnerDTO partner = new PartnerDTO();
        partner.setId( row.getInt("PartnerId"));
        partner.setName( row.getString("PartnerName") );
        
        if( !row.isNull("ProjectId") ) {
        	ProjectDTO project = new ProjectDTO();
        	project.setId( row.getInt("ProjectId") );
        	project.setName( row.getString("ProjectName") );
        	model.setProject(project);
        }
        
        model.setPartner(partner);
        return model;
	}
	
	private boolean isMySql() {
		return dialect.isMySql();
	}
	
}
