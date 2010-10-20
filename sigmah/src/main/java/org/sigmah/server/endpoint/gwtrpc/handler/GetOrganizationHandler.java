package org.sigmah.server.endpoint.gwtrpc.handler;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.shared.command.GetOrganization;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.Organization;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.OrganizationDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

/**
 * Handler for get organization command.
 * 
 * @author tmi
 * 
 */
public class GetOrganizationHandler implements CommandHandler<GetOrganization> {

    private static final Log log = LogFactory.getLog(GetOrganizationHandler.class);

    private final EntityManager em;
    private final Mapper mapper;

    @Inject
    public GetOrganizationHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }

    @Override
    public CommandResult execute(GetOrganization cmd, User user) throws CommandException {

        if (log.isDebugEnabled()) {
            log.debug("[execute] Getting organization cmd: " + cmd + ".");
        }

        Organization organization = null;

        // Retrieves organization by id.
        if (cmd.getOrganizationId() != null) {

            if (log.isDebugEnabled()) {
                log.debug("[execute] Retrieves organization by id.");
            }

            organization = em.find(Organization.class, cmd.getOrganizationId());
        }
        // Retrieves organization by member.
        else if (cmd.getUserId() != null) {

            final User member = em.find(User.class, cmd.getUserId());

            if (log.isDebugEnabled()) {
                log.debug("[execute] Retrieves organization by member: " + member + ".");
            }

            if (member != null) {
                organization = member.getOrganization();
            }
        }

        if (organization != null) {
            return mapper.map(organization, OrganizationDTO.class);
        } else {
            return null;
        }
    }
}
