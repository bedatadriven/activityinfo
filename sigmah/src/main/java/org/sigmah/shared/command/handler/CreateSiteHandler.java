package org.sigmah.shared.command.handler;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.sigmah.client.offline.command.handler.KeyGenerator;
import org.sigmah.shared.command.AddLocation;
import org.sigmah.shared.command.CreateSite;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.AttributeDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.LocationDTO2;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * Handles the creation of a new entity on the client side, and
 * queues the command for later transmission to the server.
 * 
 * Currently only supports updates to the SiteDTO view.
 * 
 */
public class CreateSiteHandler implements CommandHandlerAsync<CreateSite, CreateResult> {
	private KeyGenerator keyGenerator;

	@Inject
	public CreateSiteHandler(KeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void execute(final CreateSite cmd, final ExecutionContext context,
			final AsyncCallback<CreateResult> callback) {

		final Map<String,Object> properties = cmd.getProperties().getTransientMap();
		final int activityId = cmd.getActivityId();

		// look up the Activity Entity so we can get the corresponding location
		// type
		SqlQuery.select("a.locationTypeId", "a.reportingFrequency", "t.boundAdminLevelId")
			.from("Activity", "a")
			.leftJoin("LocationType", "t").on("t.LocationTypeId = a.LocationTypeId")
			.where("a.activityId").equalTo(activityId)
			.execute(context.getTransaction(), new SqlResultCallback() {

			@Override
			public void onSuccess(final SqlTransaction tx, SqlResultSet results) {
				if(results.getRows().isEmpty()) {
					throw new RuntimeException("Could not find activityId " + activityId);
				}
				
				SqlResultSetRow row = results.getRow(0);
				
				final int locationTypeId = row.getInt("locationTypeId");
				Integer boundAdminLevelId = row.isNull("boundAdminLevelId") ? null : row.getInt("boundAdminLevelId");
				final int reportingFrequency = row.getInt("reportingFrequency");
				
				lookupLocationId(context, boundAdminLevelId, locationTypeId, properties, new AsyncCallback<Integer>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void onSuccess(Integer locationId) {
						int siteId = insertSite(tx, activityId, locationId, properties);

						// we only create a reporting period if this is a one-off activity
						Integer reportingPeriodId = null;
						if(reportingFrequency == ActivityDTO.REPORT_ONCE) {
							reportingPeriodId = insertReportingPeriod(tx, siteId, properties);
						}

						// update command for remote consumption with new ids
						// You can call setLocation on SiteDTO, which will set the "locationId" property
						//cmd.getProperties().put("locationId", locationId);
						cmd.getProperties().put("siteId", siteId);
						cmd.getProperties().put("reportingPeriodId", reportingPeriodId);
						
						callback.onSuccess(new CreateResult(siteId));
					}
				});
			}
		});
	}

	private void lookupLocationId(final ExecutionContext context,
			Integer boundAdminLevelId, final int locationTypeId, final Map<String,Object> properties, final AsyncCallback<Integer> callback) {

		final SqlTransaction tx = context.getTransaction();
		if(boundAdminLevelId == null) {
			callback.onSuccess((Integer) properties.get("locationId"));
		} else {
			final int entityId = (Integer) properties.get(AdminLevelDTO.getPropertyName(boundAdminLevelId));
			SqlQuery.select("locationId")
			.from("Location")
			.where("LocationTypeId")
			.equalTo(locationTypeId)
			.where("locationId")
			.in(SqlQuery.select("LocationId")
					.from("LocationAdminLink")
					.where("AdminEntityId")
					.equalTo(entityId))
			.execute(tx, new SqlResultCallback() {
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					if (results.getRows().isEmpty()) {
						SqlQuery.select("Name")
								.from("AdminEntity")
								.where("AdminEntityId")
								.equalTo(entityId)
								.execute(tx, new SqlResultCallback() {
									@Override
									public void onSuccess(SqlTransaction tx, SqlResultSet results) {
										String name = results.getRow(0).getString("Name");
										int locationId = getOrCreateKey(properties, "locationId");
										AddLocation addLocation = new AddLocation().setLocation(new LocationDTO2()
														.setName(name)
														.setId(locationId)
														.setLocationTypeId(locationTypeId));
										context.execute(addLocation, new AsyncCallback<CreateResult>() {
											@Override
											public void onFailure(Throwable caught) {
												//Handled by tx
											}

											@Override
											public void onSuccess(CreateResult result) {
												callback.onSuccess(result.getNewId());
											}
										});
									}
								});
						
					} else {
						callback.onSuccess(results.getRow(0).getInt("locationId"));
					}
				}
			});
		}
	}

	private int insertSite(
			SqlTransaction tx,
			int activityId, 
			int locationId,
			Map<String, Object> properties)  {

		int siteId = getOrCreateKey(properties, "siteId");
		
		SqlInsert.insertInto("Site")
			.value("SiteId", siteId)
			.value("LocationId", locationId)
			.value("ActivityId", activityId)
			.value("Date1", properties.get("date1"))
			.value("Date2", properties.get("date2"))
			.value("Comments", properties.get("comments"))
			.value("PartnerId", properties.get("partnerId"))
			.value("ProjectId", properties.get("projectId"))
			.value("DateCreated", new Date())
			.value("DateEdited", new Date())
			.value("Status", 0) // no longer used  TODO : remove from databases
			.value("Target", 0) // no longer used TODO : remove 
		.execute(tx);

		insertAttributeValues(tx, siteId, properties);
		
		return siteId;
	}
		

	private void insertAttributeValues(
			SqlTransaction tx,
			int siteId, 
			final Map<String, Object> properties)  {

		for(Entry<String,Object> property : properties.entrySet()) {
			if(property.getKey().startsWith(AttributeDTO.PROPERTY_PREFIX) &&
					property.getValue() != null) {

				SqlInsert.insertInto("AttributeValue")
				.value("AttributeId", AttributeDTO.idForPropertyName(property.getKey()))
				.value("SiteId", siteId)
				.value("Value", property.getValue())
				.execute(tx);
			}
		}
	}

	private int insertReportingPeriod(
			SqlTransaction tx,
			int siteId, 
			Map<String, Object> properties)  {
		
		int reportingPeriodId = getOrCreateKey(properties, "reportingPeriodId");
		SqlInsert.insertInto("ReportingPeriod")
			.value("ReportingPeriodId", reportingPeriodId)
			.value("SiteId", siteId)
			.value("Date1", properties.get("date1"))
			.value("Date2", properties.get("date2"))
			.value("DateCreated", new Date())
			.value("DateEdited", new Date())
			.value("Monitoring", 0) // no longer used TODO : remove
		.execute(tx);

		insertIndicatorValues(properties, tx, reportingPeriodId);
		
		return reportingPeriodId;
	}
	
	private void insertIndicatorValues(final Map<String, Object> properties,
			SqlTransaction tx, int reportingPeriodId) {
		// insert the indicator values 
		for(Entry<String,Object> property : properties.entrySet()) {
			if(property.getKey().startsWith(IndicatorDTO.PROPERTY_PREFIX) &&
					property.getValue() != null) {

				SqlInsert.insertInto("IndicatorValue")
				.value("IndicatorId", IndicatorDTO.indicatorIdForPropertyName(property.getKey()))
				.value("ReportingPeriodId", reportingPeriodId)
				.value("Value", property.getValue())
				.execute(tx);
			}
		}
	}
	
	private int getOrCreateKey(Map<String,Object> properties, String name) {
		if(!properties.containsKey(name)) {
			properties.put(name, keyGenerator.generateInt());
		} 
		return (Integer) properties.get(name);
	}
		
}
