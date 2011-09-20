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
		/*
		 * select * from location where locationid in 
		 * (select locationid from locationadminlink where locationadminlink.adminentityid =141801 /* adminentity1  ) 
		 * and locationid in (select locationid from locationadminlink where locationadminlink.adminentityid =141945)
		 */
		 
		 SqlQuery q= SqlQuery.select("LocationID","Name")
		 		 			 .from("location");
		 
		 for (Integer adminEntityId : command.getAdminEntityIds()) {
	 		 q.where("locationid")
	 		  .in(SqlQuery.select("locationid")
	 				  	  .from("locationadminlink")
	 				      .where("adminentityid")
	 				      .equalTo(adminEntityId));
			 
		 }
		 q.where("Name").startsWith(command.getName())
		 .execute(context.getTransaction(), new SqlResultCallback() {
			@Override
			public void onSuccess(SqlTransaction tx, SqlResultSet results) {
				List<LocationDTO2> locations = Lists.newArrayList();
				for (SqlResultSetRow row : results.getRows()) {
					LocationDTO2 location = new LocationDTO2()
									.setName(row.getString("Name"))
									.setId(row.getInt("LocationID"))
									.setLatitude(row.getDouble("Y"))
									.setLongitude(row.getDouble("X"));
					locations.add(location);
				}
				
				callback.onSuccess(new LocationsResult(locations, locations.size() > command.getThreshold()));
			}
			
		});
	}

}
