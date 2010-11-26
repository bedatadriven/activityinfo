package org.sigmah.server.endpoint.gwtrpc.handler;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.shared.command.GetUserInfo;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.OrgUnitDTO;
import org.sigmah.shared.dto.OrganizationDTO;
import org.sigmah.shared.dto.UserInfoDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

/**
 * Handler for get organization command.
 * 
 * @author tmi
 * 
 */
public class GetUserInfoHandler implements CommandHandler<GetUserInfo> {

    private static final Log log = LogFactory.getLog(GetUserInfoHandler.class);

    private final EntityManager em;
    private final Mapper mapper;

    @Inject
    public GetUserInfoHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }

    @Override
    public CommandResult execute(GetUserInfo cmd, User user) throws CommandException {

        if (log.isDebugEnabled()) {
            log.debug("[execute] Getting user info cmd: " + cmd + ".");
        }

        final UserInfoDTO infos = new UserInfoDTO();

        if (cmd.getUserId() != null) {

            final User member = em.find(User.class, cmd.getUserId());

            if (log.isDebugEnabled()) {
                log.debug("[execute] Retrieves user info by member: " + member + ".");
            }

            infos.setOrganization(mapper.map(member.getOrganization(), OrganizationDTO.class));
            infos.setOrgUnit(mapper.map(member.getOrgUnit(), OrgUnitDTO.class));
        }

        return infos;
    }
}
