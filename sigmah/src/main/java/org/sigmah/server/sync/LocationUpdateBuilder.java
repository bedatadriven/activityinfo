/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.sync;

import com.bedatadriven.rebar.sync.server.JpaUpdateBuilder;
import com.google.inject.Inject;
import org.json.JSONException;
import org.sigmah.shared.command.GetSyncRegionUpdates;
import org.sigmah.shared.command.result.SyncRegionUpdate;
import org.sigmah.shared.domain.Location;
import org.sigmah.shared.domain.User;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocationUpdateBuilder implements UpdateBuilder {
    public static final int MAX_UPDATES = 25;

    private final EntityManager em;
    private List<Location> locations;
    private Set<Integer> locationIds = new HashSet<Integer>();
    private List<Location> createdLocations = new ArrayList<Location>();
    private List<Location> updatedLocations = new ArrayList<Location>();

    @Inject
    public LocationUpdateBuilder(EntityManager em) {
        this.em = em;
    }

    @Override
    public SyncRegionUpdate build(User user, GetSyncRegionUpdates request) throws JSONException {

        LocalState localState = new LocalState(request.getLocalVersion());

        if(request.getLocalVersion() == null) {
            locations = em.createQuery("select loc from Location loc order by loc.dateEdited, loc.id")
                    .setMaxResults(MAX_UPDATES)
                    .getResultList();
        } else {
            locations = em.createQuery("select loc from Location loc where loc.dateEdited > :localDate or " +
                            "(loc.dateEdited == :localDate and loc.id > :lastId) " +
                            "order by loc.dateEdited, loc.id")
                    .setParameter("localDate", localState.lastDate)
                    .setParameter("lastId", localState.lastId)
                    .setMaxResults(MAX_UPDATES)
                    .getResultList();
        }


        for(Location location : locations) {
            if(TimestampHelper.isAfter(location.getDateCreated(), localState.lastDate)) {
                createdLocations.add(location);
            } else {
                updatedLocations.add(location);
            }
            locationIds.add(location.getId());
        }

        SyncRegionUpdate update = new SyncRegionUpdate();
        update.setVersion(locations.isEmpty() ? request.getLocalVersion() : lastVersion());
        update.setSql(makeJson());
        update.setComplete(locations.size() < MAX_UPDATES);
        return update;
    }

    private String lastVersion() {
        if(locations.isEmpty()) {
            return "0,0";
        }

        return new LocalState(locations.get(locations.size()-1)).toVersionString();
    }

    private String makeJson() throws JSONException {
        JpaUpdateBuilder builder = new JpaUpdateBuilder();
        builder.createTableIfNotExists(Location.class);
        builder.executeStatement("create table if not exists LocationAdminLink (LocationId integer, AdminEntityId integer)");

        builder.insert(Location.class, createdLocations);
        builder.update(Location.class, updatedLocations);

        builder.beginPreparedStatement("delete from LocationAdminLink where LocationId = ?");
        for(Location updateLoc : updatedLocations) {
            builder.addExecution(updateLoc.getId());
        }
        builder.finishPreparedStatement();

        if(!locations.isEmpty()) {
            List<Object[]> joins = em.createQuery("SELECT loc.id, e.id FROM Location loc JOIN loc.adminEntities e WHERE loc.id IN (:ids)")
                    .setParameter("ids", locationIds)
                    .getResultList();


            builder.beginPreparedStatement("insert into LocationAdminLink (LocationId, AdminEntityId) values (?, ?)");
            for(Object[] join : joins) {
                builder.addExecution(join[0], join[1]);
            }
            builder.finishPreparedStatement();
        }

        return builder.asJson();
    }

    private class LocalState {
        private Timestamp lastDate;
        private int lastId;

        public LocalState(Location lastLocation) {
            lastDate = (Timestamp) lastLocation.getDateEdited();
            lastId = lastLocation.getId();
        }

        public LocalState(String cookie) {
            if(cookie == null) {
                lastDate = new Timestamp(0);
                lastId = Integer.MIN_VALUE;
            } else {
                String[] parts = cookie.split(",");
                lastDate = TimestampHelper.fromString(parts[0]);
                lastId = Integer.parseInt(parts[1]);
            }
        }

        public String toVersionString() {
            return TimestampHelper.toString(lastDate) + "," + lastId;
        }
    }
}
