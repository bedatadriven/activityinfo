package org.activityinfo.shared.command.handler;

import java.util.Date;
import java.util.Map.Entry;

import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.AttributeDTO;
import org.activityinfo.shared.dto.IndicatorDTO;

import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
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

	private void insertSite(
			SqlTransaction tx,
			CreateSite cmd)  {

		RpcMap properties = cmd.getProperties();
		
		// deal with the possibility that we've already received this command
		// but its completion was not acknowledged because of network problems
		tx.executeSql("delete from indicatorvalue Where ReportingPeriodId in " +
				"(select reportingperiodid from reportingperiod where siteid=" + cmd.getSiteId() + ")");
		SqlUpdate.delete(Tables.REPORTING_PERIOD)
				 .where("SiteId", cmd.getSiteId())
				 .execute(tx);
		SqlUpdate.delete(Tables.SITE)
				 .where("SiteId", cmd.getSiteId())
				 .execute(tx);
		
		
		SqlInsert.insertInto(Tables.SITE)
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
			.value("timeEdited", new Date().getTime())
		.execute(tx);

		insertAttributeValues(tx, cmd);
	}
		

	private void insertAttributeValues(
			SqlTransaction tx, CreateSite cmd)  {

		for(Entry<String,Object> property : cmd.getProperties().getTransientMap().entrySet()) {
			if(property.getKey().startsWith(AttributeDTO.PROPERTY_PREFIX) &&
					property.getValue() != null) {

				SqlInsert.insertInto(Tables.ATTRIBUTE_VALUE)
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
		SqlInsert.insertInto(Tables.REPORTING_PERIOD)
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

				SqlInsert.insertInto(Tables.INDICATOR_VALUE)
				.value("IndicatorId", IndicatorDTO.indicatorIdForPropertyName(property.getKey()))
				.value("ReportingPeriodId", cmd.getReportingPeriodId())
				.value("Value", property.getValue())
				.execute(tx);
			}
		}
	}
}
