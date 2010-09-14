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

import javax.persistence.EntityManager;
import java.util.*;

public class GetSyncRegionsHandler implements CommandHandler<GetSyncRegions> {

    private UserDatabaseDAO schemaDAO;
    private final EntityManager entityManager;

    @Inject
    public GetSyncRegionsHandler(UserDatabaseDAO schemaDAO, EntityManager entityManager) {
        this.schemaDAO = schemaDAO;
        this.entityManager = entityManager;
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
     * We need a separate sync region for each OrgUnit/UserDatabase combination
     * because we may be given permission to view data at different times.
     *
     */
    private Collection<? extends SyncRegion> listSiteRegions(List<UserDatabase> databases) {

        List<Object[]> pairs = entityManager.createQuery(
                "SELECT db.id, unit.id FROM UserDatabase db JOIN db.partners unit")
                    .getResultList();

        List<SyncRegion> siteRegions = new ArrayList<SyncRegion>();
        for(Object[] pair : pairs) {
            siteRegions.add(new SyncRegion("site/" + pair[0] + "/" + pair[1]));
        }
        return siteRegions;
    }

}
