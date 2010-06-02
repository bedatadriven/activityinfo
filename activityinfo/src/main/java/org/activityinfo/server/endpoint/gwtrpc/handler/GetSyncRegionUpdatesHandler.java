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

package org.activityinfo.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.sync.SchemaUpdateBuilder;
import org.activityinfo.shared.command.GetSyncRegionUpdates;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.SyncRegionUpdate;
import org.activityinfo.shared.exception.CommandException;

public class GetSyncRegionUpdatesHandler implements CommandHandler<GetSyncRegionUpdates> {

    private final SchemaDAO schemaDAO;

    @Inject
    public GetSyncRegionUpdatesHandler(SchemaDAO schemaDAO) {
        this.schemaDAO = schemaDAO;
    }


    @Override
    public CommandResult execute(GetSyncRegionUpdates cmd, User user) throws CommandException {
        SyncRegionUpdate updates = new SyncRegionUpdate();
        updates.setRegionId(cmd.getRegionId());

        if(cmd.getRegionId().equals("schema")) {
            SchemaUpdateBuilder builder = new SchemaUpdateBuilder(schemaDAO);
            return builder.build(user);
        }
        throw new CommandException("Unknown sync region: " + cmd.getRegionId());
    }

}
