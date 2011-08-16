package org.sigmah.shared.command.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class GetSitesHandler implements CommandHandlerAsync<GetSites, SiteResult> {

	private SqlDatabase database;

	@Inject
	public GetSitesHandler(SqlDatabase database) {
		super();
		this.database = database;
	}

	@Override
	public void execute(final GetSites command, final CommandContext context,
			final AsyncCallback<SiteResult> callback) {

		doQuery(command, context, callback);
	}

	private void calculatePage(GetSites command, CommandContext context,
			AsyncCallback<SiteResult> callback) {

		final SqlQuery query = SqlQuery.select("Site.SiteId");
		
		applyJoins(query);
		applyFilter(query, command.getFilter());
		applyPermissions(query, context);
		applySort(query, command.getSortInfo());
		
	}

	private void doQuery(final GetSites command, final CommandContext context,
			final AsyncCallback<SiteResult> callback) {
		final SqlQuery query = SqlQuery.select("site.SiteId")
			.appendColumn("activity.ActivityId")
			.appendColumn("activity.name", "ActivityName")
			.appendColumn("UserDatabase.DatabaseId", "DatabaseId")
			.appendColumn("site.Date1", "Date1")
			.appendColumn("site.Date2", "Date2")
			.appendColumn("partner.PartnerId", "PartnerId")
			.appendColumn("partner.name", "PartnerName")
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

		final Map<Integer, SiteDTO> siteMap = new HashMap<Integer, SiteDTO>();
		final List<SiteDTO> sites = new ArrayList<SiteDTO>();
		
		final SiteResult result = new SiteResult(sites);
		result.setOffset(command.getOffset());

		database.transaction(new SqlTransactionCallback() {

			@Override
			public void begin(SqlTransaction tx) {
				query.execute(tx, new SqlResultCallback() {

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
							
							if(command.getLimit() > 0) {
								queryTotalLength(tx, command, context, result);
							} else {
								result.setTotalLength(sites.size());
							}
						}
					}
				});
			}

			@Override
			public void onError(SqlException e) {
				callback.onFailure(e);
			}

			@Override
			public void onSuccess() {
				callback.onSuccess(result);
			}
		});
	}

	private void applyJoins(final SqlQuery query) {
		query.from("Site")
			.leftJoin("Activity").on("Site.ActivityId = Activity.ActivityId")
			.leftJoin("UserDatabase").on("Activity.DatabaseId = UserDatabase.DatabaseId")
			.leftJoin("Location").on("Site.LocationId = Location.LocationId")
			.leftJoin("LocationType").on("Location.LocationTypeId = LocationType.LocationTypeId")
			.leftJoin("Partner").on("Site.PartnerId = Partner.PartnerId")
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

	private void applyPermissions(final SqlQuery query, CommandContext context) {
		// Apply permissions if we are on the server, 
		// otherwise permissions have already been taken into account
		// during synchronization

		if(!GWT.isClient()) {    
			query.whereTrue("Site.DateDeleted IS NULL")
				.and("Activity.DateDeleted IS NULL")
				.and("UserDatabase.DateDeleted IS NULL");
			query.whereTrue(
					"(UserDatabase.OwnerUserId = ? OR " +
							"UserDatabase.DatabaseId in "  +
							"(SELECT p.DatabaseId from UserPermission p where p.UserId = ? and p.AllowViewAll) or " +
							"UserDatabase.DatabaseId in " +
					"(select p.DatabaseId from UserPermission p where p.UserId = ? and p.AllowView and p.PartnerId = Site.PartnerId))");
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
            } else if (field.equals("locationName")) {
            	query.orderBy("Location.name", ascending);
            } else if (field.equals("partner")) {
            	query.orderBy("Partner.Name", ascending);
            } else if (field.equals("locationAxe")) {
            	query.orderBy("Location.Axe", ascending);
            } else if (field.startsWith(IndicatorDTO.PROPERTY_PREFIX)) {
            	int indicatorId = IndicatorDTO.indicatorIdForPropertyName(field);
            	query.orderBy(SqlQuery.select("SUM(v.Value)")
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
	                query.where("Indicator.IndicatorId").in(filter.getRestrictions(type));
	
	            } else if (type == DimensionType.Activity) {
	                query.where("Site.ActivityId").in(filter.getRestrictions(type));
	
	            } else if (type == DimensionType.Database) {
	                query.where("Activity.DatabaseId").in(filter.getRestrictions(type));
	
	            } else if (type == DimensionType.Partner) {
	                query.where("Site.PartnerId").in(filter.getRestrictions(type));
	
	            } else if (type == DimensionType.AdminLevel) {
	                query.where("Site.LocationId").in(
	                        SqlQuery.select("Link.LocationId").from("LocationAdminLink Link").where("Link.AdminEntityId")
	                                .in(filter.getRestrictions(type)));
	
	            } else if(type == DimensionType.Site) {
	                query.where("Site.SiteId").in(filter.getRestrictions(type));
	            }
	        }
		}
	}
	
	private void queryTotalLength(SqlTransaction tx, GetSites command, CommandContext context, final SiteResult result) {
		final SqlQuery query = SqlQuery.select("count(*) AS site_count");
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
	private void queryEntities(SqlTransaction tx, final Map<Integer, SiteDTO> siteMap) {

		// first retrieve all the admin DTOs that we'll need for this result set
		
		SqlQuery.select("e.*")
			.from("AdminEntity e")
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
			final Map<Integer, SiteDTO> siteMap, 
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
						SiteDTO site = siteMap.get(row.getInt("SiteId"));
						AdminEntityDTO entity = entities.get(row.getInt("AdminEntityId"));
						
						site.setAdminEntity(entity.getLevelId(), entity);
						
					}					
				}
			});
	}	
	
	private void joinIndicatorValues(SqlTransaction tx, final Map<Integer, SiteDTO> siteMap) {

    	SqlQuery.select("P.SiteId", "V.IndicatorId", "V.Value")
            .from("ReportingPeriod", "P")
            	.innerJoin("IndicatorValue", "V").on("P.ReportingPeriodId = V.ReportingPeriodId")
            	.innerJoin("Indicator", "I").on("I.IndicatorId = V.IndicatorId")
            .where("P.SiteId").in(siteMap.keySet())
            .and("I.dateDeleted IS NULL")
            .execute(tx, new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					for(SqlResultSetRow row : results.getRows()) {
						SiteDTO site = siteMap.get( row.getInt("SiteId") );
						int indicatorId = row.getInt("IndicatorId");
			            double indicatorValue = row.getDouble("Value");

						site.setIndicatorValue(indicatorId, indicatorValue);
					}		
				}
			});
	}
	
	private void joinAttributeValues(SqlTransaction tx, final Map<Integer, SiteDTO> siteMap) {

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
						SiteDTO site = siteMap.get( row.getInt("siteId") );
						int attributeId = row.getInt("attributeId");
			            boolean value = row.getBoolean("value");

						site.setAttributeValue(attributeId, value);
					}		
				}
			});
	}

	private SiteDTO toSite(SqlResultSetRow row) {
	    SiteDTO model = new SiteDTO();
        model.setId( row.getInt("SiteId") );
        model.setActivityId( row.getInt("ActivityId") );
        model.setDate1( row.getDate("Date1") );
        model.setDate2( row.getDate("Date2") );
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
        
        model.setPartner(partner);
        return model;
	}
			
			
}
