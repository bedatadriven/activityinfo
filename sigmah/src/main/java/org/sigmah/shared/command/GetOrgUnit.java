package org.sigmah.shared.command;

import org.sigmah.shared.dto.OrgUnitDTO;

/**
 * Retrieves an org unit with the given id.
 * 
 * @author tmi
 * 
 */
public class GetOrgUnit implements Command<OrgUnitDTO> {

    private static final long serialVersionUID = 5675515456984800856L;

    private int id;

    public GetOrgUnit() {
        // required, or serialization exception
    }

    public GetOrgUnit(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
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
        GetOrgUnit other = (GetOrgUnit) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
