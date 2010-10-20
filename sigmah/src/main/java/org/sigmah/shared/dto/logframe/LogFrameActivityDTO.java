package org.sigmah.shared.dto.logframe;

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
}
