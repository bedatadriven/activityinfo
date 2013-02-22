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

import org.activityinfo.shared.command.CreateLocation;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.AdminLevelDTO;

import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
import com.extjs.gxt.ui.client.data.RpcMap;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CreateLocationHandler implements
    CommandHandlerAsync<CreateLocation, VoidResult> {

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

        for (String property : properties.keySet()) {
            if (property.startsWith(AdminLevelDTO.PROPERTY_PREFIX)) {
                SqlInsert.insertInto("locationadminlink")
                    .value("LocationId", properties.get("id"))
                    .value("AdminEntityId", properties.get(property))
                    .execute(context.getTransaction());
            }
        }
        callback.onSuccess(null);
    }
}
