package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.shared.command.GetUserInfo;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.profile.GlobalPermission;
import org.sigmah.shared.domain.profile.GlobalPermissionEnum;
import org.sigmah.shared.domain.profile.PrivacyGroupPermission;
import org.sigmah.shared.domain.profile.PrivacyGroupPermissionEnum;
import org.sigmah.shared.domain.profile.Profile;
import org.sigmah.shared.dto.OrgUnitDTO;
import org.sigmah.shared.dto.OrganizationDTO;
import org.sigmah.shared.dto.UserInfoDTO;
import org.sigmah.shared.dto.profile.PrivacyGroupDTO;
import org.sigmah.shared.dto.profile.ProfileDTO;
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

            // Organization.
            infos.setOrganization(mapper.map(member.getOrganization(), OrganizationDTO.class));

            // Org unit.
            infos.setOrgUnit(mapper.map(member.getOrgUnitWithProfiles().getOrgUnit(), OrgUnitDTO.class));

            // Profiles.
            aggregateProfiles(member, infos);
        }

        return infos;
    }

    /**
     * Aggregates the list of profiles of a user.
     * 
     * @param user
     *            The user.
     * @param infos
     *            The optional user info to fill.
     * @return The aggregated profile DTO.
     */
    public ProfileDTO aggregateProfiles(User user, UserInfoDTO infos) {

        // Profiles.
        if (infos != null) {
            infos.setProfiles(new ArrayList<ProfileDTO>());
        }

        // The user may have several profiles which link it to its org unit.
        // This handler merges also all the profiles in one 'aggregated
        // profile'.
        final ProfileDTO aggretatedProfileDTO = new ProfileDTO();
        aggretatedProfileDTO.setName("AGGREGATED_PROFILE");
        aggretatedProfileDTO.setGlobalPermissions(new HashSet<GlobalPermissionEnum>());
        aggretatedProfileDTO.setPrivacyGroups(new HashMap<PrivacyGroupDTO, PrivacyGroupPermissionEnum>());

        if (user != null && user.getOrgUnitWithProfiles() != null
                && user.getOrgUnitWithProfiles().getProfiles() != null) {

            // For each profile.
            for (final Profile profile : user.getOrgUnitWithProfiles().getProfiles()) {

                // Creates the corresponding profile DTO.
                final ProfileDTO profileDTO = new ProfileDTO();
                profileDTO.setName(profile.getName());

                // Global permissions.
                profileDTO.setGlobalPermissions(new HashSet<GlobalPermissionEnum>());
                if (profile.getGlobalPermissions() != null) {
                    for (final GlobalPermission p : profile.getGlobalPermissions()) {
                        profileDTO.getGlobalPermissions().add(p.getPermission());

                        // Aggregates global permissions among profiles.
                        aggretatedProfileDTO.getGlobalPermissions().add(p.getPermission());
                    }
                }

                // Privacy groups.
                profileDTO.setPrivacyGroups(new HashMap<PrivacyGroupDTO, PrivacyGroupPermissionEnum>());
                if (profile.getPrivacyGroupPermissions() != null) {
                    for (final PrivacyGroupPermission p : profile.getPrivacyGroupPermissions()) {

                        final PrivacyGroupDTO groupDTO = mapper.map(p.getPrivacyGroup(), PrivacyGroupDTO.class);
                        profileDTO.getPrivacyGroups().put(groupDTO, p.getPermission());

                        // Aggregates privacy groups among profiles.
                        if (aggretatedProfileDTO.getPrivacyGroups().get(groupDTO) != PrivacyGroupPermissionEnum.WRITE) {
                            aggretatedProfileDTO.getPrivacyGroups().put(groupDTO, p.getPermission());
                        }
                    }
                }

                if (infos != null) {
                    infos.getProfiles().add(profileDTO);
                }
            }
        }

        if (infos != null) {
            infos.setAggregatedProfile(aggretatedProfileDTO);
        }

        return aggretatedProfileDTO;
    }
}
