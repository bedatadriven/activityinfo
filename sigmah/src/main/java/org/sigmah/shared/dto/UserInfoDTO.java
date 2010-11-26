package org.sigmah.shared.dto;

import org.sigmah.shared.command.result.CommandResult;

public class UserInfoDTO implements CommandResult {

    private static final long serialVersionUID = -6918917417021859161L;

    private OrganizationDTO organization;
    private OrgUnitDTO orgUnit;

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
}
