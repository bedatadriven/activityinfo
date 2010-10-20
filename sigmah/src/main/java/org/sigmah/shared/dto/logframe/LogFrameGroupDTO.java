package org.sigmah.shared.dto.logframe;

import java.util.Date;

import org.sigmah.shared.domain.logframe.LogFrameGroupType;
import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity logframe.LogFrameGroup.
 * 
 * @author tmi
 * 
 */
public class LogFrameGroupDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = -2559578621723205905L;

    @Override
    public String getEntityName() {
        return "logframe.LogFrameGroup";
    }

    // Group id.
    @Override
    public int getId() {
        final Integer id = (Integer) get("id");
        return id != null ? id : -1;
    }

    public void setId(int id) {
        set("id", id);
    }

    // Group label.
    public String getLabel() {
        return get("label");
    }

    public void setLabel(String label) {
        set("label", label);
    }

    // Group type.
    public LogFrameGroupType getType() {
        return get("type");
    }

    public void setType(LogFrameGroupType type) {
        set("type", type);
    }

    // Objective parent log frame.
    public LogFrameDTO getParentLogFrameDTO() {
        return get("parentLogFrameDTO");
    }

    public void setParentLogFrameDTO(LogFrameDTO parentLogFrameDTO) {
        set("parentLogFrameDTO", parentLogFrameDTO);
    }

    /**
     * Gets the client-side id for this entity. If this entity has a server-id
     * id, it's returned. Otherwise, a temporary id is generated and returned.
     * 
     * @return The client-side id.
     */
    public int getClientSideId() {

        // Server-side id.
        Integer id = (Integer) get("id");

        if (id == null) {

            // Client-side id.
            id = (Integer) get("tmpid");

            // Generates the client-side id once.
            if (id == null) {
                id = generateClientSideId();
            }
        }

        return id;
    }

    /**
     * Generate a client-side unique id for this entity and stores it in the
     * <code>temporaryId</code> attribute.
     */
    private int generateClientSideId() {
        final int id = (int) new Date().getTime();
        set("tmpid", id);
        return id;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof LogFrameGroupDTO)) {
            return false;
        }

        final LogFrameGroupDTO other = (LogFrameGroupDTO) obj;
        return getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("LogFrameGroupDTO [");
        sb.append("entity name = ");
        sb.append(getEntityName());
        sb.append(" ; id = ");
        sb.append(getId());
        sb.append(" ; client side id = ");
        sb.append(getClientSideId());
        sb.append(" ; label = ");
        sb.append(getLabel());
        sb.append(" ; type = ");
        sb.append(getType());
        sb.append("]");
        return sb.toString();
    }
}
