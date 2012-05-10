package org.activityinfo.shared.command.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activityinfo.shared.command.SearchLocations;
import org.activityinfo.shared.command.result.LocationResult;
import org.activityinfo.shared.dto.LocationDTO;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;


public class SearchLocationsHandler implements CommandHandlerAsync<SearchLocations, LocationResult> {

	private static final int MAX_LOCATIONS = 26;
	
	private final SqlDialect dialect;
		
	@Inject
	public SearchLocationsHandler(SqlDialect dialect) {
		super();
		this.dialect = dialect;
	}

	@Override
	public void execute(final SearchLocations command, final ExecutionContext context, final AsyncCallback<LocationResult> callback) {
		 
		// first get a count of how many sites we're talking about
		baseQuery(command)
			.appendColumn("count(*)", "count")
			.execute(context.getTransaction(), new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					int count = results.getRow(0).getInt("count");
					if(count > MAX_LOCATIONS) {
						LocationResult result = new LocationResult(new ArrayList<LocationDTO>());
						result.setOffset(0);
						result.setTotalLength(count);
						callback.onSuccess(result);
					} else {
						retrieveLocations(command, context, callback);
					}
				}
			});
	}

	private void retrieveLocations(final SearchLocations command,
			final ExecutionContext context,
			final AsyncCallback<LocationResult> callback) {
		SqlQuery query = baseQuery(command)
			.appendColumns("LocationId", "Name","Axe", "X", "Y")
			.setLimitClause(dialect.limitClause(0, 26));

		 query.execute(context.getTransaction(), new SqlResultCallback() {
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					// Create a list of locations from query result
					List<LocationDTO> locations = Lists.newArrayList();
					Map<Integer, LocationDTO> locationsById = Maps.newHashMap();
					for (SqlResultSetRow row : results.getRows()) {
						LocationDTO location = LocationDTO.fromSqlRow(row);
						locations.add(location);
						locationsById.put(location.getId(), location);
					}
					
					LocationResult result = new LocationResult(locations);
					result.setOffset(0);
					result.setTotalLength(results.getRows().size());
					
					callback.onSuccess(result);
				}
			
		});
	}

	private SqlQuery baseQuery(final SearchLocations command) {
		SqlQuery query = SqlQuery.select().from("location");
		 
		 if (command.getAdminEntityIds() != null) {
			 for (Integer adminEntityId : command.getAdminEntityIds()) {
		 		 query.where("LocationId")
			 		  .in(SqlQuery.select("LocationId")
			 				  	  .from("locationadminlink")
			 				      .where("adminentityid")
			 				      .equalTo(adminEntityId));
			 }
		 }
		 query.orderBy("location.name");
		 
		 if (command.getLocationTypeId() != 0) {
			 query.where("locationTypeID")
			 	  .equalTo(command.getLocationTypeId());
		 }
		 if(!Strings.isNullOrEmpty(command.getName())) {
			 query.where("Name")
				  .startsWith(command.getName());
		 }
		return query;
	}

}
