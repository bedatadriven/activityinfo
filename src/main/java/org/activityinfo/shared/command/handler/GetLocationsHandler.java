package org.activityinfo.shared.command.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activityinfo.shared.command.GetLocations;
import org.activityinfo.shared.command.GetLocations.GetLocationsResult;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.LocationDTO;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetLocationsHandler implements CommandHandlerAsync<GetLocations, GetLocationsResult> {

	@Override
	public void execute(final GetLocations command, final ExecutionContext context, final AsyncCallback<GetLocationsResult> callback) {
		if (command.hasLocationIds()) {
			final Map<Integer, LocationDTO> dtos = new HashMap<Integer, LocationDTO>();
			
			SqlQuery.select("locationID", "name", "axe", "x", "y")
				.from(Tables.LOCATION)
				.where("locationId").in(command.getLocationIds())
				.execute(context.getTransaction(), new SqlResultCallback() {
					@Override
					public void onSuccess(SqlTransaction tx, SqlResultSet results) {
						for(SqlResultSetRow row : results.getRows()) {
							final LocationDTO dto = new LocationDTO();
							dto.setId(row.getInt("locationID"));
							dto.setName(row.getString("name"));
							dto.setAxe(row.getString("axe"));
							if(!row.isNull("x") && !row.isNull("y")) {
								dto.setLatitude(row.getDouble("y"));
								dto.setLongitude(row.getDouble("x"));
							}
							dtos.put(dto.getId(), dto);
						}
						
						SqlQuery.select()
							.appendColumn("AdminEntity.AdminEntityId", "adminEntityId")
							.appendColumn("AdminEntity.Name", "name")
							.appendColumn("AdminEntity.AdminLevelId", "levelId")
							.appendColumn("link.LocationID", "locationId")
							.from(Tables.LOCATION_ADMIN_LINK, "link")
							.leftJoin(Tables.ADMIN_ENTITY, "AdminEntity").on("link.AdminEntityId=AdminEntity.AdminEntityId")
							.where("link.LocationId").in(command.getLocationIds())
							.execute(context.getTransaction(), new SqlResultCallback() {
								@Override
								public void onSuccess(SqlTransaction tx, SqlResultSet results) {
									for(SqlResultSetRow row : results.getRows()) {
										AdminEntityDTO entity = new AdminEntityDTO();
										entity.setId(row.getInt("adminEntityId"));
										entity.setName(row.getString("name"));
										entity.setLevelId(row.getInt("levelId"));
										
										LocationDTO dto = dtos.get(row.getInt("locationId"));
										if (dto != null) {
											dto.setAdminEntity(entity.getLevelId(), entity);
										}
									}
									
									List<LocationDTO> list = new ArrayList<LocationDTO>(dtos.values());
									callback.onSuccess(new GetLocationsResult(list));
								}
							});
					}
				});
		} else {
			callback.onSuccess(new GetLocationsResult());
		}
	}

}
