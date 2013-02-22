

package org.activityinfo.server.command.handler;

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

import java.util.logging.Logger;

import org.activityinfo.client.Log;
import org.activityinfo.server.command.handler.sync.AdminUpdateBuilder;
import org.activityinfo.server.command.handler.sync.LocationUpdateBuilder;
import org.activityinfo.server.command.handler.sync.SchemaUpdateBuilder;
import org.activityinfo.server.command.handler.sync.SiteTableUpdateBuilder;
import org.activityinfo.server.command.handler.sync.SiteUpdateBuilder;
import org.activityinfo.server.command.handler.sync.UpdateBuilder;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.GetSyncRegionUpdates;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.json.JSONException;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class GetSyncRegionUpdatesHandler implements CommandHandler<GetSyncRegionUpdates> {

	private static final Logger LOGGER = Logger.getLogger(GetSyncRegionsHandler.class.getName());
	
    private final Injector injector;

    @Inject
    public GetSyncRegionUpdatesHandler(Injector injector) {
        this.injector = injector;
    }

    @Override
    public CommandResult execute(GetSyncRegionUpdates cmd, User user) throws CommandException {

    	Log.info("Fetching updates for " + cmd.getRegionId() + ", localVersion = " + cmd.getLocalVersion());
    	
        UpdateBuilder builder;

        if(cmd.getRegionId().equals("schema")) {
            builder = injector.getInstance(SchemaUpdateBuilder.class);

        } else if(cmd.getRegionId().startsWith("admin/")) {
            builder = injector.getInstance(AdminUpdateBuilder.class);

        } else if(cmd.getRegionId().startsWith("location/")) {
            builder = injector.getInstance(LocationUpdateBuilder.class);

        } else if(cmd.getRegionId().startsWith("site/")) {
            builder = injector.getInstance(SiteUpdateBuilder.class);

        } else if(cmd.getRegionId().equals("site-tables")) {
            builder = injector.getInstance(SiteTableUpdateBuilder.class);

        } else {
            throw new CommandException("Unknown sync region: " + cmd.getRegionId());
        }

        try {
            return builder.build(user, cmd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
