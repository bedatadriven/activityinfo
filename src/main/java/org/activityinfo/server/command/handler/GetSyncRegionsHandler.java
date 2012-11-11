/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.activityinfo.server.command.handler.sync.AdminUpdateBuilder;
import org.activityinfo.server.command.handler.sync.SiteTableUpdateBuilder;
import org.activityinfo.server.database.hibernate.dao.UserDatabaseDAO;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.GetSyncRegions;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.SyncRegion;
import org.activityinfo.shared.command.result.SyncRegions;
import org.activityinfo.shared.exception.CommandException;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class GetSyncRegionsHandler implements CommandHandler<GetSyncRegions> {

    private EntityManager entityManager;

    @Inject
    public GetSyncRegionsHandler(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public CommandResult execute(GetSyncRegions cmd, User user) throws CommandException {

    	List<Object[]> databases = entityManager.createQuery("SELECT " +	
    			"db.id, db.country.id, db.version, " +
    			"MAX(p.version) " +
    		"FROM UserDatabase db " +
    		"LEFT JOIN db.userPermissions p " +
    		"GROUP BY db.id")
    		.getResultList();
    	
    	List<Integer> databaseIds = Lists.newArrayList();
    	Set<Integer> countryIds = Sets.newHashSet();
    	long schemaVersion = 1;
    	for(Object[] db : databases) {
    		databaseIds.add((Integer)db[0]);
    		countryIds.add((Integer)db[1]);
    		if(db[2] != null) {
    			schemaVersion = Math.max(schemaVersion, (Long)db[2]);
    		}
    		if(db[3] != null) {
    			schemaVersion = Math.max(schemaVersion, (Long)db[3]);
    		}
    	}
    	
        List<SyncRegion> regions = new ArrayList<SyncRegion>();
        regions.add(new SyncRegion("schema", Long.toString(schemaVersion)));
        regions.addAll(listAdminRegions(countryIds));
        regions.addAll(listLocations(databaseIds));
        regions.addAll(listSiteRegions(databaseIds));
        return new SyncRegions(regions);
    }


    private Collection<? extends SyncRegion> listLocations(List<Integer> databases) {
        List<Object[]> regions = entityManager.createQuery("SELECT " +
    			"a.locationType.id, " +
    			"MAX(loc.timeEdited) " +
    		"FROM Activity a LEFT JOIN a.locationType.locations loc " +
    		"WHERE a.database.id in (:dbs) " +
    		"GROUP BY a.locationType")
    	.setParameter("dbs", databases)
    	.getResultList();
    	
    	List<SyncRegion> locationRegions = new ArrayList<SyncRegion>();
    	for(Object[] region : regions) {
    		if(region[1] != null) {
    			locationRegions.add(new SyncRegion("location/" + region[0], region[1].toString()));
    		}
    	}
    	return locationRegions;
	}

	private Collection<? extends SyncRegion> listAdminRegions(Set<Integer> countryIds) {
		
		 List<Integer> levels = entityManager.createQuery("SELECT " +
	    			"level.id " +
	    		"FROM AdminLevel level " +
	    		"WHERE level.country.id in (:countries) ")
	    		.setParameter("countries", countryIds)
	    		.getResultList();
		
        List<SyncRegion> adminRegions = new ArrayList<SyncRegion>();
        for(Integer level : levels) {

            adminRegions.add(new SyncRegion("admin/" + level,
            		Integer.toString(AdminUpdateBuilder.LAST_VERSION_NUMBER)));
        }
        return adminRegions;
    }

    /**
     * We need a separate sync region for each Partner/UserDatabase combination
     * because we may be given permission to view data at different times.
     *
     */
    private Collection<? extends SyncRegion> listSiteRegions(List<Integer> databases) {
        List<SyncRegion> siteRegions = new ArrayList<SyncRegion>();

        // our initial sync region manages the table schema
        siteRegions.add(new SyncRegion("site-tables", SiteTableUpdateBuilder.CURRENT_VERSION));

        // do one sync region per database
        List<Object[]> regions = entityManager.createQuery("SELECT " +
        			"s.activity.database.id, " +
        			"MAX(s.timeEdited) " +
        		" FROM Site s WHERE s.activity.database.id in (:dbs) GROUP BY s.activity.database.id")
        	.setParameter("dbs", databases)
        	.getResultList();
        
        for(Object[] region : regions) {
        	siteRegions.add(new SyncRegion("site/" + region[0], region[1].toString()));
        }
        
        return siteRegions;
    }
}
