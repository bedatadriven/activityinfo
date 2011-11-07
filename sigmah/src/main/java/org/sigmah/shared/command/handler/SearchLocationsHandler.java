package org.sigmah.shared.command.handler;

import java.util.List;
import java.util.Map;

import org.sigmah.shared.command.SearchLocations;
import org.sigmah.shared.command.result.LocationResult;
import org.sigmah.shared.dto.LocationDTO;

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

	private final SqlDialect dialect;
	
	@Inject
	public SearchLocationsHandler(SqlDialect dialect) {
		super();
		this.dialect = dialect;
	}

	@Override
	public void execute(final SearchLocations command, final ExecutionContext context, final AsyncCallback<LocationResult> callback) {
		 SqlQuery query = composeQuery(command);
		 query.setLimitClause(dialect.limitClause(0, 26));

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

	private SqlQuery composeQuery(final SearchLocations command) {
		SqlQuery query = SqlQuery.select("LocationId", "Name","Axe", "X", "Y").from("location");
		 
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
