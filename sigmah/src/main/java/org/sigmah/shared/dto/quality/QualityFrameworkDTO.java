package org.sigmah.shared.dto.quality;

import java.util.List;

import org.sigmah.shared.dto.EntityDTO;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity quality.QualityFramework.
 * 
 * @author tmi
 * 
 */
public class QualityFrameworkDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = -1494859762914765504L;

    @Override
    public String getEntityName() {
        return "quality.QualityFramework";
    }

    // Framework id.
    @Override
    public int getId() {
        final Integer id = (Integer) get("id");
        return id != null ? id : -1;
    }

    public void setId(int id) {
        set("id", id);
    }

    // Framework label.
    public String getLabel() {
        return get("label");
    }

    public void setLabel(String label) {
        set("label", label);
    }

    // Criteria.
    public List<QualityCriterionDTO> getCriteria() {
        return get("criteria");
    }

    public void setCriteria(List<QualityCriterionDTO> criteria) {
        set("criteria", criteria);
    }

    // Types.
    public List<CriterionTypeDTO> getTypes() {
        return get("types");
    }

    public void setTypes(List<CriterionTypeDTO> types) {
        set("types", types);
    }

    /**
     * Retrieves the criterion type with the given level.
     * 
     * @param level
     *            The level.
     * @return The criterion type.
     */
    public CriterionTypeDTO getType(int level) {

        Log.debug(toString());

        if (level < 0) {
            return null;
        }

        if (getTypes() == null) {
            return null;
        }

        for (final CriterionTypeDTO type : getTypes()) {
            if (type.getLevel() == level) {
                return type;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[QualityFrameworkDTO]\n");
        sb.append("label: ");
        sb.append("\ntypes:");
        if (getTypes() != null) {
            for (final CriterionTypeDTO type : getTypes()) {
                sb.append("\n");
                sb.append(type);
            }
        }
        sb.append("\ncriteria: ");
        if (getCriteria() != null) {
            for (final QualityCriterionDTO criterion : getCriteria()) {
                sb.append(criterion);
            }
        }
        return sb.toString();
    }
}
