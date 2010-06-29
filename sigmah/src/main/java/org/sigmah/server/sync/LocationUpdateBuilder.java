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

package org.sigmah.server.sync;

import com.bedatadriven.rebar.sync.server.JpaUpdateBuilder;
import com.google.inject.Inject;
import org.json.JSONException;
import org.sigmah.server.domain.AdminEntity;
import org.sigmah.server.domain.Location;
import org.sigmah.server.domain.User;
import org.sigmah.shared.command.GetSyncRegionUpdates;
import org.sigmah.shared.command.result.SyncRegionUpdate;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationUpdateBuilder implements UpdateBuilder {

    private final EntityManager em;
    private List<Location> locations;
    private List<Location> createdLocations = new ArrayList<Location>();
    private List<Location> updatedLocations = new ArrayList<Location>();

    @Inject
    public LocationUpdateBuilder(EntityManager em) {
        this.em = em;
    }

    @Override
    public SyncRegionUpdate build(User user, GetSyncRegionUpdates request) throws JSONException {

        Date lastUpdate = parseLocalState(request);

        locations = em.createQuery("select l from Location l where l.dateEdited > :localVersion" +
                " order by l.dateEdited")
                .setParameter("localVersion", lastUpdate)
                .getResultList();


        for(Location location : locations) {
            if(location.getDateCreated().after(lastUpdate)) {
                createdLocations.add(location);
            } else {
                updatedLocations.add(location);
            }
        }

        SyncRegionUpdate update = new SyncRegionUpdate();
        update.setVersion(locations.isEmpty() ? request.getLocalVersion() : lastVersion());
        update.setSql(makeJson());
        return update;
    }

    private Date parseLocalState(GetSyncRegionUpdates request) {
        if(request.getLocalVersion() == null) {
            return new Date(0L);
        } else {
            return new Date(Long.parseLong(request.getLocalVersion()));
        }
    }

    private String lastVersion() {
        if(locations.isEmpty()) {
            return "0";
        }

        return Long.toString(locations.get(locations.size()-1).getDateEdited().getTime());
    }

    private String makeJson() throws JSONException {
        JpaUpdateBuilder builder = new JpaUpdateBuilder();
        builder.createTableIfNotExists(Location.class);
        builder.executeStatement("create table if not exists LocationAdminLink (LocationId integer, AdminEntityId integer)");

        builder.insert(Location.class, createdLocations);
        builder.update(Location.class, updatedLocations);

        builder.beginPreparedStatement("delete from LocationAdminLink where LocationId = ?");
        for(Location l : locations) {
            builder.addExecution(l.getId());
        }
        builder.finishPreparedStatement();

        builder.beginPreparedStatement("insert into LocationAdminLink (LocationId, AdminEntityId) values (?, ?)");
        for(Location l : locations) {
            for(AdminEntity e : l.getAdminEntities()) {
                builder.addExecution(l.getId(), e.getId());
            }
        }
        builder.finishPreparedStatement();

        return builder.asJson();
    }


}
