package org.activityinfo.shared.command.handler;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.activityinfo.shared.command.UpdateSite;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.AttributeDTO;
import org.activityinfo.shared.dto.IndicatorDTO;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UpdateSiteHandler implements CommandHandlerAsync<UpdateSite, VoidResult> {


	@Override
	public void execute(final UpdateSite command, ExecutionContext context,
			final AsyncCallback<VoidResult> callback) {

		final Map<String, Object> changes = command.getChanges().getTransientMap();
		SqlTransaction tx = context.getTransaction();
		updateSiteProperties(tx, command, changes);
		updateAttributeValues(tx, command.getSiteId(), changes);
		//updateLocation(tx, command.getSiteId(), changes);
		updateReportingPeriod(tx, command.getSiteId(), changes);
	
		callback.onSuccess(new VoidResult());
	}
	
	private void updateSiteProperties(SqlTransaction tx, UpdateSite command, Map<String, Object> changes) {
		SqlUpdate.update("site")
			.where("SiteId", command.getSiteId())
			.value("date1", changes)
			.value("date2", changes)
			.value("comments", changes)
			.value("projectId", changes)
			.value("partnerId", changes)
			.value("locationId", changes)
			.value("timeEdited", new Date().getTime())
			.execute(tx);
	}

	private void updateAttributeValues(SqlTransaction tx, int siteId, Map<String, Object> changes) {
		for(Entry<String, Object> change : changes.entrySet()) {
			if(change.getKey().startsWith(AttributeDTO.PROPERTY_PREFIX)) {
				int attributeId = AttributeDTO.idForPropertyName(change.getKey());
				Boolean value = (Boolean)change.getValue();
				
				SqlUpdate.delete(Tables.ATTRIBUTE_VALUE)
					.where("attributeId", attributeId)
					.where("siteId", siteId)
					.execute(tx);
				
				if(value != null) {
					SqlInsert.insertInto(Tables.ATTRIBUTE_VALUE)
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
			.from(Tables.REPORTING_PERIOD)
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
		
		SqlUpdate.update(Tables.REPORTING_PERIOD)
		.where("reportingPeriodId", reportingPeriodId)
		.value("date1", changes)
		.value("date2", changes)
		.execute(tx);
		
		
        for (Map.Entry<String, Object> change : changes.entrySet()) {
        	if(change.getKey().startsWith(IndicatorDTO.PROPERTY_PREFIX)) {
        		int indicatorId = IndicatorDTO.indicatorIdForPropertyName(change.getKey());
        		Double value = (Double)change.getValue();
        		
        		SqlUpdate.delete(Tables.INDICATOR_VALUE)
        		.where("reportingPeriodId", reportingPeriodId)
        		.where("indicatorId", indicatorId)
        		.execute(tx);
        		
        		if(value != null) {
        			SqlInsert.insertInto(Tables.INDICATOR_VALUE)
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
		
		if(hasAdminUpdates(properties)) {
			
			SqlUpdate.delete(Tables.LOCATION_ADMIN_LINK)
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

	private boolean hasAdminUpdates(Map<String, Object> changes) {
		for(String propertyName : changes.keySet()) {
			if(propertyName.startsWith(AdminLevelDTO.PROPERTY_PREFIX)) {
				return true;
			}
		}
		return false;
	}
	
}
