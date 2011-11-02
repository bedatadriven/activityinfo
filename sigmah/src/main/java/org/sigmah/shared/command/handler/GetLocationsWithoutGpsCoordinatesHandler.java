package org.sigmah.shared.command.handler;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.command.GetLocationsWithoutGpsCoordinates;
import org.sigmah.shared.command.result.LocationsWithoutGpsResult;
import org.sigmah.shared.dto.LocationDTO;

import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetLocationsWithoutGpsCoordinatesHandler implements CommandHandlerAsync<GetLocationsWithoutGpsCoordinates, LocationsWithoutGpsResult>{

	@Override
	public void execute(final GetLocationsWithoutGpsCoordinates command,
			final ExecutionContext context,
			final AsyncCallback<LocationsWithoutGpsResult> callback) {
		
		// 100K seems enough for now
		final int max = command.hasLocationCountLimit() ? command.getMaxLocations() : 100000;
		
		SqlQuery.selectSingle("Count(*)")
				.from("Location")
				.where("X").isNull()
				.where("Y").isNull()

				.execute(context.getTransaction(), new SqlResultCallback() {
					
					@Override
					public void onSuccess(SqlTransaction tx, SqlResultSet results) {
						final LocationsWithoutGpsResult result = new LocationsWithoutGpsResult();
						result.setTotalLocationsCount(results.intResult());

						SqlQuery.select("Name", "LocationId", "Axe")
								.from("Location")
								.where("X")
								.isNull()
								.where("Y")
								.isNull()
								
								.setLimitClause("LIMIT 0, " + Integer.toString(max))
								
								.execute(context.getTransaction(), new SqlResultCallback() {
									@Override
									public void onSuccess(SqlTransaction tx, SqlResultSet results) {
										List<LocationDTO> locations = new ArrayList<LocationDTO>();
										for (SqlResultSetRow row : results.getRows()) {
											LocationDTO location = new LocationDTO();
											location.setName(row.getString("Name"));
											location.setId(row.getInt("LocationId"));
											location.setAxe(row.getString("Axe"));
											locations.add(location);
										}
										result.setData(locations);
										callback.onSuccess(result);
									}
								});
						}
				});
	}
}
