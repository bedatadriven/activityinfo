package org.sigmah.shared.command.handler;

import java.util.List;

import org.sigmah.shared.command.ExactMatchingLocations;
import org.sigmah.shared.command.ExactMatchingLocations.ExactMatchingLocationsResult;
import org.sigmah.shared.dto.LocationDTO2;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.dev.util.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

/** Compares all locations in the database for locations matching on name/axe/x/y/locationTypeId
 *  
 * Warning: this handler takes O=n^n queries 
 * */
public class ExactMatchingLocationsHandler implements CommandHandlerAsync<ExactMatchingLocations, ExactMatchingLocationsResult> {

	@Override
	public void execute(ExactMatchingLocations command,
			ExecutionContext context,
			final AsyncCallback<ExactMatchingLocationsResult> callback) {
		SqlQuery.select("Id", "Name", "Axe", "X", "Y", "LocationTypeId")
				.from("Location")
				.execute(context.getTransaction(), new SqlResultCallback() {
					@Override
					public void onSuccess(SqlTransaction tx, SqlResultSet results) {
						final ExactMatchingLocationsResult result = new ExactMatchingLocationsResult();
						for (SqlResultSetRow row: results.getRows()) {
							String name = row.getString("Name");
							String axe = row.isNull("Axe") ? null : row.getString("Axe");
							Double longitude = row.isNull("X") ? null : row.getDouble("X");
							Double latitude = row.isNull("Y") ? null : row.getDouble("Y");
							Integer locationTypeId = row.getInt("LocationTypeId");
							int id = row.getInt("Id");
							
							SqlQuery.select("Id", "Name", "Axe", "X", "Y", "LocationTypeId")
									.from("Location")
									.where("Name").equalTo(name)
									.where("Axe").equalTo(axe)
									.where("X").equalTo(longitude)
									.where("Y").equalTo(latitude)
									.where("LcoationTypeId").equalTo(locationTypeId)
									.execute(tx, new SqlResultCallback() {
										@Override
										public void onSuccess(SqlTransaction tx, SqlResultSet results) {
											if (results.getRowsAffected() > 0) {
												List<LocationDTO2> locations = Lists.create();
												for (SqlResultSetRow row : results.getRows()) {
													locations.add(LocationDTO2.fromSqlRow(row));
												}
												result.getMatchingLocations().add(locations);
											}
										}
									});
						}
						callback.onSuccess(result);
					}
				});
	}
	
}
