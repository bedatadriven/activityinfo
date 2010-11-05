package org.sigmah.shared.dto.quality;

import java.util.List;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity quality.QualityCriterion.
 * 
 * @author tmi
 * 
 */
public class QualityCriterionDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = -49281834964182785L;

    @Override
    public String getEntityName() {
        return "quality.QualityCriterion";
    }

    // Criterion id.
    @Override
    public int getId() {
        final Integer id = (Integer) get("id");
        return id != null ? id : -1;
    }

    public void setId(int id) {
        set("id", id);
    }

    // Criterion label.
    public String getLabel() {
        return get("label");
    }

    public void setLabel(String label) {
        set("label", label);
    }

    // Criterion sub criteria.
    public List<QualityCriterionDTO> getSubCriteria() {
        return get("subCriteria");
    }

    public void setSubCriteria(List<QualityCriterionDTO> subCriteria) {
        set("subCriteria", subCriteria);
    }
}
