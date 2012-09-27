package org.activityinfo.shared.command.handler;

import java.util.Date;

import org.activityinfo.shared.command.CreateLocation;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.AdminLevelDTO;

import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
import com.extjs.gxt.ui.client.data.RpcMap;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CreateLocationHandler implements CommandHandlerAsync<CreateLocation, VoidResult>{

	@Override
	public void execute(CreateLocation command, ExecutionContext context,
			AsyncCallback<VoidResult> callback) {

		Date timestamp = new Date();

		RpcMap properties = command.getProperties();
		
		// We need to handle the case in which the command is sent twice to
		// the server
		SqlUpdate.delete(Tables.LOCATION)
			.where("LocationId", properties.get("id"))
			.execute(context.getTransaction());
		SqlUpdate.delete(Tables.LOCATION_ADMIN_LINK)
			.where("LocationId", properties.get("id"))
			.execute(context.getTransaction());
	
		SqlInsert.insertInto("location")
			.value("LocationId", properties.get("id"))
			.value("LocationTypeId", properties.get("locationTypeId"))
			.value("Name", properties.get("name"))
			.value("Axe", properties.get("axe"))
		    .value("X", properties.get("longitude"))
		    .value("Y", properties.get("latitude"))
			.value("timeEdited", timestamp.getTime())
		    .execute(context.getTransaction());
		
		for(String property : properties.keySet()) {
			if(property.startsWith(AdminLevelDTO.PROPERTY_PREFIX)) {
				SqlInsert.insertInto("locationadminlink")
				.value("LocationId", properties.get("id"))
				.value("AdminEntityId", properties.get(property))
			    .execute(context.getTransaction());
			}
		}
		callback.onSuccess(null);
	}
}
