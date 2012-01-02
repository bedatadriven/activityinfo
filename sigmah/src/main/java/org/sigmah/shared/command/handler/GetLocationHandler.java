package org.sigmah.shared.command.handler;

import org.sigmah.shared.command.GetLocation;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.LocationDTO;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetLocationHandler implements CommandHandlerAsync<GetLocation, LocationDTO> {

	@Override
	public void execute(final GetLocation command, ExecutionContext context,
			final AsyncCallback<LocationDTO> callback) {
		
		final LocationDTO result = new LocationDTO();
		result.setId(command.getLocationId());
		
		SqlQuery.select("name", "axe", "x", "y").from("location")
			.where("locationId").equalTo(command.getLocationId())
			.execute(context.getTransaction(), new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					if(results.getRows().size() != 1) {
						throw new IllegalArgumentException("Expected exactly only one row for location id " + 
								command.getLocationId() + ", got " + results.getRows().size() + " row(s)");
					}
					SqlResultSetRow row = results.getRow(0);
					result.setName(row.getString("name"));
					result.setAxe(row.getString("axe"));
					if(!row.isNull("x") && !row.isNull("y")) {
						result.setLatitude(row.getDouble("y"));
						result.setLongitude(row.getDouble("x"));
					}
				}
			});
		
		SqlQuery.select()
			.appendColumn("AdminEntity.AdminEntityId", "adminEntityId")
			.appendColumn("AdminEntity.Name", "name")
			.appendColumn("AdminEntity.AdminLevelId", "levelId")
			.from("LocationAdminLink")
			.leftJoin("AdminEntity").on("LocationAdminLink.AdminEntityId=AdminEntity.AdminEntityId")
			.where("LocationId").equalTo(command.getLocationId())
			.execute(context.getTransaction(), new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					for(SqlResultSetRow row : results.getRows()) {
						AdminEntityDTO entity = new AdminEntityDTO();
						entity.setId(row.getInt("adminEntityId"));
						entity.setName(row.getString("name"));
						entity.setLevelId(row.getInt("levelId"));
						result.setAdminEntity(entity.getLevelId(), entity);
					}
					
					callback.onSuccess(result);
				}
		});
				
	}

}
