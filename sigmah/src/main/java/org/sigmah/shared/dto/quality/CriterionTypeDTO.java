package org.sigmah.shared.dto.quality;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity quality.CriterionType.
 * 
 * @author tmi
 * 
 */
public class CriterionTypeDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 9171979198420463751L;

    @Override
    public String getEntityName() {
        return "quality.CriterionType";
    }

    // Type id.
    @Override
    public int getId() {
        final Integer id = (Integer) get("id");
        return id != null ? id : -1;
    }

    public void setId(int id) {
        set("id", id);
    }

    // Type label.
    public String getLabel() {
        return get("label");
    }

    public void setLabel(String label) {
        set("label", label);
    }

    // Type framework.
    public QualityFrameworkDTO getQualityFramework() {
        return get("qualityFramework");
    }

    public void setQualityFramework(QualityFrameworkDTO qualityFramework) {
        set("qualityFramework", qualityFramework);
    }

    // Type level.
    public Integer getLevel() {
        return (Integer) get("level");
    }

    public void setLevel(Integer level) {
        set("level", level);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[CriterionTypeDTO] ");
        sb.append("label: ");
        sb.append(getLabel());
        sb.append(" ; level: ");
        sb.append(getLevel());
        return sb.toString();
    }
}
