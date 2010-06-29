/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.json.JSONException;
import org.sigmah.server.domain.User;
import org.sigmah.server.sync.AdminUpdateBuilder;
import org.sigmah.server.sync.LocationUpdateBuilder;
import org.sigmah.server.sync.SchemaUpdateBuilder;
import org.sigmah.server.sync.UpdateBuilder;
import org.sigmah.shared.command.GetSyncRegionUpdates;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;

public class GetSyncRegionUpdatesHandler implements CommandHandler<GetSyncRegionUpdates> {

    private final Injector injector;

    @Inject
    public GetSyncRegionUpdatesHandler(Injector injector) {
        this.injector = injector;
    }

    @Override
    public CommandResult execute(GetSyncRegionUpdates cmd, User user) throws CommandException {

        UpdateBuilder builder;

        if(cmd.getRegionId().equals("schema")) {
            builder = injector.getInstance(SchemaUpdateBuilder.class);

        } else if(cmd.getRegionId().startsWith("admin/")) {
            builder = injector.getInstance(AdminUpdateBuilder.class);

        } else if(cmd.getRegionId().equals("locations")) {
            builder = injector.getInstance(LocationUpdateBuilder.class);

        } else {
            throw new CommandException("Unknown sync region: " + cmd.getRegionId());
        }

        try {
            return builder.build(user, cmd);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
