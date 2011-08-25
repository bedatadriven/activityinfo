package org.sigmah.shared.command.handler;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.sigmah.client.offline.command.CommandQueue;
import org.sigmah.client.offline.command.handler.KeyGenerator;
import org.sigmah.shared.command.CreateSite;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.AttributeDTO;
import org.sigmah.shared.dto.IndicatorDTO;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
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


	private SqlDatabase database;
	private KeyGenerator keyGenerator;

	@Inject
	public CreateSiteHandler(SqlDatabase database, KeyGenerator keyGenerator) {
		this.database = database;
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void execute(final CreateSite cmd, ExecutionContext context,
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
				
				lookupName(tx, boundAdminLevelId, properties, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// handled by tx-level 
					}

					@Override
					public void onSuccess(String name) {
						int locationId = insertLocation(tx, locationTypeId, name, properties);
						int siteId = insertSite(tx, activityId, locationId, properties);

						// we only create a reporting period if this is a one-off activity
						Integer reportingPeriodId = null;
						if(reportingFrequency == ActivityDTO.REPORT_ONCE) {
							reportingPeriodId = insertReportingPeriod(tx, siteId, properties);
						}

						// update command for remote consumption with new ids
						cmd.getProperties().put("locationId", locationId);
						cmd.getProperties().put("siteId", siteId);
						cmd.getProperties().put("reportingPeriodId", reportingPeriodId);
						
						callback.onSuccess(new CreateResult(siteId));
					}
				});
			}
		});

	}
	

	private void lookupName(SqlTransaction tx,
			Integer boundAdminLevelId, Map<String,Object> properties, final AsyncCallback<String> callback) {

		if(boundAdminLevelId == null) {
			callback.onSuccess((String) properties.get("locationName"));
		} else {
			int entityId = (Integer) properties.get(AdminLevelDTO.getPropertyName(boundAdminLevelId));
			SqlQuery.select("name").from("AdminEntity").where("AdminEntityId").equalTo(entityId)
			.execute(tx, new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					callback.onSuccess(results.getRow(0).getString("name"));
				}
			});
		}
		
	}

	private int insertSite(
			SqlTransaction tx,
			int activityId, 
			int locationId,
			Map<String, Object> properties)  {

		int siteId = keyGenerator.generateInt();
		
		SqlInsert.insertInto("Site")
		.value("SiteId", siteId)
		.value("LocationId", locationId)
		.value("ActivityId", activityId)
		.value("Date1", properties.get("date1"))
		.value("Date2", properties.get("date2"))
		.value("Comments", properties.get("comments"))
		.value("PartnerId", properties.get("partnerId"))
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

	private int insertLocation(
			SqlTransaction tx,
			int locationTypeId, 
			String name,
			Map<String, Object> properties) {


		int locationId = keyGenerator.generateInt();
		
		SqlInsert.insertInto("Location")
		.value("LocationId", locationId)
		.value("Name", name)
		.value("Axe", properties.get("locationAxe"))
		.value("X", properties.get("x"))
		.value("Y", properties.get("y"))
		.value("LocationTypeId", locationTypeId)
		.execute(tx);
		
		insertLocationAdminLinks(tx, locationId, properties);

		return locationId;
	}
	
	private void insertLocationAdminLinks(
			SqlTransaction tx,
			int locationId, 
			Map<String, Object> properties)  {
		
		for(Entry<String,Object> property : properties.entrySet()) {
			if(property.getKey().startsWith(AdminLevelDTO.PROPERTY_PREFIX)) {
				Integer entityId = (Integer) property.getValue();
				if(entityId != null) {
					SqlInsert.insertInto("LocationAdminLink")
					.value("AdminEntityId", entityId)
					.value("Locationid", locationId)
					.execute(tx);
				}
			}
		}
	}

	private int insertReportingPeriod(
			SqlTransaction tx,
			int siteId, 
			Map<String, Object> properties)  {
		int reportingPeriodId = keyGenerator.generateInt();
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
		
}
