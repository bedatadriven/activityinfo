package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.HashSet;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.shared.command.GetOrgUnit;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.OrgUnit;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.OrgUnitDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

/**
 * {@link GetOrgUnit} command implementation.
 * 
 * @author tmi
 * 
 */
public class GetOrgUnitHandler implements CommandHandler<GetOrgUnit> {

    private final static Log LOG = LogFactory.getLog(GetOrgUnitHandler.class);

    private final EntityManager em;
    private final Mapper mapper;

    @Inject
    public GetOrgUnitHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }

    /**
     * Gets an aorg unit from the database and maps it into a {@link OrgUnitDTO}
     * object.
     * 
     * @param cmd
     *            command containing the org unit id
     * @param user
     *            user connected
     * 
     * @return the {@link OrgUnitDTO} object.
     */
    @Override
    public CommandResult execute(GetOrgUnit cmd, User user) throws CommandException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("[execute] Getting org unit id#" + cmd.getId() + " from the database.");
        }

        final OrgUnit orgUnit = em.find(OrgUnit.class, cmd.getId());

        if (orgUnit == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Org unit id#" + cmd.getId() + " doesn't exist.");
            }

            return null;
        } else {

            if (isOrgUnitVisible(orgUnit, user)) {
                return mapper.map(orgUnit, OrgUnitDTO.class);
            }
            // The user cannot see this org unit.
            else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("[execute] User cannot see org unit id#" + cmd.getId() + ".");
                }

                return null;
            }
        }
    }

    /**
     * Returns if the org unit is visible for the given user.
     * 
     * @param orgUnit
     *            The org unit.
     * @param user
     *            The user.
     * @return If the org unit is visible for the user.
     */
    public static boolean isOrgUnitVisible(OrgUnit orgUnit, User user) {

        // Checks that the user can see this org unit.
        final HashSet<OrgUnit> units = new HashSet<OrgUnit>();
        GetProjectHandler.crawlUnits(user.getOrgUnitWithProfiles().getOrgUnit(), units, true);

        for (final OrgUnit unit : units) {
            if (orgUnit.getId() == unit.getId()) {
                return true;
            }
        }

        return false;
    }
}
