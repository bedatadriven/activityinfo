package org.sigmah.client.offline.command.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import org.sigmah.client.offline.command.CommandQueue;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dao.SqlInsertBuilder;
import org.sigmah.shared.dao.SqlQueryBuilder;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AttributeDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

public class LocalCreateEntityHandler implements PartialCommandHandler<CreateEntity> {
   
	
	private Connection connection;
	private KeyGenerator keyGenerator;
	private CommandQueue queue;

	@Inject
	public LocalCreateEntityHandler(Connection connection, KeyGenerator keyGenerator, CommandQueue queue) {
		this.connection = connection;
		this.keyGenerator = keyGenerator;
		this.queue = queue;
	}
	
	
	@Override
	public boolean canExecute(CreateEntity cmd) {
		return cmd.getEntityName().equals(SiteDTO.ENTITY_NAME);
	}

	@Override
	public CreateResult execute(CreateEntity cmd, User user)
			throws CommandException {
		
		try {
			if(cmd.getEntityName().equals(SiteDTO.ENTITY_NAME)) {
				return new CreateResult(createSite(cmd));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		throw new IllegalArgumentException("Not supported offline");
		
	}

	private int createSite(CreateEntity cmd) throws SQLException {

		
		Map<String,Object> properties = cmd.getProperties().getTransientMap();
		int activityId = (Integer) properties.get("activityId");
	
		
		
		// look up the Activity Entity so we can get the corresponding location
		// type
		
		ResultSet rs = SqlQueryBuilder.select("LocationTypeId", "ReportingFrequency")
			.from("Activity")
			.where("ActivityId").equalTo(activityId)
			.executeQuery(connection);
		
		int locationTypeId = rs.getInt(1);
		int reportingFrequency = rs.getInt(2);
		
		
		// insert a new location object
		int locationId = keyGenerator.generateInt();
		SqlInsertBuilder.insertInto("Location")
			.value("LocationId", locationId)
			.value("Name", properties.get("locationName"))
			.value("Axe", properties.get("locationAxe"))
			.value("X", properties.get("x"))
			.value("Y", properties.get("y"))
			.value("LocationTypeId", locationTypeId)
			.execute(connection);
		
		// update command for remote consumption with new ids
		cmd.getProperties().put("locationId", locationId);
		
			
		// create the site 
		int siteId = keyGenerator.generateInt();
		SqlInsertBuilder.insertInto("Site")
			.value("SiteId", siteId)
			.value("LocationId", locationId)
			.value("ActivityId", activityId)
			.value("Date1", properties.get("date1"))
			.value("Date2", properties.get("date2"))
			.value("Comments", properties.get("comments"))
			.value("PartnerId", properties.get("partnerId"))
			.execute(connection);
		
		// save for remote consumption
		cmd.getProperties().put("siteId", siteId);

		
		// insert the attribute values 
		for(Entry<String,Object> property : properties.entrySet()) {
			if(property.getKey().startsWith(AttributeDTO.PROPERTY_PREFIX) &&
					property.getValue() != null) {
			
				SqlInsertBuilder.insertInto("AttributeValue")
					.value("AttributeId", AttributeDTO.idForPropertyName(property.getKey()))
					.value("SiteId", siteId)
					.value("Value", property.getValue())
					.execute(connection);
			}
		}
		
		// we only create a reporting period if this is a one-off activity
		if(reportingFrequency == ActivityDTO.REPORT_ONCE) {
			int reportingPeriodId = keyGenerator.generateInt();
			SqlInsertBuilder.insertInto("ReportingPeriod")
				.value("ReportingPeriodId", reportingPeriodId)
				.value("SiteId", siteId)
				.value("Date1", properties.get("date1"))
				.value("Date2", properties.get("date2"))
				.execute(connection);
			
			// insert the indicator values 
			for(Entry<String,Object> property : properties.entrySet()) {
				if(property.getKey().startsWith(IndicatorDTO.PROPERTY_PREFIX) &&
						property.getValue() != null) {
				
					SqlInsertBuilder.insertInto("IndicatorValue")
						.value("IndicatorId", IndicatorDTO.indicatorIdForPropertyName(property.getKey()))
						.value("ReportingPeriodId", reportingPeriodId)
						.value("Value", property.getValue())
						.execute(connection);
				}
			}
			
			// save the id for the remote end
			cmd.getProperties().put("reportingPeriodId", reportingPeriodId);
		}

		queue.queue(cmd);
		
		connection.commit();
		
		return siteId;
	}
	
}
