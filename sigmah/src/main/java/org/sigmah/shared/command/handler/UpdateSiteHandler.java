package org.sigmah.shared.command.handler;

import java.util.Map;
import java.util.Map.Entry;

import org.sigmah.shared.command.UpdateSite;
import org.sigmah.shared.command.result.VoidResult;
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
import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class UpdateSiteHandler implements CommandHandlerAsync<UpdateSite, VoidResult> {

	private SqlDatabase database;
	
	@Inject
	public UpdateSiteHandler(SqlDatabase database) {
		super();
		this.database = database;
	}

	@Override
	public void execute(final UpdateSite command, ExecutionContext context,
			final AsyncCallback<VoidResult> callback) {

		final Map<String, Object> changes = command.getChanges().getTransientMap();
		SqlTransaction tx = context.getTransaction();
		updateSiteProperties(tx, command, changes);
		updateAttributeValues(tx, command.getSiteId(), changes);
		updateLocation(tx, command.getSiteId(), changes);
		updateReportingPeriod(tx, command.getSiteId(), changes);
	
		callback.onSuccess(new VoidResult());
	}
	
	private void updateSiteProperties(SqlTransaction tx, UpdateSite command, Map<String, Object> changes) {
	
		SqlUpdate.update("Site")
			.where("SiteId", command.getSiteId())
			.value("date1", changes)
			.value("date2", changes)
			.value("comments", changes)
			.value("projectId", changes)
			.value("partnerId", changes)
			.execute(tx);

	}


	private void updateLocation(SqlTransaction tx,
			final int siteId, final Map<String, Object> changes) {
		
		SqlQuery.select("locationId", "boundAdminLevelId")
			.from("Location")
			.leftJoin("LocationType").on("LocationType.LocationTypeId=Location.LocationTypeId")
			.where("locationId").in(
					SqlQuery.select("locationId").from("Site").where("siteId").equalTo(siteId))
			.execute(tx, new SqlResultCallback() {
				
				@Override
				public void onSuccess(final SqlTransaction tx, SqlResultSet results) {
					SqlResultSetRow row = results.getRow(0);
					final int locationId = row.getInt("locationId");
					Integer boundAdminLevelId = row.isNull("boundAdminLevelId") ? null : row.getInt("boundAdminLevelId");
					
					lookupNewName(tx, boundAdminLevelId, changes, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// doesn't get called
						}

						@Override
						public void onSuccess(String result) {
							updateLocation(tx, locationId, result, changes);
						}							
					});
					
				}
			});
	}

	private void lookupNewName(SqlTransaction tx, 
			Integer boundAdminLevelId, final Map<String, Object> changes,
			final AsyncCallback<String> callback) {
	
		if(boundAdminLevelId == null) {
			callback.onSuccess((String)changes.get("locationName"));
			
		} else if(!changes.containsKey(AdminLevelDTO.getPropertyName(boundAdminLevelId))) {
			// no update
			callback.onSuccess(null);
			
		} else {
		
			int newEntityId = (Integer) changes.get(AdminLevelDTO.getPropertyName(boundAdminLevelId));
			
			SqlQuery.select("name")
				.from("AdminEntity")
				.where("AdminEntityId").equalTo(newEntityId)
				.execute(tx, new SqlResultCallback() {
					
					@Override
					public void onSuccess(SqlTransaction tx, SqlResultSet results) {
						callback.onSuccess(results.getRow(0).getString("name"));
					}
				});		
		}
		
	
	}

	private void updateLocation(SqlTransaction tx, int locationId, String newName, Map<String, Object> changes) {

		SqlUpdate.update("Location")
		.where("locationId", locationId)
		.valueIfNotNull("name", newName)
		.value("axe", changes, "locationAxe")
		.value("x", changes)
		.value("y", changes)
		.execute(tx);
		
		updateLocationAdminLinks(tx, locationId, changes);
		
	}
	
	private void updateAttributeValues(SqlTransaction tx, int siteId, Map<String, Object> changes) {
		for(Entry<String, Object> change : changes.entrySet()) {
			if(change.getKey().startsWith(AttributeDTO.PROPERTY_PREFIX)) {
				int attributeId = AttributeDTO.idForPropertyName(change.getKey());
				Boolean value = (Boolean)change.getValue();
				
				SqlUpdate.delete("AttributeValue")
					.where("attributeId", attributeId)
					.where("siteId", siteId)
					.execute(tx);
				
				if(value != null) {
					SqlInsert.insertInto("AttributeValue")
						.value("attributeId", attributeId)
						.value("siteId", siteId)
						.value("value", value)
						.execute(tx);
				}
			}
		}
	}
	

	private void updateReportingPeriod(SqlTransaction tx,
			final int siteId, final Map<String, Object> changes) {
		SqlQuery.select("reportingPeriodId")
			.from("ReportingPeriod")
			.where("siteId").equalTo(siteId)
			.execute(tx, new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					if(results.getRows().size() == 1) {
						updateReportingPeriod(tx, siteId, results.getRow(0).getInt("reportingPeriodId"), changes);
					}
				}
			});
	}

	private void updateReportingPeriod(SqlTransaction tx,
			int siteId, int reportingPeriodId,
			Map<String, Object> changes) {
		
		SqlUpdate.update("ReportingPeriod")
		.where("reportingPeriodId", reportingPeriodId)
		.value("date1", changes)
		.value("date2", changes)
		.execute(tx);
		
		
        for (Map.Entry<String, Object> change : changes.entrySet()) {
        	if(change.getKey().startsWith(IndicatorDTO.PROPERTY_PREFIX)) {
        		int indicatorId = IndicatorDTO.indicatorIdForPropertyName(change.getKey());
        		Double value = (Double)change.getValue();
        		
        		SqlUpdate.delete("IndicatorValue")
        		.where("reportingPeriodId", reportingPeriodId)
        		.where("indicatorId", indicatorId)
        		.execute(tx);
        		
        		if(value != null) {
        			SqlInsert.insertInto("IndicatorValue")
        			.value("reportingPeriodId", reportingPeriodId)
        			.value("indicatorId", indicatorId)
        			.value("value", value)
        			.execute(tx);
        		}
        	}
        }
	}

	
	private void updateLocationAdminLinks (
			SqlTransaction tx,
			int locationId, 
			Map<String, Object> properties)  {
		
		// admin entity membership is not updated invidually,
		// it must be updated in totality for a given site to maintain
		// consistency
		
		SqlUpdate.delete("LocationAdminLink")
			.where("locationId", locationId)
			.execute(tx);
		
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

}
