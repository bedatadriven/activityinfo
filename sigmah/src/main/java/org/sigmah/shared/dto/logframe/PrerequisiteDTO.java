package org.sigmah.shared.dto.logframe;

import java.util.Date;

import org.sigmah.client.page.project.logframe.grid.Row.Positionable;
import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity logframe.Prerequisite.
 * 
 * @author tmi
 * 
 */
public class PrerequisiteDTO extends BaseModelData implements EntityDTO, Positionable {

    private static final long serialVersionUID = 2491895571720689312L;

    @Override
    public String getEntityName() {
        return "logframe.Prerequisite";
    }

    // Prerequisite id.
    @Override
    public int getId() {
        final Integer id = (Integer) get("id");
        return id != null ? id : -1;
    }

    public void setId(int id) {
        set("id", id);
    }

    // Prerequisite code.
    public Integer getCode() {
        return get("code");
    }

    public void setCode(Integer code) {
        set("code", code);
    }

    // Prerequisite position in its group.
    public Integer getPosition() {
        return get("position");
    }

    @Override
    public void setPosition(Integer position) {
        set("position", position);
    }

    // Prerequisite content text.
    public String getContent() {
        return get("content");
    }

    public void setContent(String content) {
        set("content", content);
    }

    // Prerequisite parent log frame.
    public LogFrameDTO getParentLogFrameDTO() {
        return get("parentLogFrameDTO");
    }

    public void setParentLogFrameDTO(LogFrameDTO parentLogFrameDTO) {
        set("parentLogFrameDTO", parentLogFrameDTO);
    }

    // Prerequisite group.
    public LogFrameGroupDTO getLogFrameGroupDTO() {
        return get("logFrameGroupDTO");
    }

    public void setLogFrameGroupDTO(LogFrameGroupDTO logFrameGroupDTO) {
        set("logFrameGroupDTO", logFrameGroupDTO);
    }

    // Prerequisite deleted date.
    public Date getDateDeleted() {
        return get("dateDeleted");
    }

    public void setDateDeleted(Date dateDeleted) {
        set("dateDeleted", dateDeleted);
    }

    /**
     * Deletes this prerequisite.
     */
    public void delete() {
        setDateDeleted(new Date());
    }

    /**
     * Returns if this prerequisite is deleted.
     * 
     * @return If this prerequisite is deleted.
     */
    public boolean isDeleted() {
        return getDateDeleted() != null;
    }

    // Display label.
    /**
     * Sets the attribute <code>label</code> to display this element in a
     * selection window.
     */
    public void setLabel(String label) {
        set("label", label);
    }

    public String getLabel() {
        return get("label");
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
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PrerequisiteDTO [");
        sb.append("entity name = ");
        sb.append(getEntityName());
        sb.append(" ; id = ");
        sb.append(getId());
        sb.append(" ; group id = ");
        if (getLogFrameGroupDTO() != null) {
            sb.append(getLogFrameGroupDTO().getId() != -1 ? getLogFrameGroupDTO().getId() : getLogFrameGroupDTO()
                    .getClientSideId());
        }
        sb.append(" ; dlabel = ");
        sb.append(getLabel());
        sb.append(" ; date deleted = ");
        sb.append(getDateDeleted());
        sb.append(" ; position = ");
        sb.append(getPosition());
        sb.append(" ; content = ");
        sb.append(getContent());
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return getClientSideId();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof PrerequisiteDTO)) {
            return false;
        }

        final PrerequisiteDTO other = (PrerequisiteDTO) obj;
        return getClientSideId() == other.getClientSideId();
    }
}
