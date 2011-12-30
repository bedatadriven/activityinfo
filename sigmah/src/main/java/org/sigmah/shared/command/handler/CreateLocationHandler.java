package org.sigmah.shared.command.handler;

import java.util.Date;

import org.sigmah.shared.command.CreateLocation;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.AdminLevelDTO;

import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.extjs.gxt.ui.client.data.RpcMap;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CreateLocationHandler implements CommandHandlerAsync<CreateLocation, VoidResult>{

	@Override
	public void execute(CreateLocation command, ExecutionContext context,
			AsyncCallback<VoidResult> callback) {

		Date timestamp = new Date();
		
		RpcMap properties = command.getProperties();
		SqlInsert.insertInto("Location")
			.value("LocationId", properties.get("id"))
			.value("LocationTypeId", properties.get("locationTypeId"))
			.value("Name", properties.get("name"))
			.value("Axe", properties.get("axe"))
		    .value("X", properties.get("longitude"))
		    .value("Y", properties.get("latitude"))
		    .value("dateCreated", timestamp)
			.value("dateEdited", timestamp)
			.value("timeEdited", timestamp.getTime())
		    .execute(context.getTransaction());
		
		for(String property : properties.keySet()) {
			if(property.startsWith(AdminLevelDTO.PROPERTY_PREFIX)) {
				SqlInsert.insertInto("LocationAdminLink")
				.value("LocationId", properties.get("id"))
				.value("AdminEntityId", properties.get(property))
			    .execute(context.getTransaction());
			}
		}
		callback.onSuccess(null);
	}
}
