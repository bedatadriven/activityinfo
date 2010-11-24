package org.sigmah.server.endpoint.gwtrpc.handler;

import javax.persistence.EntityManager;

import org.dozer.Mapper;
import org.sigmah.shared.command.GetOrgUnit;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.OrgUnit;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.OrgUnitDTO;
import org.sigmah.shared.exception.CommandException;

import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.Inject;

/**
 * {@link GetOrgUnit} command implementation.
 * 
 * @author tmi
 * 
 */
public class GetOrgUnitHandler implements CommandHandler<GetOrgUnit> {

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

        if (Log.isDebugEnabled()) {
            Log.debug("[execute] Getting org unit id#" + cmd.getId() + " from the database.");
        }

        final OrgUnit orgUnit = em.find(OrgUnit.class, cmd.getId());
        return mapper.map(orgUnit, OrgUnitDTO.class);
    }

}
