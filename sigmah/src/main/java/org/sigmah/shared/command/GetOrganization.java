package org.sigmah.shared.command;

import org.sigmah.shared.dto.OrganizationDTO;

/**
 * Retrieves an organization with the given id or to which a user belongs.
 * 
 * @author tmi
 * 
 */
public class GetOrganization implements Command<OrganizationDTO> {

    private static final long serialVersionUID = 3131467894559905726L;

    /**
     * Found organization by id.
     */
    private Integer organizationId;

    /**
     * Found organization by user (as a member of this organization).
     */
    private Integer userId;

    public GetOrganization() {
        // serialization
    }

    public GetOrganization(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + organizationId;
        result = prime * result + userId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GetOrganization other = (GetOrganization) obj;
        if (organizationId != other.organizationId)
            return false;
        if (userId != other.userId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("GetOrganization[organization id: ");
        sb.append(organizationId);
        sb.append("; user id: ");
        sb.append(userId);
        sb.append("]");
        return sb.toString();
    }
}
