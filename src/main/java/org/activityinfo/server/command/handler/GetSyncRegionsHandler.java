/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.activityinfo.server.database.hibernate.dao.UserDatabaseDAO;
import org.activityinfo.server.database.hibernate.entity.AdminLevel;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.shared.command.GetSyncRegions;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.SyncRegion;
import org.activityinfo.shared.command.result.SyncRegions;
import org.activityinfo.shared.exception.CommandException;

import com.google.inject.Inject;

public class GetSyncRegionsHandler implements CommandHandler<GetSyncRegions> {

    private UserDatabaseDAO schemaDAO;

    @Inject
    public GetSyncRegionsHandler(UserDatabaseDAO schemaDAO) {
        this.schemaDAO = schemaDAO;
    }

    @Override
    public CommandResult execute(GetSyncRegions cmd, User user) throws CommandException {

        List<UserDatabase> databases = schemaDAO.queryAllUserDatabasesAlphabetically();

        List<SyncRegion> regions = new ArrayList<SyncRegion>();
        regions.add(new SyncRegion("schema", true));
        regions.addAll(listAdminRegions(databases));
        regions.add(new SyncRegion("locations"));
        regions.addAll(listSiteRegions(databases));
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

    /**
     * We need a separate sync region for each Partner/UserDatabase combination
     * because we may be given permission to view data at different times.
     *
     */
    private Collection<? extends SyncRegion> listSiteRegions(List<UserDatabase> databases) {
        List<SyncRegion> siteRegions = new ArrayList<SyncRegion>();

        // our initial sync region manages the table schema
        siteRegions.add(new SyncRegion("site-tables"));

        // do one sync region per database
        for(UserDatabase db : databases) {
        	siteRegions.add(new SyncRegion("site/" + db.getId()));
        }
        
        return siteRegions;
    }

}
