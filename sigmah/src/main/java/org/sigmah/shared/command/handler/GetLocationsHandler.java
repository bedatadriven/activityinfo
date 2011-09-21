package org.sigmah.shared.command.handler;

import java.util.List;

import org.sigmah.shared.command.GetLocations;
import org.sigmah.shared.command.GetLocations.LocationsResult;
import org.sigmah.shared.dto.LocationDTO2;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class GetLocationsHandler implements CommandHandlerAsync<GetLocations, LocationsResult> {

	@Override
	public void execute(final GetLocations command, ExecutionContext context, final AsyncCallback<LocationsResult> callback) {
		 SqlQuery query = SqlQuery.select("LocationID","Name","Axe", "X", "Y").from("location");
		 
		 if (command.getAdminEntityIds() != null) {
			 for (Integer adminEntityId : command.getAdminEntityIds()) {
		 		 query.where("locationid")
			 		  .in(SqlQuery.select("locationid")
			 				  	  .from("locationadminlink")
			 				      .where("adminentityid")
			 				      .equalTo(adminEntityId));
				 
			 }
		 }
		 query.where("Name")
			  .startsWith(command.getName())

			  .execute(context.getTransaction(), new SqlResultCallback() {
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					List<LocationDTO2> locations = Lists.newArrayList();
					for (SqlResultSetRow row : results.getRows()) {
						LocationDTO2 location = new LocationDTO2();
						location.setName(row.getString("Name"));
						location.setAxe(row.getString("Axe"));
						location.setId(row.getInt("LocationID"));
						if (!row.isNull("Y")) {
							location.setLatitude(row.getDouble("Y"));
						}
						if (!row.isNull("X")) {
							location.setLongitude(row.getDouble("X"));
						}
						locations.add(location);
					}
					LocationsResult result = new LocationsResult()
						.setLocations(locations)
						.setHasExceededTreshold(locations.size() > command.getThreshold())
						.setAmountResults(results.getRows().size());
					
					callback.onSuccess(result);
				}
			
		});
	}

}
