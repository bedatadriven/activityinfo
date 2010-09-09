/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.sigmah.shared.command.GetSyncRegions;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.SyncRegion;
import org.sigmah.shared.command.result.SyncRegions;
import org.sigmah.shared.dao.UserDatabaseDAO;
import org.sigmah.shared.domain.AdminLevel;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.UserDatabase;
import org.sigmah.shared.exception.CommandException;

import java.util.*;

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
