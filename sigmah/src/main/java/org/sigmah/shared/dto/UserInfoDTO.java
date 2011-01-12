package org.sigmah.shared.dto;

import java.util.List;

import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dto.profile.ProfileDTO;

public class UserInfoDTO implements CommandResult {

    private static final long serialVersionUID = -6918917417021859161L;

    private OrganizationDTO organization;
    private OrgUnitDTO orgUnit;
    private List<ProfileDTO> profiles;
    private ProfileDTO aggregatedProfile;

    public OrganizationDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDTO organization) {
        this.organization = organization;
    }

    public OrgUnitDTO getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnitDTO orgUnit) {
        this.orgUnit = orgUnit;
    }

    public List<ProfileDTO> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<ProfileDTO> profiles) {
        this.profiles = profiles;
    }

    public ProfileDTO getAggregatedProfile() {
        return aggregatedProfile;
    }

    public void setAggregatedProfile(ProfileDTO aggregatedProfile) {
        this.aggregatedProfile = aggregatedProfile;
    }
}
