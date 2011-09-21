package org.sigmah.shared.command.handler;

import java.util.Date;

import org.sigmah.client.offline.command.handler.KeyGenerator;
import org.sigmah.shared.command.AddLocation;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.LocationDTO2;

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
		LocationDTO2 newLocation = command.getLocation();
		
		SqlInsert.insertInto("Location")
			.value("LocationId", locationId)
			.value("Name", newLocation.getName())
			.value("Axe", newLocation.getAxe())
			.value("Y", newLocation.getLatitude())
			.value("X", newLocation.getLongitude())
			.value("LocationTypeId", newLocation.getLocationTypeId())
			.value("dateCreated", timestamp)
			.value("dateEdited", timestamp)
			.execute(context.getTransaction());

		callback.onSuccess(new CreateResult(locationId));
	}
}
