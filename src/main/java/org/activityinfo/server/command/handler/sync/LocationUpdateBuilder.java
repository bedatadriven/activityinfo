package org.activityinfo.server.command.handler.sync;

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

import javax.persistence.EntityManager;

import org.activityinfo.server.database.hibernate.entity.Location;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.GetSyncRegionUpdates;
import org.activityinfo.shared.command.result.SyncRegionUpdate;
import org.activityinfo.shared.db.Tables;
import org.json.JSONException;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.inject.Inject;

public class LocationUpdateBuilder implements UpdateBuilder {
    private static final String REGION_PREFIX = "location/";

    private final EntityManager em;
    private LocalState localState;
    private SqliteBatchBuilder batch;

    private int typeId;

    @Inject
    public LocationUpdateBuilder(EntityManager em) {
        this.em = em;
    }

    @Override
    public SyncRegionUpdate build(User user, GetSyncRegionUpdates request)
        throws Exception {

        typeId = parseTypeId(request);
        localState = new LocalState(request.getLocalVersion());
        batch = new SqliteBatchBuilder();

        long latestVersion = queryLatestVersion();
        if (latestVersion > localState.lastDate) {
            queryChanged();
            linkAdminEntities();
        }

        SyncRegionUpdate update = new SyncRegionUpdate();
        update.setVersion(Long.toString(latestVersion));
        update.setSql(batch.build());
        update.setComplete(true);
        return update;
    }

    private int parseTypeId(GetSyncRegionUpdates request) {
        if (!request.getRegionId().startsWith(REGION_PREFIX)) {
            throw new AssertionError("Expected region prefixed by '"
                + REGION_PREFIX +
                "', got '" + request.getRegionId() + "'");
        }
        return Integer.parseInt(request.getRegionId().substring(
            REGION_PREFIX.length()));
    }

    private void queryChanged() {

        SqlQuery query = SqlQuery.select()
            .appendColumn("LocationId")
            .appendColumn("Name")
            .appendColumn("Axe")
            .appendColumn("X")
            .appendColumn("Y")
            .appendColumn("LocationTypeId")
            .from(Tables.LOCATION)
            .where("locationTypeId").equalTo(typeId)
            .where("timeEdited").greaterThan(localState.lastDate);

        batch.insert()
            .into(Tables.LOCATION)
            .from(query)
            .execute(em);
    }

    private void linkAdminEntities() throws JSONException {

        SqlQuery query = SqlQuery.select()
            .appendColumn("K.AdminEntityId")
            .appendColumn("K.LocationId")
            .from(Tables.LOCATION, "L")
            .innerJoin(Tables.LOCATION_ADMIN_LINK, "K")
            .on("L.LocationId=K.LocationId")
            .where("L.locationTypeId").equalTo(typeId)
            .where("L.timeEdited").greaterThan(localState.lastDate);

        batch.insert()
            .into(Tables.LOCATION_ADMIN_LINK)
            .from(query)
            .execute(em);
    }

    private long queryLatestVersion() throws JSONException {
        SqlQuery query = SqlQuery.select()
            .appendColumn("MAX(timeEdited)", "latest")
            .from(Tables.LOCATION)
            .where("locationTypeId").equalTo(typeId)
            .where("timeEdited").greaterThan(localState.lastDate);

        return SqlQueryUtil.queryLong(em, query);
    }

    private class LocalState {
        private long lastDate;

        public LocalState(Location lastLocation) {
            lastDate = lastLocation.getTimeEdited();
        }

        public LocalState(String cookie) {
            if (cookie == null) {
                lastDate = 0;
            } else {
                lastDate = TimestampHelper.fromString(cookie);
            }
        }

        public String toVersionString() {
            return TimestampHelper.toString(lastDate);
        }
    }
}
