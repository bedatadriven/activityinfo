package org.sigmah.shared.command.handler;

import java.util.Date;
import java.util.Map.Entry;

import org.sigmah.shared.command.CreateSite;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.AttributeDTO;
import org.sigmah.shared.dto.IndicatorDTO;

import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.extjs.gxt.ui.client.data.RpcMap;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Handles the creation of a new entity on the client side, and
 * queues the command for later transmission to the server.
 * 
 * Currently only supports updates to the SiteDTO view.
 * 
 */
public class CreateSiteHandler implements CommandHandlerAsync<CreateSite, CreateResult> {


	@Override
	public void execute(final CreateSite cmd, final ExecutionContext context,
			final AsyncCallback<CreateResult> callback) {

		
		insertSite(context.getTransaction(), cmd);

		// we only create a reporting period if this is a one-off activity
		Integer reportingPeriodId = cmd.getReportingPeriodId();
		if(reportingPeriodId != null) {
			insertReportingPeriod(context.getTransaction(), cmd);
		}

		callback.onSuccess(new CreateResult(cmd.getSiteId()));
	}

//	private void lookupLocationId(final ExecutionContext context,
//			Integer boundAdminLevelId, final int locationTypeId, final Map<String,Object> properties, final AsyncCallback<Integer> callback) {
//
//		final SqlTransaction tx = context.getTransaction();
//		if(boundAdminLevelId == null) {
//			callback.onSuccess((Integer) properties.get("locationId"));
//		} else {
//			final int entityId = (Integer) properties.get(AdminLevelDTO.getPropertyName(boundAdminLevelId));
//			SqlQuery.select("locationId")
//			.from("Location")
//			.where("LocationTypeId")
//			.equalTo(locationTypeId)
//			.where("locationId")
//			.in(SqlQuery.select("LocationId")
//					.from("LocationAdminLink")
//					.where("AdminEntityId")
//					.equalTo(entityId))
//			.execute(tx, new SqlResultCallback() {
//				@Override
//				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
//					if (results.getRows().isEmpty()) {
//						SqlQuery.select("Name")
//								.from("AdminEntity")
//								.where("AdminEntityId")
//								.equalTo(entityId)
//								.execute(tx, new SqlResultCallback() {
//									@Override
//									public void onSuccess(SqlTransaction tx, SqlResultSet results) {
//										String name = results.getRow(0).getString("Name");
//										int locationId = getOrCreateKey(properties, "locationId");
//										AddLocation addLocation = new AddLocation().setLocation(new LocationDTO()
//														.setName(name)
//														.setId(locationId)
//														.setLocationTypeId(locationTypeId));
//										context.execute(addLocation, new AsyncCallback<CreateResult>() {
//											@Override
//											public void onFailure(Throwable caught) {
//												//Handled by tx
//											}
//
//											@Override
//											public void onSuccess(CreateResult result) {
//												callback.onSuccess(result.getNewId());
//											}
//										});
//									}
//								});
//						
//					} else {
//						callback.onSuccess(results.getRow(0).getInt("locationId"));
//					}
//				}
//			});
//		}
//	}

	private void insertSite(
			SqlTransaction tx,
			CreateSite cmd)  {

		RpcMap properties = cmd.getProperties();
		
		SqlInsert.insertInto("Site")
			.value("SiteId", cmd.getSiteId())
			.value("LocationId", cmd.getLocationId())
			.value("ActivityId", cmd.getActivityId())
			.value("Date1", properties.get("date1"))
			.value("Date2", properties.get("date2"))
			.value("Comments", properties.get("comments"))
			.value("PartnerId", properties.get("partnerId"))
			.value("ProjectId", properties.get("projectId"))
			.value("DateCreated", new Date())
			.value("DateEdited", new Date())
		.execute(tx);

		insertAttributeValues(tx, cmd);
	}
		

	private void insertAttributeValues(
			SqlTransaction tx, CreateSite cmd)  {

		for(Entry<String,Object> property : cmd.getProperties().getTransientMap().entrySet()) {
			if(property.getKey().startsWith(AttributeDTO.PROPERTY_PREFIX) &&
					property.getValue() != null) {

				SqlInsert.insertInto("AttributeValue")
				.value("AttributeId", AttributeDTO.idForPropertyName(property.getKey()))
				.value("SiteId", cmd.getSiteId())
				.value("Value", property.getValue())
				.execute(tx);
			}
		}
	}

	private int insertReportingPeriod(
			SqlTransaction tx,
			CreateSite cmd)  {
		
		int reportingPeriodId = cmd.getReportingPeriodId();
		SqlInsert.insertInto("ReportingPeriod")
			.value("ReportingPeriodId", reportingPeriodId)
			.value("SiteId", cmd.getSiteId())
			.value("Date1", cmd.getProperties().get("date1"))
			.value("Date2", cmd.getProperties().get("date2"))
			.value("DateCreated", new Date())
			.value("DateEdited", new Date())
		.execute(tx);

		insertIndicatorValues(tx, cmd);
		
		return reportingPeriodId;
	}
	
	private void insertIndicatorValues(SqlTransaction tx, CreateSite cmd) {
		for(Entry<String,Object> property : cmd.getProperties().getTransientMap().entrySet()) {
			if(property.getKey().startsWith(IndicatorDTO.PROPERTY_PREFIX) &&
					property.getValue() != null) {

				SqlInsert.insertInto("IndicatorValue")
				.value("IndicatorId", IndicatorDTO.indicatorIdForPropertyName(property.getKey()))
				.value("ReportingPeriodId", cmd.getReportingPeriodId())
				.value("Value", property.getValue())
				.execute(tx);
			}
		}
	}

}
