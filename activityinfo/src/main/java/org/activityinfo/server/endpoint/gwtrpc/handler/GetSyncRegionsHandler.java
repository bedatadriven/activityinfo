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
import org.activityinfo.server.domain.*;
import org.activityinfo.shared.command.GetSyncRegions;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.SyncRegions;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.sync.SyncRegion;

import java.util.*;

public class GetSyncRegionsHandler implements CommandHandler<GetSyncRegions> {

    private SchemaDAO schemaDAO;

    @Inject
    public GetSyncRegionsHandler(SchemaDAO schemaDAO) {
        this.schemaDAO = schemaDAO;
    }

    @Override
    public CommandResult execute(GetSyncRegions cmd, User user) throws CommandException {

        List<UserDatabase> databases = schemaDAO.getDatabases(user);

        List<SyncRegion> regions = new ArrayList<SyncRegion>();
        regions.add(new SyncRegion("schema", true));
        regions.addAll(listAdminRegions(databases));
        regions.add(new SyncRegion("locations"));

        return new SyncRegions(regions);
    }


    private Collection<? extends SyncRegion> listAdminRegions(List<UserDatabase> databases) {
        List<SyncRegion> adminRegions = new ArrayList<SyncRegion>();
        Set<Integer> countriesAdded = new HashSet<Integer>();
        for(UserDatabase db : databases) {
            if(!countriesAdded.contains(db.getCountry().getId())) {                                
                for(AdminLevel level : db.getCountry().getAdminLevels()) {
                    adminRegions.add(new SyncRegion("admin/" + level.getId(), true));
                }
                countriesAdded.add(db.getCountry().getId());
            }
        }
        return adminRegions;
    }
}
