package org.sigmah.shared.command.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.shared.command.Filter;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.report.model.DimensionType;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class GetSitesHandler implements CommandHandlerAsync<GetSites, SiteResult> {

	private final SqlDatabase database;
	
	@Inject
	public GetSitesHandler(SqlDatabase database) {
		super();
		this.database = database;
	}

	@Override
	public void execute(final GetSites command, final ExecutionContext context,
			final AsyncCallback<SiteResult> callback) {

		doQuery(command, context, callback);
	}

	private void calculatePage(GetSites command, ExecutionContext context,
			AsyncCallback<SiteResult> callback) {

		final SqlQuery query = SqlQuery.select("Site.SiteId");
		
		applyJoins(query);
		applyFilter(query, command.getFilter());
		applyPermissions(query, context);
		applySort(query, command.getSortInfo());
		
	}

	private void doQuery(final GetSites command, final ExecutionContext context,
			final AsyncCallback<SiteResult> callback) {
		final SqlQuery query = SqlQuery.select("site.SiteId")
			.appendColumn("site.Linked", "Linked")
			.appendColumn("activity.ActivityId")
			.appendColumn("activity.name", "ActivityName")
			.appendColumn("UserDatabase.DatabaseId", "DatabaseId")
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
		
		applyJoins(query);		
		applyPermissions(query, context);
		applySort(query, command.getSortInfo());
		applyFilter(query, command.getFilter());
		applyPaging(query, command);

		final Multimap<Integer, SiteDTO> siteMap = HashMultimap.create();
		final List<SiteDTO> sites = new ArrayList<SiteDTO>();
		
		final SiteResult result = new SiteResult(sites);
		result.setOffset(command.getOffset());

		query.execute(context.getTransaction(), new SqlResultCallback() {
			@Override
			public void onSuccess(SqlTransaction tx, SqlResultSet results) {
				for(SqlResultSetRow row : results.getRows()) {
					SiteDTO site = toSite(row);
					sites.add(site);
					siteMap.put(site.getId(), site);
				}
				if(!sites.isEmpty()) {
					queryEntities(tx, siteMap);
					joinIndicatorValues(tx, siteMap);
					joinAttributeValues(tx, siteMap);
					
					if(command.getLimit() <= 0) {
						result.setTotalLength(sites.size());
					} else {
						queryTotalLength(tx, command, context, result);
					} 
				}
				callback.onSuccess(result);
			}
		});
	}

	private void applyJoins(final SqlQuery query) {
		query.from("( " +
			
				"SELECT s.SiteId, 0 as Linked, s.Date1, s.Date2, s.DateEdited, s.ActivityId, s.LocationId, s.PartnerId, s.ProjectId, s.Comments " +
					"FROM Site s WHERE s.dateDeleted IS NULL " +
					"UNION ALL " +
				"SELECT DISTINCT s.SiteId, 1 as Linked, s.Date1, s.Date2, s.DateEdited, di.ActivityId, s.LocationId, s.PartnerId, s.ProjectId, s.Comments " +
				    "FROM Site s " +
				    "INNER JOIN indicator si ON (si.activityid=s.activityid) " +
				    "INNER JOIN indicatorlink link ON (si.indicatorid=link.sourceindicatorid) " +
				    "INNER JOIN indicator di on (link.destinationindicatorid=di.indicatorid)  " +
				    "WHERE s.dateDeleted IS NULL" +
			   ") Site ")
			.leftJoin("Activity").on("Site.ActivityId = Activity.ActivityId")
			.leftJoin("UserDatabase").on("Activity.DatabaseId = UserDatabase.DatabaseId")
			.leftJoin("Location").on("Site.LocationId = Location.LocationId")
			.leftJoin("LocationType").on("Location.LocationTypeId = LocationType.LocationTypeId")
			.leftJoin("Partner").on("Site.PartnerId = Partner.PartnerId")
			.leftJoin("Project").on("Site.ProjectId = Project.ProjectId")
		.whereTrue("Activity.dateDeleted IS NULL")
			.and("UserDatabase.dateDeleted IS NULL");
	}

	private void applyPaging(final SqlQuery query, GetSites command) {
		if(command.getOffset() > 0 || command.getLimit() > 0) {
		    query.setLimitClause(database.getDialect().limitClause(
		    		command.getOffset(),
		    		command.getLimit()));
		}
	}

	private void applyPermissions(final SqlQuery query, ExecutionContext context) {
		// Apply permissions if we are on the server, 
		// otherwise permissions have already been taken into account
		// during synchronization

		if(!GWT.isClient()) {    
			query.whereTrue("Activity.DateDeleted IS NULL")
				.and("UserDatabase.DateDeleted IS NULL");
			query.whereTrue(
					"(UserDatabase.OwnerUserId = ? OR " +
							"UserDatabase.DatabaseId in "  +
							"(SELECT p.DatabaseId from UserPermission p where p.UserId = ? and p.AllowViewAll) or " +
							"UserDatabase.DatabaseId in " +
					"(select p.DatabaseId from UserPermission p where (p.UserId = ?) and p.AllowView and p.PartnerId = Site.PartnerId) " +
					" OR (select count(*) from activity pa where pa.published>0 and pa.ActivityId = Site.ActivityId) > 0 )");
			
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
            	query.orderBy("Site.Date1", ascending);
            } else if (field.equals("date2")) {
            	query.orderBy("Site.Date2", ascending);
            } else if (field.equals("dateEdited")) {
            	query.orderBy("Site.DateEdited", ascending);
            } else if (field.equals("locationName")) {
            	query.orderBy("Location.name", ascending);
            } else if (field.equals("partner")) {
            	query.orderBy("Partner.Name", ascending);
            } else if (field.equals("locationAxe")) {
            	query.orderBy("Location.Axe", ascending);
            } else if (field.startsWith(IndicatorDTO.PROPERTY_PREFIX)) {
            	int indicatorId = IndicatorDTO.indicatorIdForPropertyName(field);
            	query.orderBy(SqlQuery.selectSingle("SUM(v.Value)")
    				.from("IndicatorValue", "v")
    				.leftJoin("ReportingPeriod", "r").on("v.ReportingPeriodId=r.ReportingPeriodId")
    				.whereTrue("v.IndicatorId=" + indicatorId)
    				.and("r.SiteId=Site.SiteId"), ascending);
            }
        }
	}
	
	private void applyFilter(SqlQuery query, Filter filter) {
		if(filter != null) {
	        for (DimensionType type : filter.getRestrictedDimensions()) {
	            if (type == DimensionType.Indicator) {
	            	
	            	SqlQuery subQuery = new SqlQuery()
	            		.appendColumn("ReportingPeriod.SiteId")
	            		.from("IndicatorValue")
	            		.leftJoin("ReportingPeriod").on("IndicatorValue.ReportingPeriodId=ReportingPeriod.ReportingPeriodId")
	            		.where("IndicatorValue.IndicatorId").in(filter.getRestrictions(DimensionType.Indicator))
	            		.whereTrue("IndicatorValue.Value IS NOT NULL");
	            	
	            	query.where("SiteId").in(subQuery);
	            
	            } else if (type == DimensionType.Activity) {
	                query.where("Site.ActivityId").in(filter.getRestrictions(type));
	
	            } else if (type == DimensionType.Database) {
	                query.where("Activity.DatabaseId").in(filter.getRestrictions(type));
	
	            } else if (type == DimensionType.Partner) {
	                query.where("Site.PartnerId").in(filter.getRestrictions(type));
	                
	            } else if (type == DimensionType.Project) {
	            	query.where("Site.ProjectId").in(filter.getRestrictions(type));
	
	            } else if (type == DimensionType.AdminLevel) {
	                query.where("Site.LocationId").in(
	                        SqlQuery.select("Link.LocationId").from("LocationAdminLink Link").where("Link.AdminEntityId")
	                                .in(filter.getRestrictions(type)));
	
	            } else if(type == DimensionType.Site) {
	                query.where("Site.SiteId").in(filter.getRestrictions(type));
	            }
	        }
	        if (filter.getMinDate() != null) {
	        	query.where("Site.Date2").greaterThanOrEqualTo(filter.getMinDate());
	        }
	        if (filter.getMaxDate() != null) {
	        	query.where("Site.Date2").lessThanOrEqualTo(filter.getMaxDate());
	        }
		}
	}
	
	private void queryTotalLength(SqlTransaction tx, GetSites command, ExecutionContext context, final SiteResult result) {
		final SqlQuery query = SqlQuery.selectSingle("count(*) AS site_count");
		applyJoins(query);
		applyFilter(query, command.getFilter());
		applyPermissions(query, context);
	
		query.execute(tx, new SqlResultCallback() {
			
			@Override
			public void onSuccess(SqlTransaction tx, SqlResultSet results) {
				result.setTotalLength(results.getRow(0).getInt("site_count"));
			}
		});
	}
	
	/**
	 * Retrieve a distinct list of all AdminEntities that are related to the selected Sites.
	 * 
	 * @param tx
	 * @param siteMap
	 */
	private void queryEntities(SqlTransaction tx, final Multimap<Integer, SiteDTO> siteMap) {

		// first retrieve all the admin DTOs that we'll need for this result set
		
		SqlQuery.select("adminEntityId",
				"name",
				"adminLevelId",
				"adminEntityParentId",
				"x1","y1","x2","y2")
			.from("AdminEntity", "e")
			.where("e.AdminEntityId").in(
				SqlQuery.select("AdminEntityId").from("LocationAdminLink").where("LocationId").in(
						SqlQuery.select("LocationId").from("Site").where("SiteId").in(siteMap.keySet())))
			.execute(tx, new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					Map<Integer, AdminEntityDTO> entities = new HashMap<Integer, AdminEntityDTO>();
					for(SqlResultSetRow row : results.getRows()) {
						AdminEntityDTO entity = GetAdminEntitiesHandler.toEntity(row);
						entities.put(entity.getId(), entity);
					}
					
					// now join them to the SiteDTO rows
					joinEntities(tx, siteMap, entities);
					joinIndicatorValues(tx, siteMap);
				}
			});
	}

	private void joinEntities(SqlTransaction tx,
			final Multimap<Integer, SiteDTO> siteMap, 
			final Map<Integer, AdminEntityDTO> entities) {
		
	    SqlQuery.select(
	    		"Site.SiteId", 
	    		"Link.AdminEntityId")
	        .from("Site")
	        .innerJoin("Location").on("Location.LocationId = Site.LocationId")
	        .innerJoin("LocationAdminLink Link").on("Link.LocationId = Location.LocationId")
	        .where("Site.SiteId").in(siteMap.keySet())
	        .execute(tx, new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					for(SqlResultSetRow row : results.getRows()) {
						for(SiteDTO site : siteMap.get(row.getInt("SiteId"))) {
							AdminEntityDTO entity = entities.get(row.getInt("AdminEntityId"));
							site.setAdminEntity(entity.getLevelId(), entity);
						}
						
					}					
				}
			});
	}	
	
	private void joinIndicatorValues(SqlTransaction tx, final Multimap<Integer, SiteDTO> siteMap) {

    	SqlQuery query = SqlQuery.select()
    		.appendColumn("P.SiteId", "SiteId")
    		.appendColumn("V.IndicatorId", "SourceIndicatorId")
    		.appendColumn("I.ActivityId", "SourceActivityId")
    		.appendColumn("D.IndicatorId", "DestIndicatorId")
    		.appendColumn("D.ActivityId", "DestActivityId")
    		.appendColumn("V.Value")
            .from("ReportingPeriod", "P")
            	.innerJoin("IndicatorValue", "V").on("P.ReportingPeriodId = V.ReportingPeriodId")
            	.innerJoin("Indicator", "I").on("I.IndicatorId = V.IndicatorId")
            	.leftJoin("IndicatorLink", "L").on("L.SourceIndicatorId=I.IndicatorId")
            	.leftJoin("Indicator", "D").on("L.DestinationIndicatorId=D.IndicatorId")
            .where("P.SiteId").in(siteMap.keySet())
            .and("I.dateDeleted IS NULL");
    	
    	Log.info(query.toString());
    	
        query.execute(tx, new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
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
				}
			});
	}
	
	private void joinAttributeValues(SqlTransaction tx, final Multimap<Integer, SiteDTO> siteMap) {

    	SqlQuery.select()
    		.appendColumn("AttributeId", "attributeId")
    		.appendColumn("SiteId", "siteId")
    		.appendColumn("Value", "value")
            .from("AttributeValue")
            .where("SiteId").in(siteMap.keySet())
            .execute(tx, new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					for(SqlResultSetRow row : results.getRows()) {
						int attributeId = row.getInt("attributeId");
			            boolean value = row.getBoolean("value");

			            for(SiteDTO site : siteMap.get( row.getInt("siteId") )) {
							site.setAttributeValue(attributeId, value);
						}
					}		
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
			
			
}
