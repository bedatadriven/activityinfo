/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler.sync;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.activityinfo.server.database.hibernate.entity.Location;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.GetSyncRegionUpdates;
import org.activityinfo.shared.command.result.SyncRegionUpdate;
import org.json.JSONException;

import com.bedatadriven.rebar.sync.server.JpaUpdateBuilder;
import com.google.inject.Inject;

public class LocationUpdateBuilder implements UpdateBuilder {
    private static final String REGION_PREFIX = "location/";

	public static final int MAX_UPDATES = 500;

    private final EntityManager em;
    private List<Location> locations;
    private Set<Integer> locationIds = new HashSet<Integer>();

	private LocalState localState;

	private JpaUpdateBuilder builder = new JpaUpdateBuilder();

	private int typeId;
    
    @Inject
    public LocationUpdateBuilder(EntityManager em) {
        this.em = em;
    }

    @Override
    public SyncRegionUpdate build(User user, GetSyncRegionUpdates request) throws JSONException {

    	typeId = parseTypeId(request);
        localState = new LocalState(request.getLocalVersion());

        queryChanged();
        assureTimestampsAreUnique();
        createAndUpdateLocations();
        linkAdminEntities();
                
        SyncRegionUpdate update = new SyncRegionUpdate();
        update.setVersion(locations.isEmpty() ? request.getLocalVersion() : lastVersion());
        update.setSql(builder.asJson());
        update.setComplete(locations.size() < MAX_UPDATES);
        return update;
    }

	private int parseTypeId(GetSyncRegionUpdates request) {
		if(!request.getRegionId().startsWith(REGION_PREFIX)) {
			throw new AssertionError("Expected region prefixed by '" + REGION_PREFIX + 
					"', got '" + request.getRegionId() + "'");
		}
		return Integer.parseInt(request.getRegionId().substring(REGION_PREFIX.length()));
	}

	private void queryChanged() {
		locations = em.createQuery("select loc from Location loc where loc.timeEdited > :localDate " +
                        " and locationType.id = :typeId " +
                        " order by loc.timeEdited, loc.id")
                .setParameter("typeId", typeId)
                .setParameter("localDate", localState.lastDate)
                .getResultList();
	}

	private void assureTimestampsAreUnique() {
		if(locations.size() == MAX_UPDATES + 1) {
        	if(locations.get(MAX_UPDATES - 1).getTimeEdited() == 
        			locations.get(MAX_UPDATES).getTimeEdited()) {
        		
        		throw new RuntimeException("timeEdited values on Location objects are not unique, cannot " +
        				"correctly batch updates");
        		
        	}
        	locations.remove(MAX_UPDATES);
        }
	}

	private void createAndUpdateLocations() throws JSONException {
		
		for(Location location : locations) {
			locationIds.add(location.getId());
		}
		
        builder.insert("or replace", Location.class, locations);
	}
	
	private void linkAdminEntities() throws JSONException { 

		if(!locations.isEmpty()) {
			builder.beginPreparedStatement("delete from LocationAdminLink where LocationId = ?");
			for(Location updateLoc : locations) {
				builder.addExecution(updateLoc.getId());
			}
			builder.finishPreparedStatement();

			List<Object[]> joins = em.createQuery("SELECT loc.id, e.id FROM Location loc JOIN loc.adminEntities e WHERE loc.id IN (:ids)")
			.setParameter("ids", locationIds)
			.getResultList();

			if(!joins.isEmpty()) {
				builder.beginPreparedStatement("insert into LocationAdminLink (LocationId, AdminEntityId) values (?, ?)");
				for(Object[] join : joins) {
					builder.addExecution(join[0], join[1]);
				}
				builder.finishPreparedStatement();
			}
		}
	}
	
    private String lastVersion() {
        if(locations.isEmpty()) {
            return "0";
        }

        return new LocalState(locations.get(locations.size()-1)).toVersionString();
    }


    private class LocalState {
        private long lastDate;

        public LocalState(Location lastLocation) {
            lastDate = lastLocation.getTimeEdited();
        }

        public LocalState(String cookie) {
            if(cookie == null) {
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
