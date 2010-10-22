package org.sigmah.shared.dto.logframe;

import java.util.Date;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity logframe.Activity.
 * 
 * @author tmi
 * 
 */
public class LogFrameActivityDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 6134012388369233491L;

    /**
     * Returns the prefix code to identify an activity.
     * 
     * @return The prefix code to identify an activity.
     */
    public static String getPrefixCode() {
        return I18N.CONSTANTS.logFrameActivitiesCode();
    }

    @Override
    public String getEntityName() {
        return "logframe.Activity";
    }

    // Activity id.
    @Override
    public int getId() {
        final Integer id = (Integer) get("id");
        return id != null ? id : -1;
    }

    public void setId(int id) {
        set("id", id);
    }

    // Activity code.
    public Integer getCode() {
        return get("code");
    }

    public void setCode(Integer code) {
        set("code", code);
    }

    // Activity content text.
    public String getContent() {
        return get("content");
    }

    public void setContent(String content) {
        set("content", content);
    }

    // Activity title.
    public String getTitle() {
        return get("title");
    }

    public void setTitle(String title) {
        set("title", title);
    }

    // Activity start date.
    public Date getStartDate() {
        return get("startDate");
    }

    public void setStartDate(Date startDate) {
        set("startDate", startDate);
    }

    // Activity end date.
    public Date getEndDate() {
        return get("endDate");
    }

    public void setEndDate(Date endDate) {
        set("endDate", endDate);
    }

    // Activity parent result.
    public ExpectedResultDTO getParentExpectedResultDTO() {
        return get("parentExpectedResultDTO");
    }

    public void setParentExpectedResultDTO(ExpectedResultDTO parentExpectedResultDTO) {
        set("parentExpectedResultDTO", parentExpectedResultDTO);
    }

    // Result group.
    public LogFrameGroupDTO getLogFrameGroupDTO() {
        return get("logFrameGroupDTO");
    }

    public void setLogFrameGroupDTO(LogFrameGroupDTO logFrameGroupDTO) {
        set("logFrameGroupDTO", logFrameGroupDTO);
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

    // Activity deleted date.
    public Date getDateDeleted() {
        return get("dateDeleted");
    }

    public void setDateDeleted(Date dateDeleted) {
        set("dateDeleted", dateDeleted);
    }

    /**
     * Deletes this activity.
     */
    public void delete() {
        setDateDeleted(new Date());
    }

    /**
     * Returns if this activity is deleted.
     * 
     * @return If this activity is deleted.
     */
    public boolean isDeleted() {
        return getDateDeleted() != null;
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
        sb.append("LogFrameActivityDTO [");
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
        sb.append(" ; code = ");
        sb.append(getCode());
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

        if (!(obj instanceof LogFrameActivityDTO)) {
            return false;
        }

        final LogFrameActivityDTO other = (LogFrameActivityDTO) obj;
        return getClientSideId() == other.getClientSideId();
    }
}
