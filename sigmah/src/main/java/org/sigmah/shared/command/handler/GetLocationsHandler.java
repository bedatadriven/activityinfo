package org.sigmah.shared.command.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.shared.command.GetLocations;
import org.sigmah.shared.command.GetLocations.LocationsResult;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.LocationDTO2;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class GetLocationsHandler implements CommandHandlerAsync<GetLocations, LocationsResult> {

	@Override
	public void execute(final GetLocations command, final ExecutionContext context, final AsyncCallback<LocationsResult> callback) {
		 SqlQuery query = SqlQuery.select("LocationId", "Name","Axe", "X", "Y", "LocationTypeID").from("location");
		 
		 if (command.getAdminEntityIds() != null) {
			 for (Integer adminEntityId : command.getAdminEntityIds()) {
		 		 query.where("LocationId")
			 		  .in(SqlQuery.select("LocationId")
			 				  	  .from("locationadminlink")
			 				      .where("adminentityid")
			 				      .equalTo(adminEntityId));
			 }
		 }
		 if (command.getLocationTypeId() != 0) {
			 query.where("locationTypeID")
			 	  .equalTo(command.getLocationTypeId());
		 }
		 query.where("Name")
			  .startsWith(command.getName())

			  .execute(context.getTransaction(), new SqlResultCallback() {
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					// Create a list of locations from query result
					List<LocationDTO2> locations = Lists.newArrayList();
					Map<Integer, LocationDTO2> locationsById = Maps.newHashMap();
					for (SqlResultSetRow row : results.getRows()) {
						LocationDTO2 location = LocationDTO2.fromSqlRow(row);
						locations.add(location);
						locationsById.put(location.getId(), location);
					}
					
					// Attach AdminEntities to every location 
					if (!locationsById.isEmpty()) {
						queryEntities(context.getTransaction(), locationsById);
					}
					
					LocationsResult result = new LocationsResult()
						.setLocations(locations)
						.setHasExceededTreshold(locations.size() > command.getThreshold())
						.setAmountResults(results.getRows().size());
					
					callback.onSuccess(result);
				}
			
		});
	}

	private void joinEntities(SqlTransaction tx,
			final Map<Integer, LocationDTO2> locationsById, 
			final Map<Integer, AdminEntityDTO> entities) {
		
	    SqlQuery
	    	.select("Location.LocationId", "Link.AdminEntityId")
	        .from("Location")
	        .innerJoin("LocationAdminLink Link").on("Link.LocationId = Location.LocationId")
	        .where("Location.LocationId").in(locationsById.keySet())
	        .execute(tx, new SqlResultCallback() {

	        	@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					for(SqlResultSetRow row : results.getRows()) {
						LocationDTO2 location = locationsById.get(row.getInt("LocationId"));
						AdminEntityDTO entity = entities.get(row.getInt("AdminEntityId"));
						location.setAdminEntity(entity.getLevelId(), entity);
					}					
				}
			});
	}	
	
	private void queryEntities(SqlTransaction tx, final Map<Integer, LocationDTO2> locationsById) {
		SqlQuery
			.select("adminEntityId", "name", "adminLevelId", "adminEntityParentId", "x1","y1","x2","y2")
			.from("AdminEntity", "e")
			.where("e.AdminEntityId")
				.in(SqlQuery
						.select("AdminEntityId")
						.from("LocationAdminLink")
						.where("LocationId")
						.in(locationsById.keySet()))
			.execute(tx, new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					Map<Integer, AdminEntityDTO> entities = new HashMap<Integer, AdminEntityDTO>();
					for(SqlResultSetRow row : results.getRows()) {
						AdminEntityDTO entity = GetAdminEntitiesHandler.toEntity(row);
						entities.put(entity.getId(), entity);
					}
					
					// now join them to the SiteDTO rows
					joinEntities(tx, locationsById, entities);
				}
			});
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
