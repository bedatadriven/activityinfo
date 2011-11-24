package org.sigmah.shared.command.handler;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.sigmah.client.offline.command.handler.KeyGenerator;
import org.sigmah.shared.command.AddLocation;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.LocationDTO;

import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class AddLocationHandler implements CommandHandlerAsync<AddLocation, CreateResult> {
	private KeyGenerator keyGenerator;

	@Inject
	public AddLocationHandler(KeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}
	
	@Override
	public void execute(AddLocation command, ExecutionContext context, AsyncCallback<CreateResult> callback) {
		int locationId = keyGenerator.generateInt();
		Date timestamp = new Date();
		LocationDTO newLocation = command.getLocation();
		
		SqlInsert.insertInto("Location")
			.value("LocationID", locationId)
			.value("Name", newLocation.getName())
			.value("Axe", newLocation.getAxe())
			.value("Y", newLocation.getLatitude())
			.value("X", newLocation.getLongitude())
			.value("LocationTypeId", newLocation.getLocationTypeId())
			.value("dateCreated", timestamp)
			.value("dateEdited", timestamp)
			.execute(context.getTransaction());

		insertLocationAdminLinks(context.getTransaction(), locationId, newLocation.getProperties());
		
		callback.onSuccess(new CreateResult(locationId));
	}
	private void insertLocationAdminLinks(
			SqlTransaction tx,
			int locationId, 
			Map<String, Object> properties)  {
		
		for(Entry<String,Object> property : properties.entrySet()) {
			if(property.getKey().startsWith(AdminLevelDTO.PROPERTY_PREFIX)) {
				AdminEntityDTO entity =  (AdminEntityDTO) property.getValue();
				if(entity != null) {
					SqlInsert.insertInto("LocationAdminLink")
						.value("AdminEntityId", entity.getId())
						.value("Locationid", locationId)
						.execute(tx);
				}
			}
		}
	}
}
